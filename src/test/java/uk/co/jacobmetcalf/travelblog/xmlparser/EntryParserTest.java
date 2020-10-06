package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class EntryParserTest {

  public final EntryParser unit = new EntryParser();

  @Test
  public void can_parse_a_valid_empty_entry() {
    String inputXml = "<entry date=\"2018-07-19\" country=\"Ecuador\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\"/>";

    EntryOrRoute actual = TestHelper.tryParse(inputXml, unit, TestData.ECUADOR);
    assertTrue(actual.getEntry().isPresent());
    assertThat(actual.getEntry().get().getDate(), equalTo( LocalDate.of(2018,7,19)));
    assertThat(actual.getEntry().get().getLocation(), equalTo(TestData.QUITO));
  }

  @Test
  public void inherits_country() {
    // Omit ecuador - it should inherit from parent
    String inputXml = "<entry date=\"2018-07-19\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\"/>";

    EntryOrRoute actual = TestHelper.tryParse(inputXml, unit, TestData.ECUADOR);
    assertTrue(actual.getEntry().isPresent());
    assertThat(actual.getEntry().get().getLocation(), equalTo(TestData.QUITO));
  }

  @Test
  public void can_parse_an_entry_with_paragraph() {
    String inputXml = "<entry date=\"2018-07-19\" country=\"Ecuador\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\">"
    + "<paragraph>Some text</paragraph></entry>";

    EntryOrRoute actual = TestHelper.tryParse(inputXml, unit, TestData.ECUADOR);
    assertTrue(actual.getEntry().isPresent());
    actual.getEntry().ifPresent(e -> {
      assertThat(e.getDate(), equalTo(LocalDate.of(2018, 7, 19)));
      assertThat(e.getLocation(), equalTo(TestData.QUITO));

      assertThat(e.getParagraphs().get(0).getParts(),
          Matchers.contains(equalTo(ImmutableText.builder().text("Some text").build())));
    });
  }
}
