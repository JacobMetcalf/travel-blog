package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.Matchers.containsString;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Properties;
import uk.co.jacobmetcalf.travelblog.model.Properties.Key;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class FooterTemplateTest {

  @Test
  public void can_render_linked_in_link() {
    final FooterTemplate unit = new FooterTemplate(TestData.DIARY_WITH_NAVIGATION,
        Properties.of(Key.LINKED_IN, "my-linked-in"));
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });
    MatcherAssert.assertThat(actualHtml, containsString("https://forwards.com"));
    MatcherAssert.assertThat(actualHtml, containsString("https://backwards.com"));
    MatcherAssert.assertThat(actualHtml, containsString("my-linked-in"));
  }

  @Test
  public void does_not_show_link_if_no_id() {
    final FooterTemplate unit = new FooterTemplate(TestData.DIARY_WITH_NAVIGATION,
        ImmutableProperties.builder().build());
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });

    MatcherAssert.assertThat(actualHtml, Matchers.not(containsString("my-linked-in")));
  }
}
