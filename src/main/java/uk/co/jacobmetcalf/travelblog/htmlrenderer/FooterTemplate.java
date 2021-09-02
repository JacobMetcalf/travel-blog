package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.Ul;
import uk.co.jacobmetcalf.travelblog.executor.Properties;
import uk.co.jacobmetcalf.travelblog.model.Anchor;

/**
 * Template which renders the footer of the diary page, with various social media links.
 */
@SuppressWarnings("UnusedReturnValue") // We use unused return type to syntactically ensure tags closed
public class FooterTemplate {

  private final Properties properties;
  private final NavigationAnchorTemplate navigationAnchorTemplate;

  public FooterTemplate(final List<Anchor> anchors, final Properties properties) {
    this.properties = properties;
    this.navigationAnchorTemplate = new NavigationAnchorTemplate(anchors);
  }

  public FooterTemplate(final Properties properties) {
    this.properties = properties;
    this.navigationAnchorTemplate = new NavigationAnchorTemplate(ImmutableList.of());
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
                .of(this::addFacebookIcon)
                .of(this::addTwitterIcon)
                .of(this::addGitHubIcon)
                .of(navigationAnchorTemplate::add)
              .__()
            .__()
          .__()
        .__();
    // @formatter:on
  }

  private <T extends Element<T,?>> Ul<T> addLinkedInIcon(final Ul<T> parent) {
    return properties.get(Properties.Key.LINKED_IN).map(
        // @formatter:off
        l -> parent.li().attrClass("nav-item")
            .a().attrClass("nav-link").attrHref("https://linkedin.com/in/" + l)
              .i().attrClass("fa fa-linkedin-square")
                .attrTitle("View my profile on LinkedIn")
              .__()
            .__().__())
        // @formatter:on
        .orElse(parent);
  }

  private <T extends Element<T,?>> Ul<T> addFacebookIcon(final Ul<T> parent) {
    return properties.get(Properties.Key.FACEBOOK).map(
        // @formatter:off
        f -> parent.li().attrClass("nav-item")
            .a().attrClass("nav-link").attrHref("https://facebook.com/" + f)
              .i().attrClass("fa fa-facebook-square")
                .attrTitle("View my profile on Facebook")
              .__()
            .__().__())
        // @formatter:on
        .orElse(parent);
  }

  private <T extends Element<T,?>> Ul<T> addTwitterIcon(final Ul<T> parent) {
    return properties.get(Properties.Key.TWITTER).map(
        // @formatter:off
        f -> parent.li().attrClass("nav-item")
            .a().attrClass("nav-link").attrHref("https://twitter.com/" + f)
              .i().attrClass("fa fa-twitter-square")
                .attrTitle("View my Tweets")
              .__()
            .__().__())
        // @formatter:on
        .orElse(parent);
  }

  private <T extends Element<T,?>> Ul<T> addGitHubIcon(final Ul<T> parent) {
    return properties.get(Properties.Key.GITHUB).map(
        // @formatter:off
        g -> parent.li().attrClass("nav-item")
            .a().attrClass("nav-link").attrHref("https://github.com/" + g)
              .i().attrClass("fa fa-github-square")
                .attrTitle("View my GitHub account")
              .__()
            .__().__())
        // @formatter:on
        .orElse(parent);
  }
}
