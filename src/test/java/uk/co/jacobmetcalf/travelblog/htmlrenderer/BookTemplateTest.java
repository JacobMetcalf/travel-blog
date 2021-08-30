package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static uk.co.jacobmetcalf.travelblog.model.TestData.BOOK_1;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.ImmutableProperties;
import uk.co.jacobmetcalf.travelblog.model.Properties;

public class BookTemplateTest {

  @Test
  public void can_render_book() {
    final BookTemplate unit = new BookTemplate(ImmutableList.of(BOOK_1),
        Properties.of(Properties.Key.AMAZON_ASSOCIATES_ID, "test-tag"));
    String actualHtml = TestHelper.renderInDiv(d -> {
      unit.add(d);
    });

    assertThat(actualHtml, containsString("Test book"));
    assertThat(actualHtml, containsString("/123ABC?tag=test-tag"));
  }

  @Test
  public void can_render_book_with_no_amazon() {
    final BookTemplate unit = new BookTemplate(ImmutableList.of(BOOK_1),
        ImmutableProperties.builder().build());
    String actualHtml = TestHelper.renderInDiv(d -> {
      unit.add(d);
    });

    assertThat(actualHtml, not(containsString("tag")));
  }
}
