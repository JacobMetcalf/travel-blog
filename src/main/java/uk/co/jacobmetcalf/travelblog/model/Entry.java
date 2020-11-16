package uk.co.jacobmetcalf.travelblog.model;

import java.time.LocalDate;
import java.util.List;
import org.immutables.value.Value;

/**
 * An entry in a diary, consists of a number of paragraphs
 */
@ImmutableStyle
@Value.Immutable
public interface Entry extends Locatable {
  LocalDate getDate();
  List<Paragraph> getParagraphs();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
