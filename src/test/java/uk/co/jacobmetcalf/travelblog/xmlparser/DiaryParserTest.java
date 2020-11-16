package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class DiaryParserTest {

  @Test
  public void can_parse_example_file() throws XMLStreamException {
    InputStream inputStream = this.getClass().getResourceAsStream("diary.xml");
    DiaryParser parser = new DiaryParser();

    long numEntries = parser.parse("input.xml", inputStream).getEntriesAndRoutes().count();
    assertThat(numEntries, Matchers.equalTo(23L));

//    Diary diary = parser.parse("ecuador/diary.xml", inputStream);
//
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    SimpleElementWriter writer = new SimpleElementWriter(new PrintStream(out));
//
//    new DiaryTemplate(diary, "xxx", "jacobmetcalf-21", writer).render();
//    String actualHtml = out.toString();
//    assertThat(actualHtml, Matchers.containsString("Quito"));
  }
}
