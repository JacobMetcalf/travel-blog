package uk.co.jacobmetcalf.travelblog.model;

import java.time.LocalDate;
import java.util.List;
import org.immutables.value.Value;

/**
 * An entry in a diary, consists of a number of paragraphs
 */
@ImmutableStyle
@Value.Immutable
public abstract class Entry {
  public abstract LocalDate getDate();
  public abstract Location getLocation();
  public abstract List<Paragraph> getParagraphs();
}
