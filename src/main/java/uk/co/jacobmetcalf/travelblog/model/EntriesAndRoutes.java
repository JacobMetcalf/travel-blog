package uk.co.jacobmetcalf.travelblog.model;

import com.google.common.base.Preconditions;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Container for a stream of diary entries and map routes.
 * Captures the start date on the way through.
 */
public class EntriesAndRoutes {
  private final Stream<EntryOrRoute> innerStream;
  private boolean accessedStream;
  private LocalDate startDate;

  public EntriesAndRoutes(final Stream<EntryOrRoute> entriesAndRoutes) {
    this.innerStream = entriesAndRoutes;
    this.accessedStream = false;
  }

  public Stream<EntryOrRoute> stream() {
    accessedStream = true;
    return innerStream.peek(i -> {
      if (startDate == null) {
        i.getEntry().ifPresent(e -> startDate = e.getDate());
      }
    });
  }

  /**
   * Returns the date of the first entry if there was one.
   * Note that the stream must be consumed before calling this function.
   */
  public Optional<LocalDate> getStartDate() {
    Preconditions.checkState(accessedStream,
        "Stream must be consumed before you access start date");
    return Optional.ofNullable(startDate);
  }
}
