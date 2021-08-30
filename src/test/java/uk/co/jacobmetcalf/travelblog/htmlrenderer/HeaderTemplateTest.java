package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;

public class HeaderTemplateTest {



  @Test
  public void adds_header_without_nav_links() {

    // Just check dynamic elements.
    String actualHtml = TestHelper.renderInDiv(d -> {
      new NavbarTemplate("Test title").add(d);
    });
    MatcherAssert.assertThat(actualHtml, Matchers.containsString("Test title"));
  }

  @Test
  public void adds_header_with_nav_link() {
    List<Anchor> anchors = ImmutableList.of(
        ImmutableAnchor.builder().icon("globe").text("Test nav link").ref("../test.kml").build());

    // Just check dynamic elements.
    String actualHtml = TestHelper.renderInDiv(d -> {
      new NavbarTemplate("Test title", anchors).add(d);
    });
    MatcherAssert.assertThat(actualHtml, Matchers.containsString("test.kml"));
  }
}
