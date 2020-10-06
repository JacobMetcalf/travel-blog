package uk.co.jacobmetcalf.travelblog.model;

import java.util.Optional;
import org.immutables.value.Value;

/**
 * Location represents both a &lt;location&gt; element in its own right,
 * but also other elements can specify a location through their attributes.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Location implements ParagraphPart {
  public abstract Optional<String> getCountry();
  public abstract Optional<String> getProvince();
  public abstract Optional<String> getLocation();
  public abstract Optional<Double> getLongitude();
  public abstract Optional<Double> getLatitude();
  public abstract Optional<String> getWiki();

  @Value.Default
  public int getZoom() {
    return 13;
  }

  public boolean hasCoords() {
    return getLongitude().isPresent() && getLatitude().isPresent();
  }

  public void visit(final ParagraphPart.Visitor visitor) {
    visitor.visit(this);
  }
}
