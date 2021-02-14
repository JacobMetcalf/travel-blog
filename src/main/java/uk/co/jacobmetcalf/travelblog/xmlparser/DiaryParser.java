package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiary;
import uk.co.jacobmetcalf.travelblog.model.Locatable;
import uk.co.jacobmetcalf.travelblog.model.Properties;
import uk.co.jacobmetcalf.travelblog.model.Properties.Key;

/**
 * Pull parses an XML document into an ordered stream of Entry objects.
 *
 * The stream ensures that we are only dealing with a day at a time so keeps the memory a bit
 * more efficient.
 */
public class DiaryParser {

  private XMLEventReader xmlEventReader;
  private Locatable rootLocatable;
  private final AnchorPullParser navigationParser;
  private final BookPullParser bookParser = new BookPullParser();
  private final StringPullParser stringPullParser = new StringPullParser();

  private final AttributeParser<ImmutableDiary.Builder> attributeParser =
      new LocationParser().addLocationAttributes(AttributeParser.<ImmutableDiary.Builder>builder()
          .withElementToken(ElementToken.DIARY)
          .put(AttributeToken.TITLE, (b, a) -> b.title(a.getValue()))
          .put(AttributeToken.THUMB, (b, a) -> b.thumb(a.getValue())))
          .build();

  public DiaryParser(final Properties properties) {
    this.navigationParser = new AnchorPullParser(ElementToken.NAVIGATION,
        properties.get(Key.CANONICAL_URL).orElseThrow());
  }

  public Diary parse(final String filename, final InputStream inputStream) throws XMLStreamException {

    ImmutableDiary.Builder diaryBuilder = ImmutableDiary.builder();
    Preconditions.checkNotNull(inputStream, "Input stream cannot be null");
    diaryBuilder.filename(filename);

    try {
      xmlEventReader = FilteredReaderFactory.create(inputStream);

      // Skip the start document
      XMLEvent event = xmlEventReader.nextEvent();
      Preconditions.checkState(event.isStartDocument(), "Expected start of document");

      // Process first two nested elements
      attributeParser.parse(diaryBuilder,
          ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.DIARY));
      attributeParser.parse(diaryBuilder,
          ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.SUMMARY));

      diaryBuilder.summary(stringPullParser.pullString(xmlEventReader, ElementToken.SUMMARY));
      ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.SUMMARY);

      // Process navigation and books
      diaryBuilder.addAllNavigationAnchors(navigationParser.pullElements(xmlEventReader));
      diaryBuilder.addAllBooks(bookParser.pullElements(xmlEventReader));

      EventIterator iterator = new EventIterator();
      diaryBuilder.entriesAndRoutes(StreamSupport.stream(Spliterators.spliteratorUnknownSize(
          iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL),
          false));

      Diary result = diaryBuilder.build();
      this.rootLocatable = result;
      iterator.next();
      return result;
    } catch (XMLStreamException | IllegalStateException | IllegalArgumentException ex) {
      throw wrapException(ex);
    } finally {
      xmlEventReader.close();
    }
  }

  private RuntimeException wrapException(final Exception ex) {
    try {
      // TODO wrap reader so we can get current location
      if ((xmlEventReader != null) && (xmlEventReader.peek() != null)) {
        throw new RuntimeException("Before line: "
            + xmlEventReader.peek().getLocation().getLineNumber()
            + ", col: "
            + xmlEventReader.peek().getLocation().getColumnNumber()
            + ": "
            + ex.getMessage(), ex);
      }
      return new RuntimeException(ex);
    } catch (XMLStreamException ex2) {
      return new RuntimeException(ex);
    }
  }

    /**
   * Iterator over the XML stream
   */
  private class EventIterator implements Iterator<EntryOrRoute> {

    private EntryOrRoute nextEntry;

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
            nextEntry = new EntryParser().pullElement(xmlEventReader, rootLocatable);
          } else if (token == ElementToken.ROUTE) {
            nextEntry = new RouteParser().pullElement(xmlEventReader, rootLocatable);
          } else {
            throw new IllegalStateException("Unexpected element: " + peekEvent);
          }
        }
      } catch (XMLStreamException | IllegalStateException | IllegalArgumentException ex) {
        throw wrapException(ex);
      }
      return result;
    }
  }
}
