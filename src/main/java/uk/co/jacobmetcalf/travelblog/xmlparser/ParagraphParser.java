package uk.co.jacobmetcalf.travelblog.xmlparser;

import static uk.co.jacobmetcalf.travelblog.xmlparser.AnchorParser.WIKIPEDIA_BASE;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph.Builder;
import uk.co.jacobmetcalf.travelblog.model.ImmutableText;
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
      final ImmutableLocation.Builder parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.PARAGRAPH);

    ImmutableParagraph.Builder paragraphBuilder = ImmutableParagraph.builder();
    boolean paragraphOpen = true;

    while (paragraphOpen && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();

      if (peekedEvent.isCharacters() || peekedEvent.isEntityReference()) {
        handleCharacters(xmlEventReader, paragraphBuilder);

      } else if (peekedEvent.isStartElement()) {
        handleElement(xmlEventReader, peekedEvent, parentLocation, paragraphBuilder);

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
      final ImmutableLocation.Builder parentLocation,
      final Builder paragraphBuilder) throws XMLStreamException {

      switch (ElementToken.fromEventName(peekedEvent)) {
        case IMAGE -> paragraphBuilder.addImages(
            imageParser.pullElement(xmlEventReader, parentLocation));
        case WIKI -> paragraphBuilder.addParts(
            wikiParser.pullElement(xmlEventReader, parentLocation));
        case A -> paragraphBuilder.addParts(
            plainAnchorParser.pullElement(xmlEventReader, parentLocation));
        case LOCATION -> paragraphBuilder.addParts(
            locationParser.pullElement(xmlEventReader, parentLocation));
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

