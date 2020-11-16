package uk.co.jacobmetcalf.travelblog.xmlparser;

import static uk.co.jacobmetcalf.travelblog.xmlparser.AnchorParser.WIKIPEDIA_BASE;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph.Builder;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;
import uk.co.jacobmetcalf.travelblog.model.Locatable;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;

/**
 * Pulls a &lt;paragraph&gt; element and the collection children from the event reader.
 */
public class ParagraphParser implements ElementPullParser<Paragraph> {

  private final ImageParser imageParser = new ImageParser();
  private final AnchorParser wikiParser = new AnchorParser(ElementToken.WIKI, WIKIPEDIA_BASE);
  private final AnchorParser plainAnchorParser = new AnchorParser();
  private final LocationParser locationParser = new LocationParser();

  @Override
  public Paragraph pullElement(final XMLEventReader xmlEventReader,
      final Locatable parentLocatable) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.PARAGRAPH);

    ImmutableParagraph.Builder paragraphBuilder = ImmutableParagraph.builder();
    boolean paragraphOpen = true;

    while (paragraphOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();

      if (peekedEvent.isCharacters() || peekedEvent.isEntityReference()) {
        handleCharacters(xmlEventReader, paragraphBuilder);

      } else if (peekedEvent.isStartElement()) {
        handleElement(xmlEventReader, peekedEvent, parentLocatable, paragraphBuilder);

      } else if (peekedEvent.isEndElement()) {
        // Close paragraph
        ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.PARAGRAPH);
        paragraphOpen = false;

      } else {
        throw new IllegalStateException("Unexpected event: " + peekedEvent);
      }
    }
    return paragraphBuilder.build();
  }

  private void handleElement(final XMLEventReader xmlEventReader, final XMLEvent peekedEvent,
      final Locatable parentLocatable,
      final Builder paragraphBuilder) throws XMLStreamException {

      switch (ElementToken.fromEventName(peekedEvent)) {
        case IMAGE -> paragraphBuilder.addImages(
            imageParser.pullElement(xmlEventReader, parentLocatable));
        case WIKI -> paragraphBuilder.addParts(
            wikiParser.pullElement(xmlEventReader, parentLocatable));
        case A -> paragraphBuilder.addParts(
            plainAnchorParser.pullElement(xmlEventReader, parentLocatable));
        case LOCATION -> paragraphBuilder.addParts(
            locationParser.pullElement(xmlEventReader, parentLocatable));
        default -> throw new IllegalStateException("Unexpected element: "
            + ElementToken.fromEventName(peekedEvent));
      }
  }

  private void handleCharacters(final XMLEventReader reader, final Builder paragraphBuilder)
      throws XMLStreamException {
    Characters asCharacters = reader.nextEvent().asCharacters();
    if (!asCharacters.isIgnorableWhiteSpace()) {

      // TODO: Concatenate neighbouring text, handle carriage return instead of space
      ImmutableText text = ImmutableText.builder().text(asCharacters.getData()).build();
      paragraphBuilder.addParts(text);
    }
  }
}

