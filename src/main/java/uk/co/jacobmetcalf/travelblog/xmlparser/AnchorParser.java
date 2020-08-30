package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableAnchor;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation.Builder;

/**
 * Pulls a &lt;wiki&gt; or &lt;a&gt; element and the collection children from the event reader.
 */
public class AnchorParser implements ElementPullParser<Anchor> {

  public static final String WIKIPEDIA_BASE = "https://en.wikipedia.org/wiki/";

  private final ElementToken elementToken;
  private final String base;
  private final StringPullParser stringPullParser = new StringPullParser();

  public AnchorParser() {
    this.elementToken = ElementToken.A;
    this.base = "";
  }

  public AnchorParser(@NonNull ElementToken elementToken, @NonNull String base) {
    this.elementToken = elementToken;
    this.base = base;
  }

  @Override
  public Anchor pullElement(XMLEventReader xmlEventReader, Builder parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement element = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), elementToken);

    final ImmutableAnchor.Builder anchorBuilder = ImmutableAnchor.builder();
    Streams.stream(element.getAttributes())
        .forEach(a -> parseAttribute(anchorBuilder, a));

    anchorBuilder.text(stringPullParser.pullString(xmlEventReader, elementToken));
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), elementToken); // consume end
    return anchorBuilder.build();
  }

  /**
   * Note: ref attribute can be absolute or relative to a specified base.
   */
  private void parseAttribute(final ImmutableAnchor.Builder anchorBuilder, final Attribute attribute ) {
    AttributeToken attributeToken = AttributeToken.fromAttributeName(attribute);
    if (attributeToken == AttributeToken.REF || attributeToken == AttributeToken.HREF) {
      anchorBuilder.ref(base + attribute.getValue());

    } else {
      throw new IllegalStateException("Unexpected attribute: "
          + attribute.getName() + ", expected REF | HREF");
    }
  }
}
