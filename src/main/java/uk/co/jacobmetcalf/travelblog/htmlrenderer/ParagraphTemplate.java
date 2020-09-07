package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.P;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;
import uk.co.jacobmetcalf.travelblog.model.ParagraphPart;
import uk.co.jacobmetcalf.travelblog.model.Text;

/**
 * Template for rendering a paragraph.
 */
@SuppressWarnings("rawtypes")
public class ParagraphTemplate {

  public final static HtmlView<Paragraph> template =
      DynamicHtml.view(ParagraphTemplate::paragraphTemplate).setIndented(false);

  private static void paragraphTemplate(final DynamicHtml<Paragraph> view, final Paragraph paragraph) {
    // @formatter:off
    view.div()
          .dynamic(d ->
             d.of(addImages(paragraph))
                // Now the remaining text elements
               .p()
                 .of(p -> {
                   PartTemplateVisitor<Div<HtmlView>> visitor = new PartTemplateVisitor<>(p);
                   paragraph.getParts().forEach(i -> i.visit(visitor));
                 })
               .__())
        .__();
    // @formatter:on
  }

  private static Consumer<Div<HtmlView>> addImages(final Paragraph paragraph) {
    return div -> {
        boolean someImages = !paragraph.getImages().isEmpty();
        boolean justImages = paragraph.getParts().isEmpty();
        if (someImages) {
          div.div()
              .of(d3 -> {
                if (justImages) {
                  d3.attrClass("clearfix");
                }
              })
              .of(d3 -> paragraph.getImages().forEach(
                  i -> div.getParent().addPartial(ImageTemplate.template, i)))
              .__();
        }
      };
    }

  static class PartTemplateVisitor<Z extends Element> implements ParagraphPart.Visitor {

    private final P<Z> paragraph;

    PartTemplateVisitor(final @NonNull P<Z> paragraph) {
      this.paragraph = paragraph;
    }

    @Override
    public void visit(final @NonNull Anchor anchor) {
      AnchorTemplate.add(paragraph, anchor);
    }

    @Override
    public void visit(final @NonNull Location location) {
      LocationTemplate.addLocation(paragraph, location, false);
    }

    @Override
    public void visit(final @NonNull Text text) {
      paragraph.text(text.getText());
    }
  }
}
