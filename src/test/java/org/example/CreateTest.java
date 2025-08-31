package org.example;

import io.qameta.allure.Allure;
import org.example.Config.ConfigProvider;
import org.example.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import io.restassured.http.ContentType;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

class CreateTest {

    static URI url;

    @BeforeAll
    public static void setup() throws URISyntaxException {
        url = new URI(ConfigProvider.URL);
    }

    @Test
    @DisplayName("Позитивный тест добавления поста")
    public void positiveAddPost(){

        PostDto postDto = new PostDto();
        postDto.title = "Это новый пост!";
        postDto.body = "Новый пост для теста";
        postDto.userId = 2;

        var response = given()
                .contentType(ContentType.JSON)
                .body(postDto)
        .when()
                .post(url)
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo(postDto.title))
                .body("body", equalTo(postDto.body))
                .body("userId", equalTo(2)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Запрос без userId")
    public void requestWithoutUserId(){
        PostDto postDto = new PostDto();
        postDto.title = "Это новый пост!";
        postDto.body = "Новый пост для теста";

        var response = given()
                .contentType(ContentType.JSON)
                .body(postDto)
        .when()
                .post(url)
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo(postDto.title))
                .body("body", equalTo(postDto.body)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Запрос без body")
    public void requestWithoutBody(){
        PostDto postDto = new PostDto();
        postDto.title = "Это новый пост!";
        postDto.userId = 2;

        var response = given()
                .contentType(ContentType.JSON)
                .body(postDto)
        .when()
                .post(url)
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("title", equalTo(postDto.title))
                .body("userId", equalTo(2)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Запрос без title")
    public void requestWithoutTitle(){

        PostDto postDto = new PostDto();
        postDto.body = "Новый пост для теста";
        postDto.userId = 2;

        var response = given()
                .contentType(ContentType.JSON)
                .body(postDto)
        .when()
                .post(url)
        .then()
                .statusCode(201)
                .body("id", equalTo(101))
                .body("body", equalTo(postDto.body))
                .body("userId", equalTo(2)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Пустой запрос")
    public void emptyRequest(){
        var response = given()
                .contentType(ContentType.JSON)
        .when()
                .post(url)
        .then()
                .statusCode(201)
                .body("id", equalTo(101)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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
    @DisplayName("Добавление с большим количеством символов")
    public void createWithValidation(){
        String longTitle = "A".repeat(500);
        String longBody = "B".repeat(1000);

        var response = given()
                .contentType(ContentType.JSON)
                .body("""
            {
                "id": 1,
                "title": "%s",
                "body": "%s",
                "userId": 1
            }
            """.formatted(longTitle, longBody))
                .when()
                .post(url)
                .then()
                .statusCode(201)
                .body("title", hasLength(500))
                .body("body", hasLength(1000)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "CREATE https://jsonplaceholder.typicode.com/posts/");
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

