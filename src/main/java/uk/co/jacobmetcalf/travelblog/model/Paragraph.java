package uk.co.jacobmetcalf.travelblog.model;

import java.util.List;
import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public abstract class Paragraph {
  public abstract List<ParagraphPart> getParts();
}
