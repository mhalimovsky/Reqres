import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

public class ReqresTest {

    public static final String URL = "https://reqres.in/";

    @Test

    public void listUsersTest() {
        when().
                get(URL + "api/users?page=2").
        then().
                log().all().
                statusCode(200).
                body("page", equalTo(2), "total", equalTo(12));
    }

    @Test
    public void singleUserTest() {
        when().
                get(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2), "data.email", equalTo("janet.weaver@reqres.in"),
                        "data.first_name", equalTo("Janet"), "data.last_name", equalTo("Weaver"),
                        "support.url", equalTo("https://reqres.in/#support-heading"));
    }

    @Test
    public void singleUserNotFoundTest() {
        when().
                get(URL + "api/users/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void listResourceTest() {
        when().
                get(URL + "api/unknown").
        then().
                log().all().
                statusCode(200).
                body("total_pages", equalTo(2), "data.id", hasItems(1, 2, 3, 4, 5, 6),
                        "data.year", hasItems(2000, 2001, 2002, 2003, 2004, 2005));
    }

    @Test
    public void singleResourceTest() {
        when().
                get(URL + "api/unknown/2").
        then().
                log().all().
                statusCode(200).
                body("data.id", equalTo(2),
                        "data.year", equalTo(2001));
    }

    @Test
    public void singleResourceNotFoundTest() {
        when().
                get(URL + "api/unknown/23").
        then().
                log().all().
                statusCode(404);
    }

    @Test
    public void createTest() {
        given()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post(URL + "api/users").
        then().
                log().all().
                statusCode(201).
                body("name", equalTo("morpheus"),
                        "job", equalTo("leader"));
    }

    @Test
    public void UpdateTest() {
        given()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                put(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"));
    }

    @Test
    public void UpdatePatchTest() {
        given()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                patch(URL + "api/users/2").
        then().
                log().all().
                statusCode(200).
                body("name", equalTo("morpheus"),
                        "job", equalTo("zion resident"));
    }

    @Test
    public void deleteTest() {
        when().
                delete(URL + "api/users/2").
        then().
                log().all().
                statusCode(204);
    }

    @Test
    public void SuccessfulRegisterTest() {
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post(URL + "api/register").
        then().
                log().all().
                statusCode(200).
                body("id", equalTo(4),
                        "token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void unSuccessfulRegisterTest() {
        given()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post(URL + "api/register").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }

    @Test
    public void SuccessfulLoginTest() {
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post(URL + "api/login").
        then().
                log().all().
                statusCode(200).
                body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void unSuccessfulLoginTest() {
        given()
                .body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}").
                header("Content-Type", "application/json").
                log().all().
        when().
                post(URL + "api/login").
        then().
                log().all().
                statusCode(400).
                body("error", equalTo("Missing password"));
    }

    @Test
    public void delayedResponseTest() {
        when().
                get(URL + "api/users?delay=3").
        then().
                log().all().
                statusCode(200).
                body("data.id", hasItems(1, 2, 3, 4, 5, 6), "page", equalTo(1),
                        "per_page", equalTo(6), "total", equalTo(12));
    }
}

