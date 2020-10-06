package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static uk.co.jacobmetcalf.travelblog.model.TestData.IMAGE_1;

import org.junit.jupiter.api.Test;

public class ImageTemplateTest {

  @Test
  public void can_render_image() {
    String actualHtml = TestHelper.renderInDiv(d -> new ImageTemplate().add(d, IMAGE_1));

    assertThat(actualHtml, containsString("Imabura with snow from hotel"));
    assertThat(actualHtml, containsString("float-md-left"));
  }
}
