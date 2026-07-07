package de.schulung.jakartaee.todos;

/**
 * Der Bearbeitungsstand eines {@link Todo}.
 */
public enum Status {

    ERSTELLT("erstellt"),
    IN_ARBEIT("in Arbeit"),
    FERTIG("fertig");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
