package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.WikiRef;

public class WikiParserTest {
  public WikiParser unit = new WikiParser();

  @Test
  public void can_parse_valid_wiki_element() {
    String inputXml = "<wiki ref=\"New_Cathedral_of_Cuenca\">"
        + "Cathedral of the Immaculate Conception</wiki>";

    WikiRef actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    assertThat(actual.getRef(),  equalTo("New_Cathedral_of_Cuenca"));
    assertThat(actual.getText(), equalTo("Cathedral of the Immaculate Conception"));
  }

  @Test
  public void can_parse_valid_wiki_element_with_entity_refs() {
    String inputXml = "<wiki ref=\"P&#225;ramo\">p&#225;ramo</wiki>";
    WikiRef actual = TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder());

    assertThat(actual.getRef(),  equalTo("Páramo"));
    assertThat(actual.getText(), equalTo("páramo"));
  }

  @Test
  public void throws_if_nested_element() {
    String inputXml = "<wiki ref=\"abc\">abc<image/></wiki>";
    Assertions.assertThrows(IllegalStateException.class,
        () -> TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder()));
  }

  @Test
  public void throws_if_missing_ref() {
    String inputXml = "<wiki>Invalid</wiki>";
    Assertions.assertThrows(IllegalStateException.class,
        () -> TestUtil.tryParse(inputXml, unit, TestUtil.quitoAsBuilder()));
  }
}