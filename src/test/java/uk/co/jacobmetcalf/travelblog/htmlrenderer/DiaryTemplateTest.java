package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import static org.hamcrest.Matchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Diary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableDiary;
import uk.co.jacobmetcalf.travelblog.model.ImmutableEntryOrRoute;
import uk.co.jacobmetcalf.travelblog.model.Properties;
import uk.co.jacobmetcalf.travelblog.model.Properties.Key;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class DiaryTemplateTest {

  @Test
  public void can_render_diary() {

    Diary diaryOneEntry = ImmutableDiary.builder().from(TestData.DIARY_NO_ENTRIES)
        .entriesAndRoutes(Stream.of(
            ImmutableEntryOrRoute.builder()
                .entry(TestData.ENTRY_1).build())).build();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));

    DiaryTemplate unit = new DiaryTemplate(diaryOneEntry,
        Properties.of(Key.GOOGLE_API_KEY, "my-api-key",
            Key.CANONICAL_URL, "https://mysite.com"), writer);

    unit.render();
    String actualHtml = out.toString();

    // Assert very little, we just want to make sure that the whole assembly can run
    MatcherAssert.assertThat(actualHtml, containsString("jquery.min.js"));
  }
}
