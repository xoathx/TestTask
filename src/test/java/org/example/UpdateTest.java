package org.example;

import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import org.example.Config.ConfigProvider;
import org.example.dto.PostDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UpdateTest {

    static URI url;

    @BeforeAll
    public static void setup() throws URISyntaxException {
        url = new URI(ConfigProvider.URL);
    }

    @Test
    @DisplayName("Обновление целого поста")
    public void updateFullPost(){

        PostDto postDto = new PostDto();
        postDto.id = 1;
        postDto.title = "Update title post";
        postDto.body = "Update body post";
        postDto.userId = 22;

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(postDto)
        .when()
                .put(url + "/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(postDto.id))
                .body("title", equalTo(postDto.title))
                .body("body", equalTo(postDto.body))
                .body("userId", equalTo(postDto.userId)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "UPDATE https://jsonplaceholder.typicode.com/posts/1");
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
    @DisplayName("Обновление части поста")
    public void updatePartialOfPost(){

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body("""
                    {
                        "title": "Обновлен только титул"
                    }
                    """)
        .when()
                .patch(url + "/{id}")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Обновлен только титул"))
                .body("body", not(emptyOrNullString())) // Тело осталось прежним
                .body("userId", notNullValue()).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "UPDATE https://jsonplaceholder.typicode.com/posts/1");
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
    @DisplayName("Обновление несуществующего поста")
    public void updateNonExistenPost(){

        PostDto postDto = new PostDto();
        postDto.id = 9999;
        postDto.userId = 1;
        postDto.title = "Test Title";
        postDto.body = "Test body";

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 9999)
                .body(postDto)
        .when()
                .put(url + "/{id}")
        .then()
                .statusCode(500).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "UPDATE https://jsonplaceholder.typicode.com/posts/9999");
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
    @DisplayName("Изменение с большим количеством символов")
    public void updateWithValidation(){
        String longTitle = "A".repeat(500);
        String longBody = "B".repeat(1000);

        PostDto postDto = new PostDto();
        postDto.id = 1;
        postDto.title = longTitle;
        postDto.body = longBody;
        postDto.userId = 1;

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .body(postDto)
        .when()
                .put(url + "/{id}")
        .then()
                .statusCode(200)
                .body("title", hasLength(500))
                .body("body", hasLength(1000)).extract().response();

        Allure.addAttachment("Request URL", "text/plain",
                "UPDATE https://jsonplaceholder.typicode.com/posts/1");
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
