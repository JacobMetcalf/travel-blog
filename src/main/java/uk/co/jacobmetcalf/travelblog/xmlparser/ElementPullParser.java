package uk.co.jacobmetcalf.travelblog.xmlparser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;

/**
 * Generic interface which for now makes testing a bit easier
 * @param <E> The type of model object it parses
 */
public interface ElementPullParser<E> {

  /**
   * Pulls an element of type E from the event reader.
   * @param xmlEventReader The reader
   * @param parentLocation A (possibly partially) populated location builder from the parent element.
   * @return An element of type E
   * @throws XMLStreamException If there were issues process the XML.
   */
  E pullElement(XMLEventReader xmlEventReader,
      ImmutableLocation.Builder parentLocation) throws XMLStreamException;
}
