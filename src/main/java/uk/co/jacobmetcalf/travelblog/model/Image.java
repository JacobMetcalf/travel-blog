package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public interface Image extends Locatable {

  enum Position {LEFT, RIGHT, NONE}

  String getSrc();
  Position getPosition();
  String getTitle();

  // Extend builder for common attributes
  interface Builder extends Locatable.Builder {}
}
