package de.schulung.jakartaee.todos.domain;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstraktion des Datenzugriffs auf {@link Todo}s. Das Interface gehört bewusst
 * zur <em>Domänenschicht</em>: Die Domäne legt fest, welche Persistenz-Operationen
 * sie benötigt, und die Persistenzschicht liefert eine Implementierung. Dadurch
 * zeigt die Abhängigkeit von der Persistenz zur Domäne (Dependency Inversion) –
 * nicht umgekehrt.
 *
 * <p>Nur {@link #findAll()} und {@link #save(Todo)} müssen implementiert werden.
 * {@link #count()} und {@link #findByTitleContains(String)} sind als
 * {@code default}-Methoden auf Basis von {@link #findAll()} vorformuliert; eine
 * Implementierung kann sie bei Bedarf (z.B. für effizientere Datenbankabfragen)
 * überschreiben.</p>
 */
public interface TodosDao {

    Collection<Todo> findAll();

    void save(Todo todo);

    /**
     * Löscht das Todo mit der angegebenen {@code id}.
     *
     * @return {@code true}, wenn ein Todo gelöscht wurde, sonst {@code false}
     *         (es gab kein Todo mit dieser {@code id}).
     */
    boolean deleteById(long id);

    /**
     * Liefert das Todo mit der angegebenen {@code id}, sofern vorhanden.
     *
     * @return das gefundene Todo oder {@link Optional#empty()}, wenn es kein
     *         Todo mit dieser {@code id} gibt.
     */
    default Optional<Todo> findById(long id) {
        return findAll()
                .stream()
                .filter(todo -> todo.getId() != null && todo.getId() == id)
                .findFirst();
    }

    default long count() {
        return findAll().size();
    }

    default Collection<Todo> findByTitleContains(String search) {
        String needle = search.toLowerCase(Locale.ROOT);
        return findAll()
        		.stream()
                .filter(todo -> todo.getTitle() != null
                        && todo.getTitle().toLowerCase(Locale.ROOT).contains(needle))
                .collect(Collectors.toList());
    }

}
