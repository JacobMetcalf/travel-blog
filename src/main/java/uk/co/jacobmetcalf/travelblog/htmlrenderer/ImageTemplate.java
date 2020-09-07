package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import org.checkerframework.checker.nullness.qual.NonNull;
import uk.co.jacobmetcalf.travelblog.model.Image;

public class ImageTemplate {

  public final static HtmlView<Image> template =
      DynamicHtml.view(ImageTemplate::imageTemplate).setIndented(false);

  private static void imageTemplate(DynamicHtml<Image> view, Image image) {
    // @formatter:off
    view
      .div()
        .figure()
          .dynamic(f -> f.attrClass(switch (image.getPosition()) {
              case LEFT -> "figure pull-md-left p-r-1 clear-left";
              case RIGHT -> "figure pull-md-right p-l-1 clear-right";
              case NONE -> "figure p-r-1";
            })
            .a().attrTarget("_blank")
              .attrHref("images/" + image.getSrc() + "-large.jpg")
              .img()
                .attrClass("figure-img img-fluid img-rounded")
                .attrSrc("images/" + image.getSrc() + ".jpg")
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
            .__())
        .__()
      .__();
    // @formatter:on
  }

  public void add(@NonNull final StringBuilder builder, @NonNull final Image image) {
    builder.append(template.setIndented(false).render(image));
  }
}
