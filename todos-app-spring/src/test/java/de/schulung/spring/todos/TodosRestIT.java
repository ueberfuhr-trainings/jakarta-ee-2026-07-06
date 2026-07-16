package de.schulung.spring.todos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Integrationstests gegen die REST-API der Spring-Variante
 * ({@code /todos-app-spring/api/todos}). Der Liberty wird von Maven
 * gestartet/gestoppt.
 */
class TodosRestIT {

    @BeforeAll
    static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("liberty.http.port", 9090);
        RestAssured.basePath = "/todos-app-spring/api";
    }

    @Test
    @DisplayName("Anlegen, Auslesen (per id, per Location, in Liste), Löschen, danach 404")
    void createReadListDeleteLifecycle() {
        String title = "REST-IT Todo";
        String body = "{"
                + "\"title\":\"" + title + "\","
                + "\"description\":\"per Integrationstest angelegt\","
                + "\"dueDate\":\"2026-07-20\""
                + "}";

        Response created = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/todos")
                .then()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("title", equalTo(title))
                .extract()
                .response();

        int id = created.jsonPath().getInt("id");
        String location = created.header("Location");

        given()
                .when()
                .get("/todos/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("title", equalTo(title));

        given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body("id", equalTo(id));

        given()
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItem(id));

        given()
                .when()
                .delete("/todos/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/todos/" + id)
                .then()
                .statusCode(404);

        given()
                .when()
                .delete("/todos/" + id)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Ungültiges Todo (zu kurzer Titel) -> 400")
    void invalidCreateIsRejected() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"title\":\"Ab\"}")
                .when()
                .post("/todos")
                .then()
                .statusCode(400);
    }

}
