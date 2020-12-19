package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

/**
 * Pulls a &lt;wiki&gt; or &lt;a&gt; element and the collection children from the event reader.
 */
public class AnchorPullParser
    implements ElementPullParser<Anchor>, MultipleElementPullParser<Anchor> {

  public static final String WIKIPEDIA_BASE = "https://en.wikipedia.org/wiki/";

  private final ElementToken elementToken;
  private final String base;
  private final StringPullParser stringPullParser = new StringPullParser();

  private final AttributeParser<ImmutableAnchor.Builder> attributeParser;

  public AnchorPullParser() {
    this(ElementToken.A, "");
  }

  public AnchorPullParser(final ElementToken elementToken, final String base) {
    this.elementToken = elementToken;
    this.base = addTrailingSlash(base);
    this.attributeParser = AttributeParser.<ImmutableAnchor.Builder>builder()
          .withElementToken(ElementToken.A)
          .put(AttributeToken.REF, (b, a) -> b.ref(this.base + a.getValue())) // Should deprecate
          .put(AttributeToken.HREF, (b, a) -> b.ref(this.base + a.getValue()))
          .put(AttributeToken.ICON, (b, a) -> b.icon(a.getValue()))
          .build();
  }

  @Override
  public Anchor pullElement(final XMLEventReader xmlEventReader, final Locatable parentLocatable)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement element = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), elementToken);

    Anchor result = attributeParser.parse(ImmutableAnchor.builder(), element)
        .text(stringPullParser.pullString(xmlEventReader, elementToken)
          .orElseThrow(() -> new IllegalStateException("Content of anchor not optional")))
        .build();

    ElementToken.checkEndElement(xmlEventReader.nextEvent(), elementToken); // consume end
    return result;
  }

  @Override
  public List<Anchor> pullElements(final XMLEventReader xmlEventReader) throws XMLStreamException {

    ImmutableList.Builder<Anchor> listBuilder = ImmutableList.builder();
    while (ElementToken.fromEventName(xmlEventReader.peek()) == elementToken) {

      ImmutableAnchor.Builder builder = attributeParser.parse(
          ImmutableAnchor.builder(),
          ElementToken.asStartElement(xmlEventReader.nextEvent(), elementToken));

      builder.text(stringPullParser.pullString(xmlEventReader, elementToken)
          .orElseThrow(() -> new IllegalStateException("Text of anchor not optional")));

      listBuilder.add(builder.build());
      ElementToken.checkEndElement(xmlEventReader.nextEvent(), elementToken);
    }
    return listBuilder.build();
  }

  private String addTrailingSlash(final String url) {
    if ((url.length() > 0 ) && !url.endsWith("/")) {
      return url + "/";
    }
    return url;
  }
}
