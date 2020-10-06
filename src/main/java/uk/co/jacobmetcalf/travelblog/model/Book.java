package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

@ImmutableStyle
@Value.Immutable
public abstract class Book {
  public abstract String getTitle();
  public abstract String getIsin();
}
