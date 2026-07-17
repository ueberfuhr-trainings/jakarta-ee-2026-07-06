package de.schulung.spring.todos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

import de.schulung.spring.todos.boundary.WebConfig;
import de.schulung.spring.todos.persistence.PersistenceConfig;

/**
 * Tests der REST-API über die Spring-TestContext-Integration ({@link SpringExtension},
 * ohne Spring Boot) und {@link MockMvc}. Die Tests laufen in-JVM gegen den echten
 * Spring-Context (Controller, Service, DAO, JPA) – es wird KEIN Liberty gestartet.
 *
 * <p>Die Datenbank wird per {@link TestPropertySource} auf eine In-Memory-H2
 * umgestellt (greift in {@code @Value("${todos.db.url:...}")} der
 * {@code PersistenceConfig}), damit die Tests isoliert und ohne Dateileichen
 * laufen.</p>
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { 
	AppConfig.class, 
	WebConfig.class, 
	PersistenceConfig.class 
})
@TestPropertySource(properties = {
	"todos.db.url=jdbc:h2:mem:todos-test;DB_CLOSE_DELAY=-1"		
})
class TodosRestTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
        		.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Anlegen, Auslesen (per id, in Liste), Löschen, danach 404")
    void createReadListDeleteLifecycle() throws Exception {
        String title = "REST-Test Todo";
        String body = "{"
                + "\"title\":\"" + title + "\","
                + "\"description\":\"per MockMvc angelegt\","
                + "\"dueDate\":\"2026-07-20\""
                + "}";

        // 1) Anlegen -> 201, Location-Header, angelegtes Todo mit id im Body
        MvcResult created = mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(title))
                .andReturn();

        int id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        // 2) Auslesbar per id
        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(title));

        // 3) In der Liste enthalten
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());

        // 4) Löschen -> 204
        mockMvc.perform(delete("/api/todos/{id}", id))
                .andExpect(status().isNoContent());

        // 5) Danach per GET nicht mehr gefunden -> 404
        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isNotFound());

        // 6) Erneutes Löschen -> 404
        mockMvc.perform(delete("/api/todos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Ungültiges Todo (zu kurzer Titel) -> 400")
    void invalidCreateIsRejected() throws Exception {
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Ab\"}"))
                .andExpect(status().isBadRequest());
    }

}
