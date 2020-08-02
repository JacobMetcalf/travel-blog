package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

/**
 * Within a paragraph a reference to a wikipedia article.
 */
@ImmutableStyle
@Value.Immutable
public abstract class WikiRef implements ParagraphPart {
  public abstract String getRef();
  public abstract String getText();
}
