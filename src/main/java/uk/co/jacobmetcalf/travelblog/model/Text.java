package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

/**
 * Block of text within a paragraph.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Text implements ParagraphPart {
  public abstract String getText();

  @Override
  public void visit(final ParagraphPart.Visitor visitor) {
    visitor.visit(this);
  }
}
