/**
 * Persistenzschicht der Anwendung.
 *
 * <p>Enthält die technische Umsetzung des Datenzugriffs. {@link
 * de.schulung.jakartaee.todos.persistence.JpaTodosDao} implementiert das in der
 * Domäne definierte Interface {@link
 * de.schulung.jakartaee.todos.domain.TodosDao} mit JPA.</p>
 *
 * <p>Die Abhängigkeitsrichtung ist damit invertiert: Die Persistenz hängt von
 * der Domäne ab (sie implementiert deren Interface), nicht umgekehrt. Die Domäne
 * kennt die Persistenztechnologie (JPA/{@code EntityManager}) nicht mehr.</p>
 */
package de.schulung.jakartaee.todos.persistence;
