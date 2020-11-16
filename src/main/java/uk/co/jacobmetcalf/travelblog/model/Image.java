package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public abstract class Image implements Locatable {

  public enum Position {LEFT, RIGHT, NONE}

  public abstract String getSrc();
  public abstract Position getPosition();
  public abstract String getTitle();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
