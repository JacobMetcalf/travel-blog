package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttributeParserTest {

  private static class TestObject {
    public String testAttribute;
    public String testAttribute2;

    public void setTestAttribute(String value) {
      testAttribute = value;
    }

    public void setTestAttribute2(String value) {
      testAttribute2 = value;
    }
  }

  public final AttributeParser<TestObject> unit =
      AttributeParser.<TestObject>builder()
        .withElementToken(ElementToken.BOOK)
        .put(AttributeToken.COUNTRY, (b,a) -> b.setTestAttribute(a.getValue()))
        .build();

  @Test
  public void can_set_attribute() {
    TestObject actual = new TestObject();
    unit.parse(actual, parse_xml_to_start("<test country=\"UK\">"));

    assertThat(actual.testAttribute, equalTo("UK"));
  }

  @Test
  public void throws_if_unexpected_attribute() {
    TestObject actual = new TestObject();
    StartElement element = parse_xml_to_start("<test unexpected=\"unexpected\">");

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> unit.parse(actual, element));
    assertThat(ex.getMessage(), Matchers.equalTo(
        "Unexpected attribute: unexpected='unexpected' of BOOK, expecting: COUNTRY"));
  }

  @Test
  public void can_extend_attribute_set() {
    TestObject actual = new TestObject();
    AttributeParser<TestObject> extendedUnit = AttributeParser.builder(unit)
        .withElementToken(ElementToken.LOCATION)
        .put(AttributeToken.PROVINCE, (b,a) -> b.setTestAttribute2(a.getValue()))
        .build();

    StartElement element = parse_xml_to_start("<test country=\"UK\" province=\"Yorkshire\">");
    extendedUnit.parse(actual, element);

    assertThat(actual.testAttribute, equalTo("UK"));
    assertThat(actual.testAttribute2, equalTo("Yorkshire"));

    // Check original not impacted
    assertThrows(IllegalStateException.class, () -> unit.parse(actual, element));
  }

  private StartElement parse_xml_to_start(String inputXml) {
    try (
        InputStream inputStream = new ByteArrayInputStream(inputXml.getBytes())) {
      XMLEventReader xmlEventReader = FilteredReaderFactory.create(inputStream);

      // Skip start document
      Assertions.assertTrue(xmlEventReader.nextEvent().isStartDocument());
      return xmlEventReader.nextEvent().asStartElement();

    } catch (XMLStreamException | IOException ex) {
      throw new RuntimeException("Could not read xml", ex);
    }
  }
}
