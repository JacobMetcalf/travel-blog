package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

/**
 * Location represents both a &lt;location&gt; element in its own right,
 * but also other elements can specify a location through their attributes.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Location implements Locatable, ParagraphPart {

  interface Builder extends Locatable.Builder {}

  @Override
  public void visit(final ParagraphPart.Visitor visitor) {
    visitor.visit(this);
  }
}
