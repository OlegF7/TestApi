package api;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.junit.jupiter.api.Test;
import java.util.List;


public class RegresTest {

    @Test
    void Reqres (){
        String orderReq = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
    RestAssured
            .given()
                .log().all()
            .when()
                .contentType(ContentType.JSON)
                .body(orderReq)
                .post("https://reqres.in/api/users")
            .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    void Login (){
        String loginReq = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";
        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(loginReq)
                    .post("https://reqres.in/api/login")
                .then()
                    .log().all()
                    .statusCode(200);
    }
    @Test
    void Update (){
        String UpdateReq = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .body(UpdateReq)
                    .patch("https://reqres.in/api/users/2")
                .then()
                    .log().all()
                    .statusCode(200);
    }
    @Test
    void Users (){
        RestAssured
                .given()
                    .log().all()
                .when()
                    .contentType(ContentType.JSON)
                    .get("https://reqres.in/api/users/3")
                .then()
                    .log().all()
                    .statusCode(404);
    }

    @Test
    public void Getdelayedresponse(){
        ValidatableResponse response = RestAssured
                .given()
                    .param("delay", "3")
                .when()
                    .get("https://reqres.in/api/users")
                    .andReturn()
                .then()
                    .log().all()
                    .statusCode(200);

    }
}
