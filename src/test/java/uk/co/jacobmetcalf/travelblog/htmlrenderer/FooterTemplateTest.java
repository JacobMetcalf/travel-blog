package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.Matchers.containsString;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class FooterTemplateTest {

  @Test
  public void can_render_linked_in_link() {
    final FooterTemplate unit = new FooterTemplate("my-linked-in");
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });

    MatcherAssert.assertThat(actualHtml, containsString("my-linked-in"));
  }

  @Test
  public void does_not_show_link_if_no_id() {
    final FooterTemplate unit = new FooterTemplate(null);
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });

    MatcherAssert.assertThat(actualHtml, Matchers.not(containsString("my-linked-in")));
  }
}
