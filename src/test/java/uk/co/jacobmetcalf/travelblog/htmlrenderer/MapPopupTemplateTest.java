package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiarySummary;
import uk.co.jacobmetcalf.travelblog.model.LocatableWithSummary;

public class MapPopupTemplateTest {
  @Test
  public void renders_reference_as_html() {
    LocatableWithSummary locatableWithSummary = ImmutableDiarySummary.builder()
        .country("italy")
        .canonicalUrl("https://mysite.com/italy")
        .title("Short stroll around Liguria")
        .summary("A three week hike through the hills and villages of Liguria.")
        .thumb("italy_thumb.jpg")
        .build();

    String actual = new MapPopupTemplate(locatableWithSummary).render();

    // Assert it does link to the article
    assertThat(actual, Matchers.containsString("href=\"https://mysite.com/italy\""));
  }
}
