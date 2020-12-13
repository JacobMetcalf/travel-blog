package uk.co.jacobmetcalf.travelblog.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.immutables.value.Value;

/**
 * The overall diary. Has a title, a partially populated root location and
 * a stream of diary entries.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Diary implements Locatable {
  public abstract String getTitle();
  public abstract Optional<String> getSummary();
  public abstract String getFilename();
  public abstract String getThumb();
  public abstract List<Anchor> getNavigationAnchors();
  public abstract Optional<String> getKml();
  public abstract List<Book> getBooks();
  public abstract Stream<EntryOrRoute> getEntriesAndRoutes();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
