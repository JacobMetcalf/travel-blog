package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Div;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;
import uk.co.jacobmetcalf.travelblog.model.Diary;

/**
 * Template which renders the navigation bar of the diary page, with various links.
 */
public class NavbarTemplate {

  private final Diary diary;

  public NavbarTemplate(Diary diary) {
    this.diary = diary;
  }

  public <T extends Element<T,?>> Div<T> add(final Div<T> parent) {
    // @formatter:off
    return parent
        .nav().attrClass("navbar navbar-dark bg-dark my-3")
          .div().attrClass("navbar-brand")
            .text(diary.getTitle())
          .__()
          .ul().attrClass("nav justify-content-end")
            .of(this::addSlideshowLink)
            .of(this::addNavigationAnchors)
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

  private <T extends Element<T,?>> Ul<T> addNavigationAnchors(final Ul<T> parent) {
    diary.getNavigationAnchors().forEach(
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
    return parent;
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
