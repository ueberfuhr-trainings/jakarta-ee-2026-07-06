package de.schulung.jakartaee.todos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Integrationstests gegen die HTTP-Schnittstelle der Servlets. Der Liberty wird
 * von Maven (Phasen {@code pre-integration-test}/{@code post-integration-test})
 * gestartet und gestoppt; die Datenbank läuft dabei per configDropins-Override
 * In-Memory.
 */
class TodosServletIT {

    @BeforeAll
    static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = Integer.getInteger("liberty.http.port", 9080);
        RestAssured.basePath = "/todos-app";
    }

    // ----- Positivfall -----------------------------------------------------

    @Test
    @DisplayName("Angelegtes Todo taucht in der HTML-Liste auf")
    void createdTodoAppearsInTodoList() {
        String title = "Integrationstest-Aufgabe";

        // Anlegen: Hauptsache kein Client-/Server-Fehler (kein 4xx/5xx).
        // Der Servlet leitet nach Erfolg per Redirect auf /todos um.
        given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("title", title)
                .formParam("description", "Von einem Integrationstest erstellt")
                .when()
                .post("/add-todo")
                .then()
                .statusCode(lessThan(400));

        // Abruf: das gerenderte HTML muss den Titel enthalten.
        given()
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .contentType(containsString("text/html"))
                .body(containsString(title));
    }

    // ----- Fehlerfälle: 400 Bad Request ------------------------------------

    @ParameterizedTest(name = "Titel=\"{0}\", dueDate=\"{1}\" -> 400")
    @MethodSource("invalidTodos")
    @DisplayName("Ungültige Eingaben werden mit 400 abgelehnt")
    void invalidTodoIsRejected(String title, String dueDate) {
        RequestSpecification request = given()
                .redirects().follow(false)
                .contentType(ContentType.URLENC)
                .formParam("title", title)
                .formParam("description", "Testbeschreibung");
        if (dueDate != null) {
            request.formParam("dueDate", dueDate);
        }
        request.when()
                .post("/add-todo")
                .then()
                .statusCode(400);
    }

    static Stream<Arguments> invalidTodos() {
        return Stream.of(
                arguments("", null),                       // leerer Titel
                arguments("Ab", null),                     // zu kurz (< 3 Zeichen)
                arguments("kleingeschrieben", null),       // kein Großbuchstabe am Anfang
                arguments("Gültiger Titel", "kein-datum"), // ungültiges Datumsformat
                arguments("Vergangenheitstest", LocalDate.now().minusDays(1).toString()), // Datum in der Vergangenheit
                arguments("Zukunftstest", LocalDate.now().plusMonths(4).toString())        // > 3 Monate in der Zukunft
        );
    }

}
