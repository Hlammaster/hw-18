package tests;

import io.restassured.http.ContentType;
import models.lombok.UnSucRegistrationModel;
import models.lombok.UpdateAccBodyLombokModel;
import models.lombok.UpdateAccBodyResponseLombokModel;
import models.lombok.getUsersRequestModel;
import models.pogo.CreateResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpecs.*;

public class RegressTests {


    @Test
    @DisplayName("Запрос списка пользователей")
    void getUsersListTest() {
        step("Make request", () ->
        given(loginRequestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(getUsersResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemes/status-scheme-responce.json")));

    }

    @Test
    @DisplayName("Создание нового профиля")
    void createUserTest() {
        UpdateAccBodyLombokModel accountBody = new UpdateAccBodyLombokModel();
        accountBody.setName("morpheus");
        accountBody.setJob("leader");

        CreateResponseModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(accountBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createResponseSpec)
                        .extract().as(CreateResponseModel.class));

        step("Verify response", () ->
                assertThat(response.getName()).isEqualTo("morpheus"));
        assertThat(response.getJob()).isEqualTo("leader");


    }

    @Test
    @DisplayName("Редактирование профиля")
    void updateUserAccountTest() {
        UpdateAccBodyLombokModel accountBody = new UpdateAccBodyLombokModel();
        accountBody.setName("morpheus");
        accountBody.setJob("zion resident");

        UpdateAccBodyResponseLombokModel response = step("Make request", () ->
                given(loginRequestSpec)
                        .body(accountBody)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(loginResponseSpec)
                        .extract().as(UpdateAccBodyResponseLombokModel.class));
        step("Verify response", () ->
                assertThat(response.getName()).isEqualTo("morpheus"));
        assertThat(response.getJob()).isEqualTo("zion resident");


    }

    @Test
    @DisplayName("Удаление профиля")
    void deleteUserTest() {
        step("Make request", () ->
        given(loginRequestSpec)
                .when()
                .delete("/users/2")
                .then()
                .spec(deleteResponseSpec));


    }

    @Test
    @DisplayName("Неуспешная регистрация")
    void unsuccessfulRegistrationTest() {
        step("Make request", () -> {
            UnSucRegistrationModel accountbody = new UnSucRegistrationModel();
            accountbody.setEmail("sydney@fife");
            given(loginRequestSpec)
                    .body(accountbody)
                    .when()
                    .post("/register")
                    .then()
                    .spec(unSucRegistrationResponseSpec)
                    .body("error", is("Missing password"));

        });
    }


}

