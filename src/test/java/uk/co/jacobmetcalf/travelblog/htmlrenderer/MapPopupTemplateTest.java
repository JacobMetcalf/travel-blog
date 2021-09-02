package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableIndexEntry;
import uk.co.jacobmetcalf.travelblog.model.IndexEntry;

public class MapPopupTemplateTest {
  @Test
  public void renders_reference_as_html() {
    IndexEntry indexEntry = ImmutableIndexEntry.builder()
        .country("italy")
        .relativeUrl("italy/diary.html")
        .title("Short stroll around Liguria")
        .summary("A three week hike through the hills and villages of Liguria.")
        .relativeThumbUrl("italy_thumb.jpg")
        .build();

    String actual = new MapPopupTemplate(indexEntry).render();

    // Assert it does link to the article
    assertThat(actual, Matchers.containsString("href=\"italy/diary.html\""));
  }
}
