package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiary;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class HeaderTemplateTest {



  @Test
  public void adds_header_without_nav_links() {

    // Just check dynamic elements.
    String actualHtml = TestHelper.renderInDiv(d -> {
      new NavbarTemplate(TestData.DIARY_NO_ENTRIES).add(d);
    });
    MatcherAssert.assertThat(actualHtml, Matchers.containsString("Test title"));
  }

  @Test
  public void adds_header_with_nav_link() {
    final Diary diary = ImmutableDiary.builder().from(TestData.DIARY_NO_ENTRIES)
        .addNavigationAnchors(
            ImmutableAnchor.builder()
                .icon("globe").text("Test nav link").ref("../test.kml").build())
        .build();

    // Just check dynamic elements.
    String actualHtml = TestHelper.renderInDiv(d -> {
      new NavbarTemplate(diary).add(d);
    });
    MatcherAssert.assertThat(actualHtml, Matchers.containsString("test.kml"));
  }
}
