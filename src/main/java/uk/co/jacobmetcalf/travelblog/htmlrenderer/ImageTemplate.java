package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.function.Consumer;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Figure;
import uk.co.jacobmetcalf.travelblog.model.Image;

public class ImageTemplate {
  public <Z extends Element<Z,?>> void add(final Div<Z> parent, final Image image) {

    // @formatter:off
    parent
        .figure()
          .of(addAlignmentAttribute(image.getPosition()))
          .a().attrTarget("_blank")
            .attrHref("images/" + image.getSrc() + "-large.jpg")
            .img()
              .attrSrc("images/" + image.getSrc() + ".jpg")
              .attrClass("figure-img img-fluid img-rounded")
              .attrTitle(image.getTitle() + ", "
                + LocationTemplate.formatLocation(image.getLocation()))
              .attrStyle("border: 0")
            .__()
          .__()
          .figcaption()
            .attrClass("figure-caption")
            .text(image.getTitle())
            .br().__()
            .text("(" + LocationTemplate.formatLocation(image.getLocation()) + ")")
          .__()
        .__();
    // @formatter:on
  }

  public <Z extends Element<Z,?>> Consumer<Figure<Div<Z>>> addAlignmentAttribute(final Image.Position position) {
    return f -> f.attrClass(switch (position) {
      case LEFT -> "figure pull-md-left p-r-1 clear-left";
      case RIGHT -> "figure pull-md-right p-l-1 clear-right";
      case NONE -> "figure p-r-1";
    });
  }
}
