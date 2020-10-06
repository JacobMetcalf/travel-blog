package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.Book;
import uk.co.jacobmetcalf.travelblog.model.ImmutableBook;

/**
 * Slightly different pattern in that it pulls until it has run out of books and then
 * exits. Uses a peek so will not start consuming the next element.
 */
public class BookPullParser {

  private final AttributeParser<ImmutableBook.Builder> attributeParser =
      AttributeParser.<ImmutableBook.Builder>builder()
          .withElementToken(ElementToken.BOOK)
          .put(AttributeToken.ISIN, (b, a) -> b.isin(a.getValue()))
          .put(AttributeToken.TITLE, (b, a) -> b.title(a.getValue()))
          .build();

  public List<Book> pullBooks(final XMLEventReader xmlEventReader) throws XMLStreamException {
    ImmutableList.Builder<Book> listBuilder = ImmutableList.builder();
    while (ElementToken.fromEventName(xmlEventReader.peek()) == ElementToken.BOOK) {
      StartElement bookElement = ElementToken
          .asStartElement(xmlEventReader.nextEvent(), ElementToken.BOOK);
      ImmutableBook.Builder bookBuilder = ImmutableBook.builder();
      attributeParser.parse(bookBuilder, bookElement);
      listBuilder.add(bookBuilder.build());
      ElementToken.checkEndElement(xmlEventReader.nextEvent(), ElementToken.BOOK);
    }
    return listBuilder.build();
  }
}
