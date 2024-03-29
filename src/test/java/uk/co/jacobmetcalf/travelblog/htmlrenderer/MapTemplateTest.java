package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.executor.Properties;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class MapTemplateTest {

  private final MapTemplate unit = new MapTemplate(TestData.QUITO,
      Properties.of(Properties.Key.GOOGLE_API_KEY, "my-api-key"));

  @Test
  public void can_render_route() {
    String actualHtml = TestHelper.renderInDiv(d -> {
      unit.addRoute(d, TestData.ROUTE_1);
    });

    // Check path plotting
    assertThat(actualHtml, containsString("path:[{lat:10.0,lng:-40.0},{lat:12.0,lng:-42.0}]"));
  }

  @Test
  public void can_render_location() {
    String actualHtml = TestHelper.renderInScript(d -> {
      MapTemplate.addLocation(d, TestData.QUITO, null);
    });

    // Check location plotting
    assertThat(actualHtml, containsString("locations[\"Quito\"] = {lat:-0.2181,lng:-78.5084}"));
  }

  @Test
  public void ignores_no_coord_location() {
    String actualHtml = TestHelper.renderInScript(d -> {
      MapTemplate.addLocation(d, TestData.ECUADOR, null);
    });

    // Check does not cause null pointer exception
    assertThat(actualHtml, not(containsString("locations")));
  }

  @Test
  public void renders_footer_script() {
    String actualHtml = TestHelper.renderInDiv(d -> {
      unit.addFooterScript(d);
    });

    // Check does not cause null pointer exception
    assertThat(actualHtml, containsString("my-api-key"));
  }
}
