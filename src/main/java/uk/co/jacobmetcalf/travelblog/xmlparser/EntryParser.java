package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import java.time.LocalDate;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Pulls an entry in a diary. Returns EntryOrRoute which allows the bulk of the diary
 * to be streamed over.
 */
public class EntryParser implements ElementPullParser<EntryOrRoute> {

  private final LocationParser locationParser = new LocationParser();
  private final ParagraphParser paragraphParser = new ParagraphParser();

  // TODO: This is pretty fugly
  private final AttributeParser<BuilderWithLocation<ImmutableEntry.Builder>> attributeParser =
      locationParser.append(AttributeParser.<BuilderWithLocation<ImmutableEntry.Builder>>builder()
          .withElementToken(ElementToken.ENTRY)
          .put(AttributeToken.DATE, (b, a) -> b.outer().date(LocalDate.parse(a.getValue()))))
          .build();

  @Override
  public EntryOrRoute pullElement(final XMLEventReader xmlEventReader,
      @Nullable final Location parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    final StartElement element =
        ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.ENTRY);

    // TODO: This is pretty fugly
    final BuilderWithLocation<ImmutableEntry.Builder> builderWithLocation =
        new BuilderWithLocation<>(ImmutableEntry.builder(), parentLocation);
    attributeParser.parse(builderWithLocation, element);
    Location location = builderWithLocation.inner().build();
    ImmutableEntry.Builder entryBuilder =
        builderWithLocation.outer().location(location);

    boolean entryOpen = true;
    while (entryOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();
      if (peekedEvent.isStartElement()) {
        // A paragraph of text and other elements
        ElementToken.asStartElement(peekedEvent, ElementToken.PARAGRAPH);
        entryBuilder.addParagraphs(paragraphParser.pullElement(xmlEventReader,location));

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
