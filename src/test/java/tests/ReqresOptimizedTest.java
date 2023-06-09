package tests;

import models.ReqresBodyLombokModel;
import models.ReqresResponseLombokModel;
import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.reqresRequestSpec;
import static specs.ReqresSpec.reqresResponseSpec;

@Tag("opti_test")
@DisplayName("Оптимизированные api тесты на запросы Reqres")
public class ReqresOptimizedTest {

    @Test
    @DisplayName("Проверка статус кода и валидности json при запросе пользователей")
    void singleUserCheckSchemeTest() {

        step("Check response for get request", () ->
        given(reqresRequestSpec)
                .when()
                .get()
                .then()
                .spec(reqresResponseSpec)
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemes/singleUserResponceScheme.json")));
    }

    @Test
    @DisplayName("Проверка запроса на смену работы пользователя")
    void changeMorpheusJobTest(){

        ReqresBodyLombokModel reqresBody = new ReqresBodyLombokModel();
        reqresBody.setName("morpheus");
        reqresBody.setJob("QA automation");

        String expectedJob = "QA automation";

        ReqresResponseLombokModel response = step("Make request for change job", () ->
                given(reqresRequestSpec)
                        .body(reqresBody)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract().as(ReqresResponseLombokModel.class));

        step("Verify expected job", () ->
                assertThat(response.getJob()).isEqualTo(expectedJob));

    }

    @Test
    @DisplayName("Проверка статус кода при запросе на удаление пользователя")
    void checkStatusCodeForDelete(){

        step("Check status code for delete request", () ->
        given(reqresRequestSpec)
                .when()
                .delete()
                .then()
                .spec(reqresResponseSpec)
                .statusCode(204));
    }

    @Test
    @DisplayName("Проверка запроса на создание пользователя")
    void checkCreateUser(){

        ReqresBodyLombokModel reqresBody = new ReqresBodyLombokModel();
        reqresBody.setName("morpheus");
        reqresBody.setJob("QA automation");

        ReqresResponseLombokModel response = step("Make request for create user", () ->
        given(reqresRequestSpec)
                .body(reqresBody)
                .when()
                .post()
                .then()
                .spec(reqresResponseSpec)
                .statusCode(201)
                .extract().as(ReqresResponseLombokModel.class));

        step("Verify expected job", () ->
                assertThat(response.getJob()).isEqualTo("QA automation"));
        step("Verify expected name", () ->
                assertThat(response.getName()).isEqualTo("morpheus"));
    }

    @Test
    @DisplayName("Проверка запроса на смену работы пользователя")
    void changeJobTest(){

        User user = new User();
        user.setName("morpheus");
        user.setJob("QA");

        step("Make request for change job", () ->
                given(reqresRequestSpec)
                        .body(user)
                        .when()
                        .put("/users/2")
                        .then()
                        .spec(reqresResponseSpec)

                        .statusCode(200)
                        .extract().as(User.class));

        // @formatter:on
        step("Verify expected job", () ->
                assertEquals("QA", user.getJob()));

    }
}
