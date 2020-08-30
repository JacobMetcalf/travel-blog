package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.Location;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;
import uk.co.jacobmetcalf.travelblog.model.Text;

/**
 * Template for rendering a paragraph.
 */
public class ParagraphTemplate {

  public final static HtmlView<Paragraph> template =
      DynamicHtml.view(ParagraphTemplate::paragraphTemplate).setIndented(false);

  private static void paragraphTemplate(final DynamicHtml<Paragraph> view, final Paragraph paragraph) {

    boolean justImages = paragraph.getParts().isEmpty();
    ImageTemplate imageTemplate = new ImageTemplate();

    // @formatter:off
    view.div()
          .dynamic(d -> { if (justImages) {
            d.div()
                .attrClass("clearfix")
                .of(d2 -> paragraph.getImages().forEach(
                    i -> view.addPartial(ImageTemplate.template, i)))
            .__();
          } else {
            d.of(d2 -> paragraph.getImages().forEach(
                i -> view.addPartial(ImageTemplate.template, i)));
          }})
          // Now the remaining text elements
          .p()
            .dynamic(p -> paragraph.getParts().forEach(
                //TODO Replace with visitor pattern?
                i -> {
                  if (i instanceof Location) {
                    view.addPartial(LocationTemplate.template, (Location)i);
                  } else if (i instanceof Anchor){
                    view.addPartial(AnchorTemplate.template, (Anchor)i);
                  } else if (i instanceof Text) {
                    p.text(((Text)i).getText());
                  }
                }
              ))
          .__()
        .__();
    // @formatter:on
  }
}
