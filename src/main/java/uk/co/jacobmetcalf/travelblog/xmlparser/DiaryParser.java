package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;

/**
 * Pull parses an XML document into an ordered stream of Entry objects.
 *
 * The stream ensures that we are only dealing with a day at a time so keeps the memory a bit
 * more efficient.
 */
public class DiaryParser {

  private XMLEventReader xmlEventReader;
  private ImmutableLocation.Builder rootLocation;

  public Stream<Entry> parse(@NonNull final InputStream inputStream) throws XMLStreamException {

    Preconditions.checkNotNull(inputStream, "Input stream cannot be null");

    xmlEventReader = FilteredReaderFactory.create(inputStream);

    // Skip the start document
    XMLEvent event = xmlEventReader.nextEvent();
    Preconditions.checkArgument(event.isStartDocument(), "Expected start of document");

    event = xmlEventReader.nextEvent();
    ElementToken.asStartElement(event, ElementToken.DIARY);

    //TODO: Get the properties of the diary entry and pass down
    rootLocation = ImmutableLocation.builder().country("Ecuador");

    EventIterator iterator = new EventIterator();
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
        iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.NONNULL), false);
  }

  private class EventIterator implements Iterator<Entry> {

    private Entry nextEntry;

    public EventIterator() {
      next(); // Peek ahead
    }

    @Override
    public boolean hasNext() {
      return nextEntry != null;
    }

    @Override
    public Entry next() {

      // Peek ahead pattern
      Entry result = nextEntry;
      try {
        nextEntry = null;
        if (xmlEventReader.hasNext()) {
          final XMLEvent peekEvent = xmlEventReader.peek();
          if (peekEvent.isEndElement()) {
            // Close of the diary element sets hasNext() to false
            ElementToken.checkEndElement(peekEvent, ElementToken.DIARY);
          } else if (peekEvent.isStartElement() && peekEvent.asStartElement().getName()
              .getLocalPart().equals(ElementToken.ENTRY.name().toLowerCase())) {
            nextEntry = new EntryParser().pullElement(xmlEventReader, rootLocation);
          } else {
            throw new RuntimeException("Unexpected event: " + peekEvent);
          }
        }
      } catch (XMLStreamException ex) {
        throw new RuntimeException("Could not parse as a diary", ex);
      }
      return result;
    }
  }
}
