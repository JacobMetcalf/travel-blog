package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph;
import uk.co.jacobmetcalf.travelblog.model.Paragraph;
import uk.co.jacobmetcalf.travelblog.model.TestData;
import uk.co.jacobmetcalf.travelblog.xmlparser.AnchorParser;

public class ParagraphTemplateTest {

  @Test
  public void renders_a_single_textual_element() {
    final Paragraph input = ImmutableParagraph.builder()
        .addParts(TestData.TEXT_1)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));
    assertThat(actualHtml, containsString("<p>Start</p>"));
  }

  @Test
  public void renders_two_textual_elements() {
    final Paragraph input = ImmutableParagraph.builder()
        .addParts(TestData.TEXT_1, TestData.TEXT_3)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));

    assertThat(actualHtml, containsString("<p>Start end.</p>"));
  }

  @Test
  public void renders_location_in_text() {
    final Paragraph input = ImmutableParagraph.builder()
        .addParts(TestData.TEXT_1, TestData.QUITO, TestData.TEXT_3)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));

    assertThat(actualHtml, startsWith("<div><p>Start"));
    assertThat(actualHtml, endsWith("end.</p></div>"));
    assertThat(actualHtml, containsString("map.setCenter({lat:-0.2181,lng:-78.5084})"));
  }

  @Test
  public void renders_anchor_in_text() {
    final Paragraph input = ImmutableParagraph.builder()
        .addParts(TestData.TEXT_1, TestData.WIKI_LINK, TestData.TEXT_3)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));

    assertThat(actualHtml, startsWith("<div><p>Start"));
    assertThat(actualHtml, endsWith("end.</p></div>"));
    assertThat(actualHtml, containsString("href=\""
        + AnchorParser.WIKIPEDIA_BASE + "Quito\""));
  }

  @Test
  public void wraps_images_without_text_in_clearfix() {

    final Paragraph input = ImmutableParagraph.builder()
        .addImages(TestData.IMAGE_1, TestData.IMAGE_2)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));

    // Just check dynamic elements - in this case we are looking for the clearfix
    assertThat(actualHtml, containsString("<div class=\"clearfix\">"));
    assertThat(actualHtml, containsString("ecuador001"));
    assertThat(actualHtml, containsString("ecuador002"));
  }

  @Test
  public void renders_multiple_parts() {

    final Paragraph input = ImmutableParagraph.builder()
        .addParts(TestData.TEXT_1, TestData.QUITO, TestData.TEXT_2,
            TestData.WIKI_LINK, TestData.TEXT_3)
        .addImages(TestData.IMAGE_1)
        .build();

    String actualHtml = TestHelper.renderInDiv(d -> new ParagraphTemplate().add(d, input));

    assertThat(actualHtml, not(containsString("<div class=\"clearfix\">")));
    assertThat(actualHtml, containsString("<p>Start"));
    assertThat(actualHtml, containsString("src=\"images/ecuador001.jpg\""));
    assertThat(actualHtml, containsString(" middle "));
    assertThat(actualHtml, containsString("href=\""
        + AnchorParser.WIKIPEDIA_BASE + "Quito\""));
    assertThat(actualHtml, containsString("map.setCenter({lat:-0.2181,lng:-78.5084})"));
    assertThat(actualHtml, endsWith("end.</p></div>"));
  }
}
