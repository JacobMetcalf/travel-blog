package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Point;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class PointParserTest {

  private final PointParser unit = new PointParser();

  @Test
  public void can_parse_point() {
    String inputXml = "<point longitude=\"10.0\" latitude=\"-40.0\"/>";

    Point actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);

    assertThat(actual.getLatitude(), Matchers.closeTo(-40d, 0.001));
    assertThat(actual.getLongitude(), Matchers.closeTo(10d, 0.001));
  }

  @Test
  public void does_not_expect_content() {
    String invalidXml = "<point longitude=\"10.0\" latitude=\"-40.0\">Invalid</point>";

    Assertions.assertThrows(IllegalStateException.class,
        () -> TestHelper.tryParse(invalidXml, unit, TestData.QUITO));
  }

  @Test
  public void must_get_both_coords() {
    String invalidXml = "<point longitude=\"10.0\"/>";

    Assertions.assertThrows(IllegalStateException.class,
        () -> TestHelper.tryParse(invalidXml, unit, TestData.QUITO));
  }
}
