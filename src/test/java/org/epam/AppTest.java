package org.epam;


import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class AppTest {
    private static final String BASE_URL_USERS = "https://jsonplaceholder.typicode.com/users";
    private RequestSpecification requestSpecification;


    @BeforeMethod
    public void authSetUp() {
        requestSpecification = RestAssured.given().auth().none();
        setCommonParams(requestSpecification);
    }

    private void setCommonParams(RequestSpecification requestSpecification) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);
    }

    @Test
    public void testStatusCode() {
        requestSpecification
                .expect()
                .statusCode(HttpStatus.SC_OK)
                .log().ifError()
                .when()
                .get(BASE_URL_USERS);
    }

    @Test
    public void testHeader() {
        var response = requestSpecification
                .when()
                .get(BASE_URL_USERS);

        String header = response.contentType();
        Assert.assertNotNull(header);
        Assert.assertEquals(header, "application/json; charset=utf-8");
    }

    @Test
    public void testBody() {
        requestSpecification
                .when()
                .get(BASE_URL_USERS)
                .then()
                .assertThat()
                .body("size()", equalTo(10));
    }
}
