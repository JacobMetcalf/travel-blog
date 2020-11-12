package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Pulls a &lt;wiki&gt; or &lt;a&gt; element and the collection children from the event reader.
 */
public class AnchorParser implements ElementPullParser<Anchor> {

  public static final String WIKIPEDIA_BASE = "https://en.wikipedia.org/wiki/";

  private final ElementToken elementToken;
  private final String base;
  private final StringPullParser stringPullParser = new StringPullParser();

  private final AttributeParser<ImmutableAnchor.Builder> attributeParser;

  public AnchorParser() {
    this(ElementToken.A, "");
  }

  public AnchorParser(final ElementToken elementToken, final String base) {
    this.elementToken = elementToken;
    this.base = base;
    this.attributeParser = AttributeParser.<ImmutableAnchor.Builder>builder()
          .withElementToken(ElementToken.A)
          .put(AttributeToken.REF, (b, a) -> b.ref(base + a.getValue()))
          .put(AttributeToken.HREF, (b, a) -> b.ref(base + a.getValue()))
          .build();
  }

  @Override
  public Anchor pullElement(final XMLEventReader xmlEventReader, final Location parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement element = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), elementToken);

    Anchor result = attributeParser.parse(ImmutableAnchor.builder(), element)
        .text(stringPullParser.pullString(xmlEventReader, elementToken))
        .build();

    ElementToken.checkEndElement(xmlEventReader.nextEvent(), elementToken); // consume end
    return result;
  }
}
