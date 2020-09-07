package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class LocationTemplateTest {
  @Test
  public void can_render_location_without_wiki() {
    // Just check dynamic elements
    HtmlView<Location> template = DynamicHtml.view((view, location) ->
        view.div().p().of(p -> LocationTemplate.addLocation(p, location, false)));

    String actualHtml = template.render(TestData.LOCATION_1);

    // Has link target for Quito
    assertThat(actualHtml, containsString("<a id=\"Quito\">"));

    // Links to Quito on the map
    assertThat(actualHtml, containsString("onclick=\"map.setCenter({lat:-0.2181,lng:-78.5084});map.setZoom(13);\""));
  }

  @Test
  public void can_render_location_with_wiki() {
    Location location = ImmutableLocation.builder()
        .location("Quito")
        .province("Pichincha")
        .country("Ecuador")
        .wiki("https://en.wikipedia.org/wiki/Quito")
        .latitude(-0.2181)
        .longitude(-78.5084)
        .build();

    HtmlView<Location> template = DynamicHtml.view((view, l) ->
        view.div().p().of(p -> LocationTemplate.addLocation(p, l, false)));

    String actualHtml = template.render(location);

    // Has link target for Quito
    assertThat(actualHtml, containsString("<a id=\"Quito\">"));

    // Links to Quito on the map
    assertThat(actualHtml, containsString("onclick=\"map.setCenter({lat:-0.2181,lng:-78.5084});map.setZoom(13);\""));

    // Links to wiki article
    assertThat(actualHtml, containsString("href=\"https://en.wikipedia.org/wiki/Quito\""));
  }
}