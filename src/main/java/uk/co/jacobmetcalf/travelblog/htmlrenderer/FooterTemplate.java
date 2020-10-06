package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Element;

/**
 * Template which renders the footer of the diary page, with various links.
 */
public class FooterTemplate {

  public <T extends Element<T,?>> Body<T> add(final Body<T> parent) {
    // @formatter:off
    return parent
        .footer().attrClass("footer my-3")
          .div().attrClass("container")
            .nav().attrClass("navbar navbar-dark bg-dark")
              .div().attrClass("navbar-brand").__()
              .ul().attrClass("nav justify-content-end")
                .li().attrClass("nav-item")
                  .a().attrClass("nav-link").attrHref("http://www.linkedin.com/in/jacobmetcalf")
                    .i().attrClass("fa fa-linkedin-square")
                      .attrTitle("View Jacob Metcalf's profile on LinkedIn")
                    .__()
                  .__()
                .__()
              .__()
            .__()
          .__()
        .__();
    // @formatter:on
  }
}
