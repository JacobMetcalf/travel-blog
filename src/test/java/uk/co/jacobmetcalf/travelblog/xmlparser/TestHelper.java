package uk.co.jacobmetcalf.travelblog.xmlparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Assertions;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class TestHelper {
  public static <E> E tryParse(String inputXml, ElementPullParser<E> unit,
      Location parentLocation) {
    try (InputStream inputStream = new ByteArrayInputStream(inputXml.getBytes())) {
      XMLEventReader xmlEventReader = FilteredReaderFactory.create(inputStream);

      // Skip start document
      Assertions.assertTrue(xmlEventReader.nextEvent().isStartDocument());

      return unit.pullElement(xmlEventReader, parentLocation);

    } catch (XMLStreamException | IOException ex) {
      throw new RuntimeException("Could not read xml", ex);
    }
  }
}
