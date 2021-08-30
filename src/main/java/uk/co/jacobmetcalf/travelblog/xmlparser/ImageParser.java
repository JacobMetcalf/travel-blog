package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Image;
import uk.co.jacobmetcalf.travelblog.model.ImmutableImage;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

/**
 * Pulls an &lt;image&gt; element from the parser
 */
public class ImageParser implements ElementPullParser<Image> {

  private final LocationParser locationParser = new LocationParser();

  private final AttributeParser<ImmutableImage.Builder> attributeParser =
      locationParser.addLocationAttributes(AttributeParser.<ImmutableImage.Builder>builder()
          .withElementToken(ElementToken.IMAGE)
          .put(AttributeToken.SRC, (b, a) -> b.src(a.getValue()))
          .put(AttributeToken.TITLE, (b, a) -> b.title(a.getValue()))
          .put(AttributeToken.POSITION, (b, a) -> b.position(
              Image.Position.valueOf(a.getValue().toUpperCase()))))
          .build();

  @Override
  public Image pullElement(final XMLEventReader xmlEventReader,
      final Locatable parentLocatable) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());

    StartElement imageElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);

    return attributeParser
        .parse(ImmutableImage.builder().from(parentLocatable), imageElement)
        .build();
  }
}
