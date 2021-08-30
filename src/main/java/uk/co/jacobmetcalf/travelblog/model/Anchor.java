package uk.co.jacobmetcalf.travelblog.model;

import java.util.Optional;
import org.immutables.value.Value;

/**
 * Within a paragraph a reference to another website, typically a wikipedia article
 */
@ImmutableStyle
@Value.Immutable
public abstract class Anchor implements ParagraphPart {
  public abstract String getRef();
  public abstract String getText();
  public abstract Optional<String> getIcon();

  @Override
  public void visit(final ParagraphPart.Visitor visitor) {
    visitor.visit(this);
  }
}
