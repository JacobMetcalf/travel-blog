package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class LocationParserTest {

  public final LocationParser unit = new LocationParser();

  @Test
  public void can_parse_valid_location_element() {
    String inputXml = "<location latitude=\"-1.6733\" longitude=\"-78.6517\""
        + " wiki=\"Riobamba\" zoom=\"5\">Riobamba</location>";

    // We expect the location, lat and long attributes of the parent to be overridden
    Location actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);
    assertThat(actual.getCountry().orElseThrow(), equalTo("Ecuador"));
    assertThat(actual.getProvince().orElseThrow(), equalTo("Pichincha"));
    assertThat(actual.getLocation(), equalTo("Riobamba"));
    assertThat(actual.getLatitude().orElseThrow(), closeTo(-1.6733, 0.00001));
    assertThat(actual.getLongitude().orElseThrow(), closeTo(-78.6517, 0.00001));
    assertThat(actual.getZoom(), Matchers.equalTo(5));
    assertThat(actual.getWiki().orElseThrow(), equalTo("https://en.wikipedia.org/wiki/Riobamba"));
  }

  @Test
  public void defaults_zoom() {
    String inputXml = "<location latitude=\"-1.6733\" longitude=\"-78.6517\""
        + " wiki=\"Riobamba\">Riobamba</location>";

    Location actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);
    assertThat(actual.getZoom(), Matchers.equalTo(13));
  }
}
