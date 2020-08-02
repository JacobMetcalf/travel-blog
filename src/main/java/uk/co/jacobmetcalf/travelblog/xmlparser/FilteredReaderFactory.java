package uk.co.jacobmetcalf.travelblog.xmlparser;

import java.io.InputStream;
import javax.xml.stream.EventFilter;
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

  private static XMLInputFactory factory = XMLInputFactory.newFactory();

  public static XMLEventReader create(@NonNull final InputStream inputStream)
      throws XMLStreamException {
    final XMLEventReader rawReader =
        factory.createXMLEventReader(inputStream);

    // Filter out ignorable whitespace, namespaces and processing instructions
    final XMLEventReader filteredReader = factory.createFilteredReader(rawReader,
        new EventFilter() {
          public boolean accept(XMLEvent e) {
            return !((e.isCharacters() && e.asCharacters().isIgnorableWhiteSpace())
                || e.isProcessingInstruction() || e.isNamespace());
          }
        });
    return filteredReader;
  }
}
