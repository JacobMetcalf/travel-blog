package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class LocationParserTest {

  public final LocationParser unit = new LocationParser();

  //TODO: Test optionality of zoom

  @Test
  public void can_parse_valid_location_element() {
    String inputXml = "<location latitude=\"-1.6733\" longitude=\"-78.6517\" "
        + "wiki=\"Riobamba\">Riobamba</location>";

    // We expect the location, lat and long attributes of the parent to be overridden
    Location actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());
    assertThat(actual.getCountry(), equalTo("Ecuador"));
    assertThat(actual.getProvince(), equalTo("Pichincha"));
    assertThat(actual.getLocation(), equalTo("Riobamba"));
    assertThat(actual.getLatitude(), closeTo(-1.6733, 0.00001));
    assertThat(actual.getLongitude(), closeTo(-78.6517, 0.00001));
    assertThat(actual.getWiki().orElseThrow(), equalTo("Riobamba"));
  }
}