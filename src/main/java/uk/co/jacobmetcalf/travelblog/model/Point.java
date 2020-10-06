package uk.co.jacobmetcalf.travelblog.model;

import org.immutables.value.Value;

/**
 * Point represents a long, lat coordinate in the world.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Point {
  public abstract Double getLongitude();
  public abstract Double getLatitude();
}
