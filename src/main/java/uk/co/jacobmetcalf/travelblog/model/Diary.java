package uk.co.jacobmetcalf.travelblog.model;

import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

/**
 * The overall diary. Has a title, a partially populated root location and
 * a stream of diary entries.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Diary implements Locatable {
  public abstract String getTitle();
  public abstract String getCanonicalUrl();
  public abstract Optional<String> getSummary();
  public abstract Optional<String> getThumb();

  public abstract List<Anchor> getNavigationAnchors();
  public abstract List<Book> getBooks();
  public abstract EntriesAndRoutes getEntriesAndRoutes();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
