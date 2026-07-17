package de.schulung.spring.todos.boundary.web;

import org.springframework.web.bind.annotation.GetMapping;

public class IndexController {

    /** Startseite: statische index.html an den Default-Servlet weiterreichen. */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

}
