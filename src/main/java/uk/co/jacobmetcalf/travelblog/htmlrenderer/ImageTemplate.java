package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.function.Consumer;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Figure;
import uk.co.jacobmetcalf.travelblog.model.Image;

/**
 * Template for rendering an image.
 */
public class ImageTemplate {

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent, final Image image) {
    // @formatter:off
    return parent
        .figure()
          .of(addAlignmentAttribute(image.getPosition()))
          .a().attrTarget("_blank")
            .attrHref("images/" + image.getSrc() + "-large.jpg")
            .img()
              .attrSrc("images/" + image.getSrc() + ".jpg")
              .attrClass("figure-img img-fluid rounded")
              .attrTitle(image.getTitle() + ", "
                + LocationTemplate.formatLocatable(image))
              .attrStyle("border: 0")
            .__()
          .__()
          .figcaption()
            .attrClass("figure-caption")
            .text(image.getTitle())
            .br().__()
            .text("(" + LocationTemplate.formatLocatable(image) + ")")
          .__()
        .__();
    // @formatter:on
  }

  @SuppressWarnings("UnnecessaryParentheses") //https://github.com/google/error-prone/issues/1647
  private <T extends Element<T,?>> Consumer<Figure<Div<T>>> addAlignmentAttribute(final Image.Position position) {
    final String attrClass = switch (position) {
      case LEFT -> "figure float-md-left pr-3 clear-left";
      case RIGHT -> "figure float-md-right pl-3 clear-right";
      case NONE -> "figure pr-3";
    };
    return f -> f.attrClass(attrClass);
  }
}
