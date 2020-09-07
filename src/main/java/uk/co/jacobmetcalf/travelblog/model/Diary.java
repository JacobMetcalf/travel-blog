package uk.co.jacobmetcalf.travelblog.model;

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
  public abstract String getThumb();
  public abstract String getKml();
  public abstract ImmutableLocation.Builder getLocation();
  public abstract Stream<Entry> getEntries();
}