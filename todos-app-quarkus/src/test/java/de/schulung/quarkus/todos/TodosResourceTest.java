package de.schulung.quarkus.todos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Tests der REST-API mit {@link QuarkusTest}: Quarkus startet die Anwendung
 * (mit In-Memory-H2 aus dem Test-Profil) und konfiguriert REST-Assured
 * automatisch auf den Test-Port.
 */
@QuarkusTest
class TodosResourceTest {

    @Test
    @DisplayName("Anlegen, Auslesen (per id, per Location, in Liste), Löschen, danach 404")
    void createReadListDeleteLifecycle() {
        String title = "REST-Test Todo";
        String body = "{"
                + "\"title\":\"" + title + "\","
                + "\"description\":\"per QuarkusTest angelegt\","
                + "\"dueDate\":\"2026-07-20\""
                + "}";

        Response created = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/todos")
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
                .get("/api/todos/" + id)
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
                .get("/api/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", hasItem(id));

        given()
                .when()
                .delete("/api/todos/" + id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/api/todos/" + id)
                .then()
                .statusCode(404);

        given()
                .when()
                .delete("/api/todos/" + id)
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
                .post("/api/todos")
                .then()
                .statusCode(400);
    }

}
