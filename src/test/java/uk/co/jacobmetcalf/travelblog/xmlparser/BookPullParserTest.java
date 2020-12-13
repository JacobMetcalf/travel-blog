package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Book;

public class BookPullParserTest {

  private final BookPullParser unit = new BookPullParser();

  @Test
  public void can_parse_one_book() {
    String inputXml = "<diary><book isin=\"123\" title=\"Interesting book\"/></diary>";
    List<Book> actual = tryParse(inputXml);

    assertThat(actual.size(), Matchers.equalTo(1));
    assertThat(actual.get(0).getIsin(), Matchers.equalTo("123"));
    assertThat(actual.get(0).getTitle(), Matchers.equalTo("Interesting book"));
  }

  @Test
  public void can_parse_two_books() {
    String inputXml = "<diary><book isin=\"123\" title=\"Interesting book\"/>"
        + "<book isin=\"456\" title=\"Not so interesting book\"/></diary>";
    List<Book> actual = tryParse(inputXml);

    assertThat(actual.size(), Matchers.equalTo(2));
    assertThat(actual.get(1).getIsin(), Matchers.equalTo("456"));
    assertThat(actual.get(1).getTitle(), Matchers.equalTo("Not so interesting book"));
  }

  @Test
  public void stops_on_other_element() {
    String inputXml = "<diary><book isin=\"123\" title=\"Interesting book\"/><entry/></diary>";
    List<Book> actual = tryParse(inputXml);
    assertThat(actual.size(), Matchers.equalTo(1));
  }

  @Test
  public void throws_if_content() {
    String inputXml = "<diary><book isin=\"123\" title=\"Interesting book\">Unexpected text</book></diary>";
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> tryParse(inputXml));
    assertThat(ex.getMessage(), Matchers.equalTo("Expected end of element: book"));
  }

  @Test
  public void throws_if_unexpected_attribute() {
    String inputXml = "<diary><book isin=\"123\" xyz=\"XYZ\"/></diary>";
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> tryParse(inputXml));
    assertThat(ex.getMessage(), Matchers.equalTo("Unexpected attribute: xyz='XYZ' of BOOK, expecting: TITLE|ISIN"));
  }


  private List<Book> tryParse(String inputXml) {
    try (InputStream inputStream = new ByteArrayInputStream(inputXml.getBytes())) {
      XMLEventReader xmlEventReader = FilteredReaderFactory.create(inputStream);

      // Skip start document
      Assertions.assertTrue(xmlEventReader.nextEvent().isStartDocument());
      ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.DIARY);
      return unit.pullElements(xmlEventReader);

    } catch (XMLStreamException | IOException ex) {
      throw new RuntimeException("Could not read xml", ex);
    }
  }
}
