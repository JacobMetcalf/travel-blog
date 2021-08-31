package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.List;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import uk.co.jacobmetcalf.travelblog.model.Book;
import uk.co.jacobmetcalf.travelblog.model.Properties;


/**
 * Template which shows a series of images of books read on the trip and, if you provide an
 * associates key, a link to buy the book on Amazon.
 */
public class BookTemplate {

  public static final String AMAZON_BASE = "https://www.amazon.co.uk/gp/product/";
  private final String associatesTag;
  private final List<Book> books;

  public BookTemplate(final List<Book> books, final Properties properties) {
    this.associatesTag = properties.get(Properties.Key.AMAZON_ASSOCIATES_ID)
        .map(t -> "?tag=" + t).orElse("");
    this.books = books;
  }

  public boolean hasBooks() {
    return !books.isEmpty();
  }

  public <T extends Element<T,?>> void add(final Div<T> parent) {
    parent
        .span().attrClass("d-none d-md-block").text("Books").__()
        .of(d -> books.forEach(b -> addBook(d, b)));
  }

  private <T extends Element<T,?>> void addBook(final Div<T> parent, final Book book) {
    String link = AMAZON_BASE + book.getIsin() + associatesTag;
    parent
        .a().attrTarget("_blank").attrHref(link)
          .img().attrSrc("https://images-na.ssl-images-amazon.com/images/P/"
            + book.getIsin() + ".01.THUMBZZZ.jpg")
            .attrTitle(book.getTitle() + " - click to buy")
            .attrStyle("width:50px;height:75px;margin: 0px 0px 3px 3px;border:0")
          .__()
        .__();
  }
}
