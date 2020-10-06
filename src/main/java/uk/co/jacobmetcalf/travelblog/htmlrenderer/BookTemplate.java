package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.List;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import uk.co.jacobmetcalf.travelblog.model.Book;

public class BookTemplate {
  private final String associatesTag;
  private final List<Book> books;

  public BookTemplate(final List<Book> books, final String associatesTag) {
    this.associatesTag = associatesTag;
    this.books = books;
  }

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent) {
    return parent
        .span().attrClass("d-none d-md-block").text("Books").__()
        .of(d -> books.forEach(b -> addBook(d, b)));
  }

  private <T extends Element<T,?>> Div<T> addBook(final Div<T> parent, final Book book) {
    String link = "https://www.amazon.co.uk/gp/product/" + book.getIsin() + "?tag=" + associatesTag;
    return parent
        .a().attrTarget("_blank").attrHref(link)
          .img().attrSrc("https://images-na.ssl-images-amazon.com/images/P/"
            + book.getIsin() + ".01.THUMBZZZ.jpg")
            .attrTitle(book.getTitle() + " - click to buy")
            .attrStyle("width:50px;height:75px;margin: 0px 0px 3px 3px;border:0")
          .__()
        .__();
  }
}
