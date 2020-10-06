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
public abstract class Diary {
  public abstract String getTitle();
  public abstract String getFilename();
  public abstract String getThumb();
  public abstract Optional<String> getKml();
  public abstract Location getLocation();
  public abstract List<Book> getBooks();
  public abstract Stream<EntryOrRoute> getEntriesAndRoutes();
}
