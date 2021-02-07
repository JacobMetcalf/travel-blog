package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.Matchers.containsString;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class NavigationAnchorTemplateTest {

  @Test
  public void can_render_linked_in_link() {
    final NavigationAnchorTemplate unit =
        new NavigationAnchorTemplate(TestData.DIARY_WITH_NAVIGATION.getNavigationAnchors());

    String actualHtml = TestHelper.renderInUl(d -> {
      unit.add(d);
    });
    MatcherAssert.assertThat(actualHtml, containsString("https://forwards.com"));
    MatcherAssert.assertThat(actualHtml, containsString("https://backwards.com"));
  }
}
