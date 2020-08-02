package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.time.LocalDate;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

public class EntryParser implements ElementPullParser<Entry> {

  private final LocationParser locationParser = new LocationParser();
  private final ParagraphParser paragraphParser = new ParagraphParser();

  public Entry pullElement(final XMLEventReader xmlEventReader,
      final ImmutableLocation.Builder parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    final StartElement element =
        ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.ENTRY);

    final ImmutableEntry.Builder entryBuilder = ImmutableEntry.builder();
    Location location = parseAttributes(parentLocation, element, entryBuilder);

    boolean entryOpen = true;
    while (entryOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();
      if (peekedEvent.isStartElement()) {
        // A paragraph of text and other elements
        ElementToken.asStartElement(peekedEvent, ElementToken.PARAGRAPH);
        entryBuilder.addParagraphs(paragraphParser.pullElement(xmlEventReader, parentLocation));

      } else if (peekedEvent.isEndElement()) {
        // Close entry
        ElementToken.asEndElement(xmlEventReader.nextEvent(), ElementToken.ENTRY);
        entryOpen = false;

      } else {
        throw new IllegalStateException("Unexpected event: " + peekedEvent);
      }
    }
    return entryBuilder.build();
  }

  private Location parseAttributes(ImmutableLocation.Builder parentLocation,
      StartElement element, ImmutableEntry.Builder entryBuilder) {
    Location location = locationParser.pullLocationAsAttributes(element, parentLocation,
        (attributeToken, attribute) -> {
          if (attributeToken == AttributeToken.DATE) {
            entryBuilder.date(LocalDate.parse(attribute.getValue()));
          } else {
            throw new IllegalStateException("Unexpected attribute: " + attributeToken.name()
                + " != DATE|" + Joiner.on("|").join(LocationParser.EXPECTED_ATTRIBUTES));
          }
        });
    entryBuilder.location(location);
    return location;
  }
}
