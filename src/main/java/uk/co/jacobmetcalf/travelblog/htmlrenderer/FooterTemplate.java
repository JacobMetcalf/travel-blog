package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;

/**
 * Template which renders the footer of the diary page, with various links.
 */
public class FooterTemplate {

  private final String linkedInId;

  public FooterTemplate(@Nullable final String linkedInId) {
    this.linkedInId = linkedInId;
  }

  public <T extends Element<T,?>> Body<T> add(final Body<T> parent) {
    // @formatter:off
    return parent
        .footer().attrClass("footer my-3")
          .div().attrClass("container")
            .nav().attrClass("navbar navbar-dark bg-dark")
              .div().attrClass("navbar-brand").__()
              .ul().attrClass("nav justify-content-end")
                .of(this::addLinkedInIcon)
              .__()
            .__()
          .__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>> Ul<T> addLinkedInIcon(final Ul<T> parent) {

    if (linkedInId == null) {
      return parent;
    }

    // @formatter:off
    return parent.li().attrClass("nav-item")
          .a().attrClass("nav-link").attrHref("http://www.linkedin.com/in/" + linkedInId)
            .i().attrClass("fa fa-linkedin-square")
              .attrTitle("View my profile on LinkedIn")
            .__()
          .__()
        .__();
    // @formatter:on
  }
}
