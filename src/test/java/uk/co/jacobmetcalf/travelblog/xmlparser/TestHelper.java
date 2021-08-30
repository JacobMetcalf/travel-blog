package uk.co.jacobmetcalf.travelblog.xmlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Assertions;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class TestHelper {
  public static <E> E tryParse(String inputXml, ElementPullParser<E> unit,
      Location parentLocation) {
    try (InputStream inputStream = new ByteArrayInputStream(inputXml.getBytes(StandardCharsets.UTF_8))) {
      XMLEventReader xmlEventReader = FilteredReaderFactory.create(inputStream);

      // Skip start document
      Assertions.assertTrue(xmlEventReader.nextEvent().isStartDocument());

      E result = unit.pullElement(xmlEventReader, parentLocation);
      Assertions.assertTrue(xmlEventReader.peek().isEndDocument()); // Check all consumed
      return result;


    } catch (XMLStreamException | IOException ex) {
      throw new RuntimeException("Could not read xml", ex);
    }
  }
}
