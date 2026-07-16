package de.schulung.jakartaee.todos.boundary.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Registriert die JAX-RS-Anwendung und legt den Basispfad der REST-API auf
 * {@code /api} fest. Alle Ressourcen liegen damit unter {@code .../todos-app/api/...}.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
}
