package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import htmlflow.DynamicHtml;
import htmlflow.HtmlView;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

public class AnchorTemplate {

  public final static HtmlView<Anchor> template =
      DynamicHtml.view(AnchorTemplate::anchorTemplate).setIndented(false);

  private static void anchorTemplate(DynamicHtml<Anchor> view, Anchor anchor) {
    // @formatter:off
    view.div()
          .a()
            .attrTarget("_new")
            .dynamic(a -> a.attrHref(anchor.getRef()))
            .dynamic(a -> a.text(anchor.getText()))
          .__()
        .__();
    // @formatter:on
  }
}
