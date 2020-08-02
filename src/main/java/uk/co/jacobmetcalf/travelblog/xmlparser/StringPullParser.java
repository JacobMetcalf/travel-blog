package uk.co.jacobmetcalf.travelblog.xmlparser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

/**
 * Pulls a string of characters within another element.
 */
public class StringPullParser {
  public String pullString(final XMLEventReader xmlEventReader,
      final ElementToken elementToken) throws XMLStreamException {

    StringBuilder stringBuilder = new StringBuilder();
    boolean elementOpen = true;

    while (elementOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();

      if (peekedEvent.isCharacters() || peekedEvent.isEntityReference()) {
        Characters asCharacters = xmlEventReader.nextEvent().asCharacters();
        if (!asCharacters.isIgnorableWhiteSpace()) {
          stringBuilder.append(asCharacters.getData());
        }
      } else if (peekedEvent.isEndElement()) {
        // Close paragraph
        ElementToken.asEndElement(peekedEvent, elementToken);
        elementOpen = false;

      } else {
        throw new IllegalStateException("Unexpectected event: " + peekedEvent);
      }
    }
    return stringBuilder.toString();
  }
}
