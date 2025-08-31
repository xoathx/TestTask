package org.example;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import org.example.Config.ConfigProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteTest {

    private static URI url;

    @BeforeAll
    public static void setup() throws URISyntaxException {
        url = new URI(ConfigProvider.URL);
    }

    @Test
    @DisplayName("Позитивный тест удаления")
    public void positiveDeleteTest(){
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
        .when()
                .delete(url + "/{id}")
        .then()
                .statusCode(200)
                .body(equalTo("{}")).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "DELETE https://jsonplaceholder.typicode.com/posts/1");
        Allure.addAttachment("Response Status", "text/plain",
                "Status Code: " + response.getStatusCode());
        Allure.addAttachment("Response Headers", "text/plain",
                response.getHeaders().toString());
        Allure.addAttachment("Response Body", "application/json",
                response.getBody().asString());
        Allure.addAttachment("Response Time", "text/plain",
                "Response time: " + response.getTime() + "ms");
    }

    @Test
    @DisplayName("Проверка статуса кода")
    public void testDeletePostStatusCode() {
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
        .when()
                .delete(url + "/{id}")
        .then()
                .statusCode(200).extract().response(); // JSONPlaceholder всегда возвращает 200 даже для несуществующих

        Allure.addAttachment("Request URL", "text/plain",
                "DELETE https://jsonplaceholder.typicode.com/posts/1");
        Allure.addAttachment("Response Status", "text/plain",
                "Status Code: " + response.getStatusCode());
        Allure.addAttachment("Response Headers", "text/plain",
                response.getHeaders().toString());
        Allure.addAttachment("Response Body", "application/json",
                response.getBody().asString());
        Allure.addAttachment("Response Time", "text/plain",
                "Response time: " + response.getTime() + "ms");
    }

    @Test
    @DisplayName("Удаление несуществующего поста")
    public void testDeleteNonExistentPost() {
         var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 9999)
        .when()
                .delete(url + "/{id}")
        .then()
                .statusCode(200).extract().response(); // Все равно возвращает 200

        Allure.addAttachment("Request URL", "text/plain",
                "DELETE https://jsonplaceholder.typicode.com/posts/9999");
        Allure.addAttachment("Response Status", "text/plain",
                "Status Code: " + response.getStatusCode());
        Allure.addAttachment("Response Headers", "text/plain",
                response.getHeaders().toString());
        Allure.addAttachment("Response Body", "application/json",
                response.getBody().asString());
        Allure.addAttachment("Response Time", "text/plain",
                "Response time: " + response.getTime() + "ms");
    }
}
