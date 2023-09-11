package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestAssuredMantis {
    private String PHPSESSID;
    private String MANTIS_SECURE_SESSION;
    private String MANTIS_STRING_COOKIE;
    private Map<String, String> cookies = new HashMap<>();

    @BeforeEach
    public void getCookies() {
        Response responselogin = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .body("return=index.php&username=admin&password=admin20&secure_session=on")
                .post("https://academ-it.ru/mantisbt/login.php")
                .andReturn();

        PHPSESSID = responselogin.cookie("PHPSESSID");
        System.out.println("PHPSESSID = " + PHPSESSID);

        MANTIS_SECURE_SESSION = responselogin.cookie("MANTIS_SECURE_SESSION");
        System.out.println("MANTIS_SECURE_SESSION = " + MANTIS_SECURE_SESSION);

        MANTIS_STRING_COOKIE = responselogin.cookie("MANTIS_STRING_COOKIE");
        System.out.println("MANTIS_STRING_COOKIE = " + MANTIS_STRING_COOKIE);

        cookies.put("PHPSESSID", PHPSESSID);
        cookies.put("MANTIS_SECURE_SESSION", MANTIS_SECURE_SESSION);
        cookies.put("MANTIS_STRING_COOKIE", MANTIS_STRING_COOKIE);
    }

    @Test
    public void getRealNameString() {
        Response response = RestAssured
                .given()
                    .cookies(cookies)
                    .get("https://academ-it.ru/mantisbt/account_page.php")
                    .andReturn();

        System.out.println("\nResponse");
        response.prettyPrint();

        assertEquals(200, response.statusCode(), "Response status code is not as expected");
        assertTrue(response.body().asString().contains("Real Name"));
    }



    @Test
    public void postRealName() {

        String name = "[{\"key\":\"email\",\"value\":\"rov55an3014@mail.ru\"}," +
                "{\"key\":\"password\",\"value\":\"\"}," +
                "{\"key\":\"password_confirm\",\"value\":\"\"}," +
                "{\"key\":\"password_current\",\"value\":\"\"}," +
                "{\"key\":\"realname\",\"value\":\"Tom\"}]";

        Response response = RestAssured
                .given()
                    .log().all()
                    .body(name)
                .when()
                    .contentType("application/x-www-form-urlencoded")
                    .cookies(cookies)
                    .post("https://academ-it.ru/mantisbt/account_update.php")
                    .andReturn();

        System.out.println("\nResponse");
        response.prettyPrint();

        assertEquals(200, response.statusCode(), "Response status code is not as expected");
        assertTrue(response.body().asString().contains("Bob"));
    }

//    @Test
//    public void testGptPost(){
//                    // Set base URI
//            RestAssured.baseURI = "https://academ-it.ru";
//
//            // Define request body
//            String requestBody = "[{\"key\":\"email\",\"value\":\"rov55an3014@mail.ru\"},{\"key\":\"password\",\"value\":\"\"},{\"key\":\"password_confirm\",\"value\":\"\"},{\"key\":\"password_current\",\"value\":\"\"},{\"key\":\"realname\",\"value\":\"Bob\"}]";
//
//            // Send POST request
//            Response response = RestAssured
//                    .given()
//                        .header("Content-Type", "application/x-www-form-urlencoded")
//                        .body(requestBody)
//                        .cookies(cookies)
//                        .post("/mantisbt/account_update.php")
//                        .andReturn();
//                        response.prettyPrint();
//
//            // Get and print response
//            int statusCode = response.getStatusCode();
//            String responseBody = response.getBody().asString();
//
//            System.out.println("Status Code: " + statusCode);
//            System.out.println("Response Body: " + responseBody);
//
//    }
}
