package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public abstract class Image implements ParagraphPart {

  public enum Position {LEFT, CENTRE, RIGHT};

  public abstract String getSrc();
  public abstract Position getPosition();
  public abstract Location getLocation();
  public abstract String getTitle();
}
