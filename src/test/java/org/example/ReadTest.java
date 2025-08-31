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
import static org.hamcrest.Matchers.*;

public class ReadTest {

    private static URI url;

    @BeforeAll
    public static void setup() throws URISyntaxException {
        url = new URI(ConfigProvider.URL);
    }

    @Test
    @DisplayName("Позитивный тест запроса get")
    public void positiveTestRequestGet(){

        Integer postId = 1;
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", postId)
        .when()
                .get(url + "/{id}")
        .then()

                .statusCode(200)
                .body("userId", equalTo(1))
                .body("id", equalTo(postId)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "READ https://jsonplaceholder.typicode.com/posts/1");
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
    @DisplayName("Запрос всех постов")
    public void requestForAllPosts(){

        var response = given()
                .contentType(ContentType.JSON)
        .when()
                .get(url)
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$", everyItem(hasKey("id")))   // Проверяем, что каждый элемент содержит в себе id, title, body, userId
                .body("$", everyItem(hasKey("title")))
                .body("$", everyItem(hasKey("body")))
                .body("$", everyItem(hasKey("userId"))).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "READ https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Проанализировать данные первого элемента всех постов")
    public void postDataAnalysis(){
        var response = given()
                .contentType(ContentType.JSON)
        .when()
                .get(url)
        .then()
                .statusCode(200)
                // Проверяем конкретные значения
                .body("find { it.id == 1 }.title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .body("find { it.id == 1 }.userId", equalTo(1))
                // Проверяем что есть пост с определенным userId
                .body("findAll { it.userId == 1 }.size()", greaterThan(0))
                // Проверяем что title не пустые
                .body("title", everyItem(not(emptyString())))
                .body("body", everyItem(not(emptyString()))).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "READ https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Негативный тест с запросом несуществующего поста")
    public void negativeTest(){
        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 0)
        .when()
                .get(url + "/{id}")
        .then()
                .statusCode(404)
                .body("$", anEmptyMap()).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "READ https://jsonplaceholder.typicode.com/posts/");
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
