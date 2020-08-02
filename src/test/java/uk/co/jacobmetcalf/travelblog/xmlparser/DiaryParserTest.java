package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.InputStream;
import javax.xml.stream.XMLStreamException;
import org.junit.jupiter.api.Test;

public class DiaryParserTest {

  @Test
  public void can_parse_example_file() throws XMLStreamException {
    InputStream inputStream = this.getClass().getResourceAsStream("diary.xml");
    DiaryParser parser = new DiaryParser();

    long numEntries = parser.parse(inputStream).count();
    assertThat(numEntries, equalTo(17L));
  }
}