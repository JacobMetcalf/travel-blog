package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import java.util.List;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

/**
 * Adds a set of navigation links to a footer or header element.
 */
public class NavigationAnchorTemplate {

  private final List<Anchor> navigationAnchors;

  public NavigationAnchorTemplate(final List<Anchor> navigationAnchors) {
    this.navigationAnchors = navigationAnchors;
  }

  public <T extends Element<T,?>> void add(final Ul<T> parent) {
    navigationAnchors.forEach(
        // @formatter:off
        a -> parent
            .li().attrClass("nav-item")
              .a().attrClass("nav-link px-2").attrHref(a.getRef())
                .i().attrClass("fa fa-" + a.getIcon().orElse("question"))
                  .attrTitle(a.getText())
                .__()
              .__()
            .__()
        // @formatter:on
    );
  }
}
