package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class RouteParserTest {

  private final RouteParser unit = new RouteParser();

  @Test
  public void can_parse_point() {
    String inputXml = "<route><point longitude=\"10.0\" latitude=\"-40.0\"/>"
        + "<point longitude=\"12.0\" latitude=\"-40.0\"/></route>";

    EntryOrRoute actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);

    assertTrue(actual.getRoute().isPresent());
    actual.getRoute().ifPresent(r -> {
      assertThat(r.getPoints().size(), Matchers.equalTo(2));
      assertThat(r.getPoints().get(0).getLatitude(), Matchers.closeTo(-40d, 0.001));
      assertThat(r.getPoints().get(0).getLongitude(), Matchers.closeTo(10d, 0.001));
      assertThat(r.getPoints().get(1).getLatitude(), Matchers.closeTo(-40d, 0.001));
      assertThat(r.getPoints().get(1).getLongitude(), Matchers.closeTo(12d, 0.001));
    });
  }

  @Test
  public void only_expects_points() {
    String invalidXml = "<route><point longitude=\"10.0\" latitude=\"-40.0\"/><book/></route>";

    Assertions.assertThrows(IllegalStateException.class,
        () -> TestHelper.tryParse(invalidXml, unit, TestData.QUITO));
  }
}
