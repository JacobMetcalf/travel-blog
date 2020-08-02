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
import org.checkerframework.checker.nullness.qual.NonNull;
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
  public Location pullElement(@NonNull final XMLEventReader xmlEventReader,
      final ImmutableLocation.@NonNull Builder parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement locationElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.LOCATION);

    String locationName = stringPullParser.pullString(xmlEventReader, ElementToken.LOCATION);
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.LOCATION); // consume end

    ImmutableLocation.Builder curriedLocation = parentLocation.location(locationName);
    return pullLocationAsAttributes(locationElement, curriedLocation,
        (a, b) -> { throw new IllegalStateException("Unexpected attribute: " + a
          + ", expecting: " + Joiner.on("|").join(LocationParser.EXPECTED_ATTRIBUTES));});
  }

  /**
   * @param startElement An element with attributes which represent a location
   * @param handleOther Consumer called when a non-location attribute encountered. Useful for
   *                    dealing with a mix of attributes.
   * @return Location A location in the world.
   */
  public Location pullLocationAsAttributes(@NonNull final StartElement startElement,
    final ImmutableLocation.@NonNull Builder parentLocation,
    final @NonNull BiConsumer<AttributeToken, Attribute> handleOther) {

    Streams.stream(startElement.getAttributes())
        .forEach( a -> {
          AttributeToken attributeToken = AttributeToken.fromAttributeName(a);
          switch (attributeToken) {
            case COUNTRY -> parentLocation.country(a.getValue());
            case PROVINCE -> parentLocation.province(a.getValue());
            case LOCATION -> parentLocation.location(a.getValue());
            case LATITUDE -> parentLocation.latitude(Double.valueOf(a.getValue()));
            case LONGITUDE -> parentLocation.longitude(Double.valueOf(a.getValue()));
            case ZOOM -> parentLocation.zoom(Integer.parseInt(a.getValue()));
            case WIKI -> parentLocation.wiki(a.getValue());
            default -> handleOther.accept(attributeToken, a);
          }
        });

    return parentLocation.build();
  }
}