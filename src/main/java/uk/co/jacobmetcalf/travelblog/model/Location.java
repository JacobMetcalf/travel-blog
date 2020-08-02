package uk.co.jacobmetcalf.travelblog.model;

import java.util.Optional;
import java.util.OptionalInt;
import org.immutables.value.Value;

/**
 * Location represents both a &lt;location&gt; element in its own right,
 * but also other elements cam specify a location through their attributes.
 */
@ImmutableStyle
@Value.Immutable
public abstract class Location implements ParagraphPart {
  public abstract String getCountry();
  public abstract String getProvince();
  public abstract String getLocation();
  public abstract Double getLongitude();
  public abstract Double getLatitude();
  public abstract OptionalInt getZoom();
  public abstract Optional<String> getWiki();
}
