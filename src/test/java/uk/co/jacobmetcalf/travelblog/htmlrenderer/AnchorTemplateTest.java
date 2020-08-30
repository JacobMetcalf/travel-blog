package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;

public class AnchorTemplateTest {

  @Test
  public void renders_wiki_link() {
    String actualHtml = AnchorTemplate.template.render(TestData.HREF_1);

    assertThat(actualHtml, containsString("href=\"https://www.royensoc.co.uk/identifying-insects\""));
    assertThat(actualHtml, containsString(">Royal Entomological Society<"));
  }
}