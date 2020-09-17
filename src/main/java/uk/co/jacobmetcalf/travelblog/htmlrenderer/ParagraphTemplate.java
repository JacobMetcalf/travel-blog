package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.function.Consumer;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;

public class ParagraphTemplate {

  private final ImageTemplate imageTemplate = new ImageTemplate();

  /**
   * Adds a paragraph to a div.
   * @param parent The div
   * @param paragraph The contents of the paragraph.
   * @param <Z> Parent element of the div.
   */
  public <Z extends Element<Z,?>> void add(final Div<Z> parent, final Paragraph paragraph) {

    Consumer<P<Div<Z>>> consumer = p -> {
        ParagraphPartVisitor<Div<Z>> visitor = new ParagraphPartVisitor<>(p);
        paragraph.getParts().forEach(i -> i.visit(visitor));
      };

      parent.of(addImages(paragraph))
        // Now the remaining text elements
        .p().of(consumer).__();
    }

  private <Z extends Element<Z,?>>  Consumer<Div<Z>> addImages(final Paragraph paragraph) {
    return div -> {
      boolean someImages = !paragraph.getImages().isEmpty();
      boolean justImages = paragraph.getParts().isEmpty();
      if (someImages) {
        Div<Div<Z>> wrappingDiv = div.div();
        if (justImages) {
          wrappingDiv.attrClass("clearfix");
        }
        paragraph.getImages().forEach(
            i -> imageTemplate.add(wrappingDiv, i));
        wrappingDiv.__();
      }
    };
  }
}
