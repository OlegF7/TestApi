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
    public void jsonBodyHashMap() {
        HashMap<String,Object> dataBody = new HashMap<>();

        dataBody.put("realname", "Paul Potts");

        Response response = RestAssured
                .given()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded")
                    .cookies(cookies)
                    .queryParams(dataBody)
                .when()
                    .post("https://academ-it.ru/mantisbt/account_update.php")
                    .andReturn();
        response.prettyPrint();
        assertEquals(200, response.statusCode(), "Response status code is not as expected");
        assertTrue(response.body().asString().contains("Real name successfully updated"));
    }
    @Test
    public void getRealName() {
        Response response = RestAssured
                .given()
                    .log().all()
                    .cookies(cookies)
                    .get("https://academ-it.ru/mantisbt/my_view_page.php")
                .andReturn();

        System.out.println("\nResponse");
        response.prettyPrint();

        assertEquals(200, response.statusCode(), "Response status code is not as expected");
        assertTrue(response.body().asString().contains("Paul Potts"));
    }
}