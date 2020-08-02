package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;

public class EntryParserTest {

  public EntryParser unit = new EntryParser();

  @Test
  public void can_parse_a_valid_empty_entry() {
    String inputXml = "<entry date=\"2018-07-19\" country=\"Ecuador\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\"/>";

    Entry actual = TestUtil.tryParse(inputXml, unit, TestUtil.ecuadorAsBuilder());
    assertThat(actual.getDate(), equalTo( LocalDate.of(2018,7,19)));
    assertThat(actual.getLocation(), equalTo(TestUtil.QUITO));
  }

  @Test
  public void inherits_country() {
    // Omit ecuador - it should inherit from parent
    String inputXml = "<entry date=\"2018-07-19\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\"/>";

    Entry actual = TestUtil.tryParse(inputXml, unit, TestUtil.ecuadorAsBuilder());
    assertThat(actual.getLocation(), equalTo(TestUtil.QUITO));
  }

  @Test
  public void can_parse_an_entry_with_paragraph() {
    String inputXml = "<entry date=\"2018-07-19\" country=\"Ecuador\" province=\"Pichincha\" "
        + "location=\"Quito\" latitude=\"-0.2181\" longitude=\"-78.5084\">"
    + "<paragraph>Some text</paragraph></entry>";

    Entry actual = TestUtil.tryParse(inputXml, unit, TestUtil.ecuadorAsBuilder());
    assertThat(actual.getDate(), equalTo(LocalDate.of(2018, 7, 19)));
    assertThat(actual.getLocation(), equalTo(TestUtil.QUITO));

    assertThat(actual.getParagraphs().get(0).getParts(),
        Matchers.contains(equalTo(ImmutableText.builder().text("Some text").build())));
  }
}