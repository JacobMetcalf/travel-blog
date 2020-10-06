package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import org.checkerframework.checker.nullness.qual.Nullable;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Parses the dedicated location element but also able to construct a location
 * based on the attributes in another element, e.g. an entry, an image.
 */
public class LocationParser implements ElementPullParser<Location> {

  private final StringPullParser stringPullParser = new StringPullParser();

  public static final Set<AttributeToken> EXPECTED_ATTRIBUTES =
      ImmutableSet.of(AttributeToken.COUNTRY, AttributeToken.PROVINCE, AttributeToken.LOCATION,
          AttributeToken.LATITUDE, AttributeToken.LONGITUDE);
  /**
   * @param xmlEventReader reader positioned at the location element
   * @return Populated location element.
   * @throws XMLStreamException If could not parse Xml
   * @throws IllegalStateException If encounters Xml it is not expecting
   */
  public Location pullElement(final XMLEventReader xmlEventReader,
      @Nullable final Location parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement locationElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.LOCATION);

    String locationName = stringPullParser.pullString(xmlEventReader, ElementToken.LOCATION);
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.LOCATION); // consume end

    Location locationWithName = ImmutableLocation.builder()
        .from(parentLocation).location(locationName).build();

    return pullLocationAsAttributes(locationElement, locationWithName,
        (a, b) -> { throw new IllegalStateException("Unexpected attribute: " + a
          + ", expecting: " + Joiner.on("|").join(LocationParser.EXPECTED_ATTRIBUTES));});
  }

  /**
   * @param startElement An element with attributes which represent a location
   * @param handleOther Consumer called when a non-location attribute encountered. Useful for
   *                    dealing with a mix of attributes.
   * @return Location A location in the world.
   */
  public Location pullLocationAsAttributes(
      final StartElement startElement,
      final Location parentLocation,
      final BiConsumer<AttributeToken, Attribute> handleOther) {

    ImmutableLocation.Builder location = ImmutableLocation.builder().from(parentLocation);
    Streams.stream(startElement.getAttributes())
        .forEach( a -> {
          AttributeToken attributeToken = AttributeToken.fromAttributeName(a);
          switch (attributeToken) {
            case COUNTRY -> location.country(a.getValue());
            case PROVINCE -> location.province(a.getValue());
            case LOCATION -> location.location(a.getValue());
            case LATITUDE -> location.latitude(Double.parseDouble(a.getValue()));
            case LONGITUDE -> location.longitude(Double.parseDouble(a.getValue()));
            case ZOOM -> location.zoom(Integer.parseInt(a.getValue()));
            case WIKI -> location.wiki(AnchorParser.WIKIPEDIA_BASE + a.getValue());
            default -> handleOther.accept(attributeToken, a);
          }
        });

    return location.build();
  }
}
