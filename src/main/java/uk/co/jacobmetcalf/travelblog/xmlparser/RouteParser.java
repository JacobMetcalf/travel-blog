package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import uk.co.jacobmetcalf.travelblog.model.EntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.ImmutableRoute;
import uk.co.jacobmetcalf.travelblog.model.Locatable;

/**
 * Pulls a map route in a diary. Returns EntryOrRoute which allows the bulk of the diary
 * to be streamed over.
 */
public class RouteParser implements ElementPullParser<EntryOrRoute> {

  private final PointParser pointParser = new PointParser();

  @Override
  public EntryOrRoute pullElement(final XMLEventReader xmlEventReader,
      final Locatable parentLocatable) throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    ElementToken.asStartElement(xmlEventReader.nextEvent(), ElementToken.ROUTE);

    final ImmutableRoute.Builder routeBuilder = ImmutableRoute.builder();

    boolean open = true;
    while (open && xmlEventReader.hasNext()) {
      final XMLEvent peekedEvent = xmlEventReader.peek();
      if (peekedEvent.isStartElement()) {
        // Then it should be a point
        ElementToken.asStartElement(peekedEvent, ElementToken.POINT);
        routeBuilder.addPoints(pointParser.pullElement(xmlEventReader, parentLocatable));

      } else if (peekedEvent.isEndElement()) {
        // Should be close element of route
        ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.ROUTE);
        open = false;

      } else {
        throw new IllegalStateException("Unexpected event: " + peekedEvent);
      }
    }

    return ImmutableEntryOrRoute.builder()
        .route(routeBuilder.build()).build();
  }
}
