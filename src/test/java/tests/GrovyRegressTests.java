package tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static specs.LoginSpecs.getUsersResponseSpec;
import static specs.LoginSpecs.loginRequestSpec;

public class GrovyRegressTests {

    @Test
    @Feature("Api Tests")
    @DisplayName("Запрос списка пользователей")
    void getUsersListTest() {
        step("Make request", () ->
                given(loginRequestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(getUsersResponseSpec)
                        .body("data.findAll{it.email =~/./}.email.flatten()",
                                hasItem("tobias.funke@reqres.in"))
                        .body("data.findAll{it.id =~/./}.id.flatten()",
                                hasItem(8))
                        .body("data.findAll{it.last_name =~/./}.last_name.flatten()",
                                hasItem("Funke")));

    }

}