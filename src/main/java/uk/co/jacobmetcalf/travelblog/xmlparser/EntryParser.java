package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import java.time.LocalDate;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

/**
 * Pulls an entry in a diary. Returns EntryOrRoute which allows the bulk of the diary
 * to be streamed over.
 */
public class EntryParser implements ElementPullParser<EntryOrRoute> {

  private final LocationParser locationParser = new LocationParser();
  private final ParagraphParser paragraphParser = new ParagraphParser();

  private final AttributeParser<ImmutableEntry.Builder> attributeParser =
      locationParser.addLocationAttributes(AttributeParser.<ImmutableEntry.Builder>builder()
          .withElementToken(ElementToken.ENTRY)
          .put(AttributeToken.DATE, (b, a) -> b.date(LocalDate.parse(a.getValue()))))
          .build();

  @Override
  public EntryOrRoute pullElement(final XMLEventReader xmlEventReader,
      @Nullable final Locatable parentLocatable) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    final StartElement element =
        ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.ENTRY);

    ImmutableEntry.Builder entryBuilder =
        attributeParser.parse(ImmutableEntry.builder().from(parentLocatable), element);

    Entry entryPreview = entryBuilder.build();
    entryBuilder = ImmutableEntry.builder().from(entryPreview);

    boolean entryOpen = true;
    while (entryOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();
      if (peekedEvent.isStartElement()) {
        // A paragraph of text and other elements
        ElementToken.asStartElement(peekedEvent, ElementToken.PARAGRAPH);
        entryBuilder.addParagraphs(paragraphParser.pullElement(xmlEventReader, entryPreview));

      } else if (peekedEvent.isEndElement()) {
        // Close entry
        ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.ENTRY);
        entryOpen = false;

      } else {
        throw new IllegalStateException("Unexpected event: " + peekedEvent);
      }
    }

    return ImmutableEntryOrRoute.builder()
        .entry(entryBuilder.build()).build();
  }
}
