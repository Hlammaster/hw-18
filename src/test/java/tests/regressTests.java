package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import models.lombok.UpdateAccBodyLombokModel;
import models.lombok.UpdateAccBodyResponseLombokModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class regressTests {

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
        String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));


    }

    @Test
    void updateUserAccountTest() {
        UpdateAccBodyLombokModel accountBody = new UpdateAccBodyLombokModel();
        accountBody.setName("morpheus");
        accountBody.setJob("zion resident");

       UpdateAccBodyResponseLombokModel response = given()
               .filter(new AllureRestAssured())
                .log().uri()
                .log().body()
                .body(accountBody)
                .contentType(ContentType.JSON)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
               .extract().as(UpdateAccBodyResponseLombokModel.class);

        assertThat(response.getName()).isEqualTo("morpheus");
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

