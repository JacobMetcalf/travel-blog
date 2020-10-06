package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Div;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class MapTemplateTest {

  private final MapTemplate unit = new MapTemplate(TestData.QUITO, "my-api-key");

  @Test
  public void can_render_route() {
    String actualHtml = TestHelper.<Body>renderInDiv(d -> unit.addRoute(d, TestData.ROUTE_1));

    // Check path plotting
    assertThat(actualHtml, containsString("path:[{lat:10.0,lng:-40.0},{lat:12.0,lng:-42.0}]"));
  }

  @Test
  public void can_render_location() {
    String actualHtml = TestHelper.<Div>renderInScript(d -> MapTemplate.addLocation(d, TestData.QUITO));

    // Check location plotting
    assertThat(actualHtml, containsString("locations[\"Quito\"] = {lat:-0.2181,lng:-78.5084}"));
  }

  @Test
  public void ignores_no_coord_location() {
    String actualHtml = TestHelper.<Div>renderInScript(d -> MapTemplate.addLocation(d, TestData.ECUADOR));

    // Check does not cause null pointer exception
    assertThat(actualHtml, not(containsString("locations")));
  }

  @Test
  public void renders_footer_script() {
    String actualHtml = TestHelper.<Body>renderInDiv(unit::addFooterScript);

    // Check does not cause null pointer exception
    assertThat(actualHtml, containsString("my-api-key"));
  }
}
