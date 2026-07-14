/**
 * Persistenzschicht der Anwendung.
 *
 * <p><strong>Offenes Problem (bewusst!):</strong> Dieses Package ist nach der
 * ersten Umstrukturierung noch <em>leer</em>. Der Datenbankzugriff steckt
 * aktuell direkt im {@link de.schulung.jakartaee.todos.domain.TodosService} der
 * Domänenschicht, der dafür den JPA-{@code EntityManager} verwendet. Damit hängt
 * die Domäne unmittelbar von der Persistenztechnologie ab – die
 * Abhängigkeitsrichtung zeigt „nach außen" statt zur Domäne hin.</p>
 *
 * <p>Dieses Problem wird im nächsten Schritt (Dependency Inversion) gelöst: Die
 * Domäne definiert ein Interface {@code TodosDao}, dessen Implementierung in
 * dieses Package wandert. Dann hängt die Persistenz von der Domäne ab – und
 * nicht umgekehrt.</p>
 */
package de.schulung.jakartaee.todos.persistence;
