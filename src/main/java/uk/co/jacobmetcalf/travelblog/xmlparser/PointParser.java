package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.ImmutablePoint;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Point;

/**
 * Pulls a point element from the stream.
 */
public class PointParser implements ElementPullParser<Point> {

  private final AttributeParser<ImmutablePoint.Builder> attributeParser =
      AttributeParser.<ImmutablePoint.Builder>builder()
          .withElementToken(ElementToken.BOOK)
          .put(AttributeToken.LATITUDE, (b, a) -> b.latitude(Double.valueOf(a.getValue())))
          .put(AttributeToken.LONGITUDE, (b, a) -> b.longitude(Double.valueOf(a.getValue())))
          .build();

  /**
   * @param xmlEventReader reader positioned at the point element
   * @return Populated point element.
   * @throws XMLStreamException If could not parse Xml
   * @throws IllegalStateException If encounters Xml it is not expecting
   */
  @Override
  public Point pullElement(final XMLEventReader xmlEventReader,
      final Location parentLocation) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement pointElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.POINT);
    ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.POINT); // consume end

    return attributeParser.parse(ImmutablePoint.builder(),pointElement).build();
  }
}
