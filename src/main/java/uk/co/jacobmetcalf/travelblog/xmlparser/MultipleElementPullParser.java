package uk.co.jacobmetcalf.travelblog.xmlparser;

import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

/**
 * Generic interface which for now makes testing a bit easier
 * @param <E> The type of model object it parses
 */
public interface MultipleElementPullParser<E> {

  /**
   * Pulls an element of type E from the event reader.
   * @param xmlEventReader The reader
   * @return A list of elements of type E
   * @throws XMLStreamException If there were issues process the XML.
   */
  List<E> pullElements(XMLEventReader xmlEventReader) throws XMLStreamException;
}
