package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.htmlrenderer.DiaryTemplate;
import uk.co.jacobmetcalf.travelblog.model.Diary;

public class DiaryParserTest {

  @Test
  public void can_parse_example_file() throws XMLStreamException {
    InputStream inputStream = this.getClass().getResourceAsStream("diary.xml");
    DiaryParser parser = new DiaryParser();

//    long numEntries = parser.parse(inputStream).count();
//    assertThat(numEntries, equalTo(17L));

    Diary diary = parser.parse(inputStream);
    String actualHtml = DiaryTemplate.template.render(diary);
    assertThat(actualHtml, Matchers.containsString("Quito"));
  }
}