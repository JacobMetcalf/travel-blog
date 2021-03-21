package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;
import uk.co.jacobmetcalf.travelblog.model.Diary;

/**
 * Template which renders the navigation bar of the diary page, with various links.
 */
public class NavbarTemplate {

  private final NavigationAnchorTemplate navigationAnchorTemplate;
  private final Diary diary;

  public NavbarTemplate(Diary diary) {
    this.diary = diary;
    this.navigationAnchorTemplate = new NavigationAnchorTemplate(diary.getNavigationAnchors());
  }

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent) {
    // @formatter:off
    return parent
        .nav().attrClass("navbar navbar-dark bg-dark my-3")
          .div().attrClass("navbar-text text-light") // light because we do not want muted
            .text(diary.getTitle())
          .__()
          .ul().attrClass("nav justify-content-end")
            .of(this::addSlideshowLink)
            .of(navigationAnchorTemplate::add)
            .of(this::addHomeLink)
          .__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>> Ul<T> addHomeLink(final Ul<T> parent) {
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
