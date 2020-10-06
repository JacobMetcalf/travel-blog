package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Pull parses an XML document into an ordered stream of Entry objects.
 *
 * The stream ensures that we are only dealing with a day at a time so keeps the memory a bit
 * more efficient.
 */
public class DiaryParser {

  private XMLEventReader xmlEventReader;
  private Location rootLocation = ImmutableLocation.builder().build();
  private final LocationParser locationParser = new LocationParser();
  private final BookPullParser bookParser = new BookPullParser();
  private final ImmutableDiary.Builder diaryBuilder = ImmutableDiary.builder();

  public Diary parse(final String filename, final InputStream inputStream) throws XMLStreamException {

    Preconditions.checkNotNull(inputStream, "Input stream cannot be null");
    diaryBuilder.filename(filename);

    xmlEventReader = FilteredReaderFactory.create(inputStream);

    // Skip the start document
    XMLEvent event = xmlEventReader.nextEvent();
    Preconditions.checkState(event.isStartDocument(), "Expected start of document");

    // Process first two elements
    parseInitialElement(ElementToken.DIARY);
    parseInitialElement(ElementToken.SUMMARY);
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.SUMMARY);

    // Process books
    diaryBuilder.addAllBooks(bookParser.pullBooks(xmlEventReader));
    diaryBuilder.location(rootLocation);

    EventIterator iterator = new EventIterator();
    diaryBuilder.entriesAndRoutes(StreamSupport.stream(Spliterators.spliteratorUnknownSize(
        iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL), false));

    return diaryBuilder.build();
  }

  /**
   * For some reason I decided to split the location between the diary element and
   * a child summary element. The processing is simplified here by allowing the attributes
   * to be on either of the elements.
   * @param elementToken The element type being processed
   */
  private void parseInitialElement(final ElementToken elementToken)
      throws XMLStreamException {
    StartElement diaryElement = ElementToken.asStartElement(xmlEventReader.nextEvent(),
        elementToken);

    rootLocation = locationParser.pullLocationAsAttributes(diaryElement,
        rootLocation, this::parseAdditionalAttributes);
  }

  private void parseAdditionalAttributes(final AttributeToken attributeToken,
      final Attribute attribute) {
    switch (attributeToken) {
      case TITLE -> diaryBuilder.title(attribute.getValue());
      case THUMB -> diaryBuilder.thumb(attribute.getValue());
      case KML -> diaryBuilder.kml(attribute.getValue());
      default -> throw new IllegalStateException("Unexpected attribute: " + attributeToken.name()
          + " != TITLE|THUMB|KML|"
          + Joiner.on("|").join(LocationParser.EXPECTED_ATTRIBUTES));
    }
  }

  /**
   * Iterator over the XML stream
   */
  private class EventIterator implements Iterator<EntryOrRoute> {

    private EntryOrRoute nextEntry;

    public EventIterator() {
      next(); // Peek ahead
    }

    @Override
    public boolean hasNext() {
      return nextEntry != null;
    }

    @Override
    public EntryOrRoute next() {

      // Peek ahead pattern
      EntryOrRoute result = nextEntry;
      try {
        nextEntry = null;
        if (xmlEventReader.hasNext()) {
          final XMLEvent peekEvent = xmlEventReader.peek();
          ElementToken token = ElementToken.fromEventName(peekEvent);
          if (peekEvent.isEndElement()) {
            // Close of the diary element sets hasNext() to false
            ElementToken.checkEndElement(peekEvent, ElementToken.DIARY);
          } else if (token == ElementToken.ENTRY) {
            nextEntry = new EntryParser().pullElement(xmlEventReader, rootLocation);
          } else if (token == ElementToken.ROUTE) {
            nextEntry = new RouteParser().pullElement(xmlEventReader, rootLocation);
          } else {
            throw new IllegalStateException("Unexpected element: " + peekEvent);
          }
        }
      } catch (XMLStreamException ex) {
        throw new RuntimeException("Could not parse as a diary", ex);
      }
      return result;
    }
  }
}
