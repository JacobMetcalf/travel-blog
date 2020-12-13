package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.Diary;

public class DiaryParserTest {

  public static final String HEADER = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n";
  public static final String DIARY_ELEMENT = "<diary country=\"Ecuador\" title=\"Ecuador: Another Inca trail and searching for bears in the cloud forest\">\n";
  public static final String SUMMARY_ELEMENT = "  <summary latitude=\"-1.7\" longitude=\"-78.7\" zoom=\"7\" kml=\"ecuador.kml\" thumb=\"ecuador-thumb.jpg\">\n";
  private final DiaryParser unit = new DiaryParser();

  @Test
  public void parse_example_file() throws XMLStreamException, IOException {
    try (InputStream inputStream = this.getClass().getResourceAsStream("diary.xml")) {
      long numEntries = unit.parse("input.xml", inputStream).getEntriesAndRoutes().count();
      assertThat(numEntries, equalTo(23L));
    }
  }

  @Test
  public void parse_diary_with_no_summary_text() throws IOException, XMLStreamException {
    String inputXml = HEADER + DIARY_ELEMENT + SUMMARY_ELEMENT
        + "  </summary>\n"
        + "</diary>\n";

    Diary actual = tryParse(inputXml);
    assertThat(actual.getKml().orElseThrow(), equalTo("ecuador.kml"));
    assertThat(actual.getSummary(), equalTo(Optional.empty()));
  }


  @Test
  public void parse_diary_with_summary_test() throws IOException, XMLStreamException {
    String inputXml = HEADER + DIARY_ELEMENT + SUMMARY_ELEMENT
        + "    Some summary\n"
        + "  </summary>\n"
        + "</diary>\n";

    Diary actual = tryParse(inputXml);
    assertThat(actual.getKml().orElseThrow(), equalTo("ecuador.kml"));
    assertThat(actual.getSummary().orElseThrow(), equalTo("Some summary"));
  }

  @Test
  public void parse_diary_with_navigation_elements() throws IOException, XMLStreamException {
    String inputXml = HEADER + DIARY_ELEMENT + SUMMARY_ELEMENT
        + "  </summary>\n"
        + "  <navigation icon=\"backward\" href=\"china/xinjiang.html\">Prev Page</navigation>\n"
        + "  <navigation icon=\"forward\" href=\"china/sichuan.html\">Next Page</navigation>\n"
        + "</diary>\n";

    Diary actual = tryParse(inputXml);
    assertThat(actual.getNavigationAnchors().size(), equalTo(2));

    Anchor anchor1 = actual.getNavigationAnchors().get(0);
    assertThat(anchor1.getIcon().orElseThrow(), equalTo("backward"));
    assertThat(anchor1.getText(), equalTo("Prev Page"));

    Anchor anchor2 = actual.getNavigationAnchors().get(1);
    assertThat(anchor2.getIcon().orElseThrow(), equalTo("forward"));
    assertThat(anchor2.getText(), equalTo("Next Page"));
  }

  private Diary tryParse(String inputXml) throws IOException, XMLStreamException {
    try (InputStream inputStream = new ByteArrayInputStream(inputXml.getBytes())) {
      return unit.parse("file.xml", inputStream);
    }
  }
}
