package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import java.util.Set;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.ImmutablePoint;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Point;

/**
 * Pulls a point element from the stream.
 */
public class PointParser implements ElementPullParser<Point> {

  public static final Set<AttributeToken> EXPECTED_ATTRIBUTES =
      ImmutableSet.of(AttributeToken.LATITUDE, AttributeToken.LONGITUDE);

  /**
   * @param xmlEventReader reader positioned at the point element
   * @return Populated point element.
   * @throws XMLStreamException If could not parse Xml
   * @throws IllegalStateException If encounters Xml it is not expecting
   */
  public Point pullElement(final XMLEventReader xmlEventReader,
      final Location parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement pointElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.POINT);

    final ImmutablePoint.Builder pointBuilder = ImmutablePoint.builder();
    Streams.stream(pointElement.getAttributes())
        .forEach(a -> parseAttribute(pointBuilder, a));

    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.POINT); // consume end
    return pointBuilder.build();
  }

  private void parseAttribute(final ImmutablePoint.Builder pointBuilder, final Attribute attribute) {
    AttributeToken attributeToken = AttributeToken.fromAttributeName(attribute);
    switch (attributeToken) {
      case LATITUDE -> pointBuilder.latitude(Double.valueOf(attribute.getValue()));
      case LONGITUDE -> pointBuilder.longitude(Double.valueOf(attribute.getValue()));
      default -> throw new IllegalStateException("Unexpected attribute: " + attribute
          + ", expecting: " + Joiner.on("|").join(EXPECTED_ATTRIBUTES));
    }
  }
}
