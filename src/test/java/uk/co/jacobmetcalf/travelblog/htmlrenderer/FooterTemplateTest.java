package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.Matchers.containsString;

import com.google.common.collect.ImmutableList;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableProperties;
import uk.co.jacobmetcalf.travelblog.model.Properties;

public class FooterTemplateTest {

  @Test
  public void can_render_linked_in_link() {
    final FooterTemplate unit = new FooterTemplate(ImmutableList.of(
        ImmutableAnchor.builder().icon("backward").text("Prev").ref("https://forwards.com").build(),
        ImmutableAnchor.builder().icon("forward").text("Next").ref("https://backwards.com").build()),
        Properties.of(Properties.Key.LINKED_IN, "my-linked-in"));
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });
    MatcherAssert.assertThat(actualHtml, containsString("https://forwards.com"));
    MatcherAssert.assertThat(actualHtml, containsString("https://backwards.com"));
    MatcherAssert.assertThat(actualHtml, containsString("my-linked-in"));
  }

  @Test
  public void does_not_show_link_if_no_id() {
    final FooterTemplate unit = new FooterTemplate(ImmutableProperties.builder().build());
    String actualHtml = TestHelper.renderInBody(d -> {
      unit.add(d);
    });

    MatcherAssert.assertThat(actualHtml, Matchers.not(containsString("my-linked-in")));
  }
}
