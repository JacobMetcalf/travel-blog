package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static uk.co.jacobmetcalf.travelblog.htmlrenderer.TestData.IMAGE_1;

import org.junit.jupiter.api.Test;

public class ImageTemplateTest {

  @Test
  public void can_render_image() {
    ImageTemplate unit = new ImageTemplate();
    StringBuilder builder = new StringBuilder();
    unit.add(builder, IMAGE_1);

    // Just check dynamic elements
    String actualHtml = builder.toString();
    assertThat(builder.toString(), containsString("Imabura with snow from hotel"));
    assertThat(builder.toString(), containsString("pull-md-left"));
  }
}