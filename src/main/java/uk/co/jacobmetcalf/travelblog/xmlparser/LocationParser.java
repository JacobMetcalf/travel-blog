package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import org.checkerframework.checker.nullness.qual.Nullable;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation;
import uk.co.jacobmetcalf.travelblog.model.Locatable;
import uk.co.jacobmetcalf.travelblog.model.Location;

/**
 * Parses the dedicated location element but also able to construct a location
 * based on the attributes in another element, e.g. an entry, an image.
 */
public class LocationParser implements ElementPullParser<Location> {

  private final StringPullParser stringPullParser = new StringPullParser();

  public <B extends Locatable.Builder> AttributeParser.Builder<B> addLocationAttributes(AttributeParser.Builder<B> parserBuilder) {
    return parserBuilder.put(AttributeToken.COUNTRY, (b, a) -> b.country(a.getValue()))
        .put(AttributeToken.PROVINCE, (b, a) -> b.province(a.getValue()))
        .put(AttributeToken.LOCATION, (b, a) -> b.location(a.getValue()))
        .put(AttributeToken.LATITUDE, (b, a) -> b.latitude(Double.parseDouble(a.getValue())))
        .put(AttributeToken.LONGITUDE, (b, a) -> b.longitude(Double.parseDouble(a.getValue())))
        .put(AttributeToken.ZOOM, (b, a) -> b.zoom(Integer.parseInt(a.getValue())))
        .put(AttributeToken.WIKI, (b, a) -> b.wiki(AnchorPullParser.WIKIPEDIA_BASE + a.getValue()));
  }

  private final AttributeParser<ImmutableLocation.Builder> attributeParser =
      addLocationAttributes(AttributeParser.<ImmutableLocation.Builder>builder()
          .withElementToken(ElementToken.LOCATION))
          .build();

  /**
   * @param xmlEventReader reader positioned at the location element
   * @return Populated location element.
   * @throws XMLStreamException If could not parse Xml
   * @throws IllegalStateException If encounters Xml it is not expecting
   */
  public Location pullElement(final XMLEventReader xmlEventReader,
      @Nullable final Locatable parentLocatable)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement locationElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.LOCATION);

    String locationName = stringPullParser.pullString(xmlEventReader, ElementToken.LOCATION)
        .orElseThrow(() -> new IllegalStateException("Content of Location element not optional"));

    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.LOCATION); // consume end

    return attributeParser.parse(ImmutableLocation.builder().from(parentLocatable), locationElement)
        .location(locationName).build();
  }
}
