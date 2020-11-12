package uk.co.jacobmetcalf.travelblog.xmlparser;

import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class BuilderWithLocation<B> {

  private final B outerBuilder;
  private final ImmutableLocation.Builder locationBuilder;

  public BuilderWithLocation(B outerBuilder, Location parentLocation) {
    this.outerBuilder = outerBuilder;
    this.locationBuilder = ImmutableLocation.builder().from(parentLocation);
  }

  public BuilderWithLocation(B outerBuilder) {
    this.outerBuilder = outerBuilder;
    this.locationBuilder = ImmutableLocation.builder();
  }

  public B outer() {
    return outerBuilder;
  }

  public ImmutableLocation.Builder inner() {
    return locationBuilder;
  }
}
