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
public interface Diary extends Locatable {
  String getTitle();
  String getFilename();
  String getThumb();
  Optional<String> getKml();
  List<Book> getBooks();
  Stream<EntryOrRoute> getEntriesAndRoutes();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
