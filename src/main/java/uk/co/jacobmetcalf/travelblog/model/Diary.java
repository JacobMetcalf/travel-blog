package uk.co.jacobmetcalf.travelblog.model;

import java.util.List;
import java.util.stream.Stream;
import org.immutables.value.Value;

/**
 * The overall diary. Has a title, a partially populated root location and
 * a stream of diary entries.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Diary implements LocatableWithSummary {
  public abstract List<Anchor> getNavigationAnchors();
  public abstract List<Book> getBooks();
  public abstract Stream<EntryOrRoute> getEntriesAndRoutes();

  // Extend builder for common attributes
  interface Builder extends LocatableWithSummary.Builder {}
}
