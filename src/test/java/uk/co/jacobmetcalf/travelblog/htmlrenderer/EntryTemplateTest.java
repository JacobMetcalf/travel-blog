package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Entry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntry;
import uk.co.jacobmetcalf.travelblog.model.ImmutableParagraph;

public class EntryTemplateTest {

  @Test
  public void renders_entry() {
    String actualHtml = test_rendering(
        ImmutableEntry.builder()
          .date(TestData.JUL_19)
          .location(TestData.LOCATION_1)
          .addParagraphs(ImmutableParagraph.builder()
              .addParts(TestData.TEXT_1).build())
          .addParagraphs(ImmutableParagraph.builder()
                .addParts(TestData.TEXT_3).build())
          .build());

    assertThat(actualHtml, Matchers.containsString("Thursday, 19 July 2018"));
    // We need to see the country and province on the entry header
    assertThat(actualHtml, Matchers.containsString("Quito, Pichincha, Ecuador"));
  }

  @Test
  public void test_date_format() {
    assertThat(EntryTemplate.formatDate(TestData.JUL_19), equalTo("Thursday, 19 July 2018"));
  }

  private String test_rendering(Entry entry) {
     return EntryTemplate.template.render(entry);
  }
}