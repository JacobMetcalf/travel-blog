package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Image;
import uk.co.jacobmetcalf.travelblog.model.ImmutableImage;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Pulls an &lt;image&gt; element from the parser
 */
public class ImageParser implements ElementPullParser<Image> {

  private final LocationParser locationParser = new LocationParser();

  public Image pullElement(final XMLEventReader xmlEventReader,
      final ImmutableLocation.Builder parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement imageElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);

    //TODO: Possibly better that location

    final ImmutableImage.Builder imageBuilder = ImmutableImage.builder();
    Location location = locationParser.pullLocationAsAttributes(imageElement, parentLocation,
        (attributeToken, attribute) -> {
          switch (attributeToken) {
            case SRC:
              imageBuilder.src(attribute.getValue());
              break;
            case TITLE:
              imageBuilder.title(attribute.getValue());
              break;
            case POSITION:
              imageBuilder.position(
                  Image.Position.valueOf(attribute.getValue().toUpperCase()));
              break;
            default:
              throw new IllegalStateException("Unexpected attribute: " + attributeToken.name()
                  + " != SRC|POSITION|TITLE|"
                  + Joiner.on("|").join(LocationParser.EXPECTED_ATTRIBUTES));
          }
        });

    imageBuilder.location(location);
    ElementToken.asEndElement(xmlEventReader.nextEvent(), ElementToken.IMAGE);
    return imageBuilder.build();

  }
}
