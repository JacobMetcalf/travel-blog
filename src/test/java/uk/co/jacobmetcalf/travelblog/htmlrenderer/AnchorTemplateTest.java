package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class AnchorTemplateTest {

  @Test
  public void renders_wiki_link() {
    String actualHtml = TestHelper.renderInP(
        (d -> new AnchorTemplate().add(d, TestData.HREF_1)));

    assertThat(actualHtml, containsString("href=\"https://www.royensoc.co.uk/identifying-insects\""));
    assertThat(actualHtml, containsString(">Royal Entomological Society<"));
  }
}
