package uk.co.jacobmetcalf.travelblog.xmlparser;

import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Creates a reader which filters out all white space, namespace entries and processing
 * instructions.
 */
public final class FilteredReaderFactory {

  private static final XMLInputFactory factory = XMLInputFactory.newFactory();

  public static XMLEventReader create(@NonNull final InputStream inputStream)
      throws XMLStreamException {
    final XMLEventReader rawReader =
        factory.createXMLEventReader(inputStream);

    // Filter out ignorable whitespace, namespaces and processing instructions
    return factory.createFilteredReader(rawReader,
        e -> !((e.isCharacters() && e.asCharacters().isWhiteSpace())
                || e.isProcessingInstruction() || e.isNamespace()
                || e.getEventType() == XMLEvent.COMMENT));
  }
}
