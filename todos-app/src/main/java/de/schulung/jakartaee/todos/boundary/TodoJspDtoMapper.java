package de.schulung.jakartaee.todos.boundary;

import org.mapstruct.Mapper;

import de.schulung.jakartaee.todos.domain.Todo;
import de.schulung.jakartaee.todos.domain.TodoStatus;

/**
 * Wandelt das Domänenmodell {@link Todo} in das Anzeige-DTO {@link TodoJspDto}
 * um. Nur die Anzeige-Richtung wird gebraucht. Die Implementierung erzeugt
 * MapStruct; {@code componentModel = "cdi"} macht daraus eine CDI-Bean.
 */
@Mapper(componentModel = "cdi")
public interface TodoJspDtoMapper {

    TodoJspDto toJspDto(Todo todo);

    /**
     * Bildet den Status auf eine Anzeige-Bezeichnung ab. MapStruct verwendet
     * diese Methode automatisch für das Feld {@code status} (TodoStatus →
     * String) – bewusst per {@code switch}, nicht über {@link Enum#name()}.
     */
    default String mapStatus(TodoStatus status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case ERSTELLT:
                return "Erstellt";
            case IN_ARBEIT:
                return "In Arbeit";
            case FERTIG:
                return "Fertig";
            default:
                throw new IllegalArgumentException("Unbekannter Status: " + status);
        }
    }

}
