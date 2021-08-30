package uk.co.jacobmetcalf.travelblog.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocationTest {
  @Test
  public void defaults_location_from_country() {
    Location location = ImmutableLocation.builder()
        .country("a_country")
        .build();

    assertEquals("a_country", location.getLocation());
  }

  @Test
  public void defaults_location_from_province() {
    Location location = ImmutableLocation.builder()
        .province("a_province")
        .build();

    assertEquals("a_province", location.getLocation());
  }

  @Test
  public void throws_if_no_location_and_cannot_default() {
   Assertions.assertThrows(IllegalArgumentException.class,
       () -> ImmutableLocation.builder().build());
  }
}
