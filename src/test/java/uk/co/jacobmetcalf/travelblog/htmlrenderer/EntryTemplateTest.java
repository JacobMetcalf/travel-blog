package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.xmlet.htmlapifaster.Body;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class EntryTemplateTest {

  @Test
  public void renders_two_textual_paragraphs() {

    String actualHtml = TestHelper.<Body>renderInDiv(d -> new EntryTemplate().add(d, TestData.ENTRY_1));

    // We need to see the country and province on the entry header
    assert_closes_tags(actualHtml);
    assertThat(actualHtml, Matchers.containsString("Thursday, 19 July 2018"));
    assertThat(actualHtml, Matchers.containsString("Quito, Pichincha, Ecuador"));
    assertThat(actualHtml, Matchers.containsString("Start"));
    assertThat(actualHtml, Matchers.containsString("end"));
  }

  @Test
  public void renders_an_image_in_a_paragraph() {
    Entry input = ImmutableEntry.builder()
          .date(TestData.JUL_19)
          .location(TestData.QUITO)
          .addParagraphs(ImmutableParagraph.builder()
              .addImages(TestData.IMAGE_1)
              .addParts(TestData.TEXT_1).build())
          .addParagraphs(ImmutableParagraph.builder()
                .addParts(TestData.TEXT_3).build())
          .build();

    String actualHtml = TestHelper.<Body>renderInDiv(d -> new EntryTemplate().add(d, input));

    assert_closes_tags(actualHtml);
    assertThat(actualHtml, Matchers.containsString("Start"));
    assertThat(actualHtml, Matchers.containsString("end"));
  }

  @Test
  public void test_date_format() {
    assertThat(EntryTemplate.formatDate(TestData.JUL_19), equalTo("Thursday, 19 July 2018"));
  }

  /**
   * Just tests a subset of valid HTML.
   *
   * This stems from an actual problem I was having with HTML flow where I had two dynamic blocks
   * in the paragraph template and as a result it was not closing out one of the div tags properly.
   */
  private void assert_closes_tags(final String actualHtml) {
    assertTrue(actualHtml.matches("([^<>]*<[^<>]*>)*\\s*"),() -> "Invalid HTML: " + actualHtml);
  }
}
