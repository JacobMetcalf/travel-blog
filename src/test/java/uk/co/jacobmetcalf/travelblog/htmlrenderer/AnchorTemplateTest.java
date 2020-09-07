package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import htmlflow.DynamicHtml;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

public class AnchorTemplateTest {

  @Test
  public void renders_wiki_link() {
    String actualHtml = DynamicHtml.view((view, a) ->
        view.div().p().of(p -> AnchorTemplate.add(p, (Anchor)a)))
        .setIndented(false).render(TestData.HREF_1);

    assertThat(actualHtml, containsString("href=\"https://www.royensoc.co.uk/identifying-insects\""));
    assertThat(actualHtml, containsString(">Royal Entomological Society<"));
  }
}