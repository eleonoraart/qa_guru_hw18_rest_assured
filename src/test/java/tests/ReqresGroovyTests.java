package tests;

import models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.reqresRequestSpec;
import static specs.ReqresSpec.reqresResponseSpec;

public class ReqresGroovyTests {

    @Test
    @DisplayName("Проверка запроса на смену работы пользователя")
    void changeMorpheusJobTest(){

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

    @Test
    @DisplayName("Проверка значений атрибутов полученных цветов")
    void checkColor(){

        // @formatter:off
        step("Make request for get colors", () ->
                given(reqresRequestSpec)
                   .when()
                   .get("/unknown")
                .then()
                   .spec(reqresResponseSpec)
                   .statusCode(200)
                   .body("data.color[3]",
                           equalTo("#7BC4C4"))
                   .and()
                   .body("data.findAll{it.name =~/./}.name.flatten()",
                           hasItem("aqua sky"))
                   .and()
                   .body("data.pantone_value.flatten()",
                           hasItems("15-4020", "17-2031", "19-1664", "14-4811", "17-1456", "15-5217")));
        // @formatter:on
    }

}
