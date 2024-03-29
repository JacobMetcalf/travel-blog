package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

/**
 * Template which renders the navigation bar of the diary page, with various links.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class NavbarTemplate {

  private final NavigationAnchorTemplate navigationAnchorTemplate;
  private final String title;

  public NavbarTemplate(String title, List<Anchor> anchors) {
    this.title = title;
    this.navigationAnchorTemplate = new NavigationAnchorTemplate(anchors);
  }

  public NavbarTemplate(String title) {
    this.title = title;
    this.navigationAnchorTemplate = new NavigationAnchorTemplate(ImmutableList.of());
  }

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent) {
    // @formatter:off
    return parent
        .nav().attrClass("navbar navbar-dark bg-dark my-3")
          .div().attrClass("navbar-text text-light") // light because we do not want muted
            .text(title)
          .__()
          .ul().attrClass("nav justify-content-end")
            .of(this::addSlideshowLink)
            .of(navigationAnchorTemplate::add)
            .of(this::addHomeLink)
          .__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>>  Ul<T> addHomeLink(final Ul<T> parent) {
    // @formatter:off
    return parent
        .li().attrClass("nav-item")
          .a().attrClass("nav-link px-2").attrHref("../index.html")
            .i().attrClass("fa fa-globe").attrTitle("Return to World Map")
            .__()
          .__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>> Ul<T> addSlideshowLink(final Ul<T> parent) {
    // @formatter:off
    return parent
      .li().attrClass("nav-item")
        .a().attrClass("nav-link px-2").attrOnclick("$('.figure:first').trigger('click');")
          .i().attrClass("fa fa-clone").attrTitle("View slideshow")
          .__()
        .__()
      .__();
    // @formatter:on
  }
}
