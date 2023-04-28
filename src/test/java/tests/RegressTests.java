package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import models.lombok.UpdateAccBodyLombokModel;
import models.lombok.UpdateAccBodyResponseLombokModel;
import models.lombok.UsersListResponseModel;
import models.pogo.CreateResponseModel;
import org.junit.jupiter.api.Test;
import specs.LoginSpecs;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpecs.*;

public class RegressTests {


    @Test
    void getUsersListTest() {
        given()
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/status-scheme-responce.json"));


    }
    @Test
    void createUserTest() {
        UpdateAccBodyLombokModel accountBody = new UpdateAccBodyLombokModel();
        accountBody.setName("morpheus");
        accountBody.setJob("leader");

        CreateResponseModel response =
                given(loginRequestSpec)
                        .body(accountBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(createResponseSpec)
                        .extract().as(CreateResponseModel.class);


        assertThat(response.getName()).isEqualTo("morpheus");
        assertThat(response.getJob()).isEqualTo("leader");


    }

    @Test
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
    void deleteUserTest() {

        given()
                .log().uri()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().all()
                .statusCode(204);


    }

    @Test
    void unsuccessfulRegistrationTest() {
        String body = "{ \"email\": \"sydney@fife\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));


    }


}

