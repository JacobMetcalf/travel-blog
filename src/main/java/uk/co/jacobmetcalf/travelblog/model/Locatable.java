package uk.co.jacobmetcalf.travelblog.model;

import java.util.Optional;
import org.immutables.value.Value;

/**
 * Interface provides a set of common attributes and a builder for
 * all elements which can be located within the world.
 */
public interface Locatable {
  Optional<String> getCountry();
  Optional<String> getProvince();
  Optional<String> getLocation();
  Optional<Double> getLongitude();
  Optional<Double> getLatitude();
  @Value.Default
  default int getZoom() {
    return 13;
  }
  Optional<String> getWiki();

  default boolean hasCoords() {
    return getLongitude().isPresent() && getLatitude().isPresent();
  }

  interface Builder {
    Builder country(String country);
    Builder province(String province);
    Builder location(String location);
    Builder longitude(double longitude);
    Builder latitude(double latitude);
    Builder zoom(int zoom);
    Builder wiki(String wiki);
  }
}
