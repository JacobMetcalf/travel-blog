package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * Enum of element names plus methods to tokenize an element into a enum value.
 */
public enum ElementToken {
  DIARY,
  ENTRY,
  PARAGRAPH,
  IMAGE,
  LOCATION,
  WIKI,
  A,
  SUMMARY,
  NAVIGATION,
  BOOK,
  ROUTE,
  POINT;

  /**
   * Returns event name as an element.
   * @throws IllegalStateException if not a start or end element
   */
  @SuppressWarnings({"UnnecessaryParentheses", "RedundantSuppression"})
  //https://github.com/google/error-prone/issues/1647
  public static ElementToken fromEventName(final XMLEvent event) {

    String name = switch (event.getEventType()) {
      case XMLEvent.START_ELEMENT -> event.asStartElement().getName().getLocalPart();
      case XMLEvent.END_ELEMENT -> event.asEndElement().getName().getLocalPart();
      default -> throw new IllegalStateException("Unexpected event: " + event);
    };

    Preconditions.checkState(name.toLowerCase().equals(name),
        "Element names should be lower case: " + name);

    return ElementToken.valueOf(name.toUpperCase());
  }

  public static StartElement asStartElement(final XMLEvent event, final ElementToken element) {
    Preconditions.checkState(event.isStartElement() && fromEventName(event) == element,
        "Expected start of element: " + element.name().toLowerCase());
    return event.asStartElement();
  }

  public static void checkEndElement(final XMLEvent event, final ElementToken element) {
    Preconditions.checkState(event.isEndElement() && fromEventName(event) == element,
        "Expected end of element: " + element.name().toLowerCase());
  }
}
