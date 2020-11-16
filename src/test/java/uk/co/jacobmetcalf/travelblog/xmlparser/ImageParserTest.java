package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Image;
import uk.co.jacobmetcalf.travelblog.model.Image.Position;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class ImageParserTest {
  public final ImageParser unit = new ImageParser();

  @Test
  public void can_parse_a_valid_image() {
    String inputXml = "<image src=\"ecuador066\" position=\"right\" "
        + "province=\"Cotopaxi\" location=\"Chugchil&#225;n\" title=\"Tristerix longebracteatus\"/>";

    Image actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);
    assertThat(actual.getSrc(), equalTo("ecuador066"));
    assertThat(actual.getPosition(), equalTo(Position.RIGHT));
    assertThat(actual.getTitle(), equalTo("Tristerix longebracteatus"));

    assertThat(actual.getCountry().orElseThrow(), equalTo("Ecuador"));
    assertThat(actual.getProvince().orElseThrow(), equalTo("Cotopaxi"));
    assertThat(actual.getLocation().orElseThrow(), equalTo("Chugchilán"));
  }
}
