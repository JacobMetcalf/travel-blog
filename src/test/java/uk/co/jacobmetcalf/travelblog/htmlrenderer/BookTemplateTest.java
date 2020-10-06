package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static uk.co.jacobmetcalf.travelblog.model.TestData.BOOK_1;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.xmlet.htmlapifaster.Body;

public class BookTemplateTest {

  @Test
  public void can_render_book() {
    final BookTemplate unit = new BookTemplate(ImmutableList.of(BOOK_1), "test-tag");
    String actualHtml = TestHelper.<Body>renderInDiv(unit::add);

    assertThat(actualHtml, containsString("Test book"));
    assertThat(actualHtml, containsString("/123ABC?tag=test-tag"));
  }
}
