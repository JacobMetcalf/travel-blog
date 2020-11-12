package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Image;
import uk.co.jacobmetcalf.travelblog.model.ImmutableImage;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Pulls an &lt;image&gt; element from the parser
 */
public class ImageParser implements ElementPullParser<Image> {

  private final LocationParser locationParser = new LocationParser();

  // TODO: This is pretty fugly
  private final AttributeParser<BuilderWithLocation<ImmutableImage.Builder>> attributeParser =
      locationParser.append(AttributeParser.<BuilderWithLocation<ImmutableImage.Builder>>builder()
          .withElementToken(ElementToken.IMAGE)
          .put(AttributeToken.SRC, (b, a) -> b.outer().src(a.getValue()))
          .put(AttributeToken.TITLE, (b, a) -> b.outer().title(a.getValue()))
          .put(AttributeToken.POSITION, (b, a) -> b.outer().position(
              Image.Position.valueOf(a.getValue().toUpperCase()))))
          .build();

  public Image pullElement(final XMLEventReader xmlEventReader,
      final Location parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement imageElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);

    // TODO: This is pretty fugly
    final BuilderWithLocation<ImmutableImage.Builder> builderWithLocation =
        new BuilderWithLocation<>(ImmutableImage.builder(), parentLocation);
    attributeParser.parse(builderWithLocation, imageElement);

    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);

    Location location = builderWithLocation.inner().build();
    return builderWithLocation.outer().location(location).build();
  }
}
