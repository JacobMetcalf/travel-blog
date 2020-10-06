package uk.co.jacobmetcalf.travelblog.xmlparser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.jacobmetcalf.travelblog.model.Anchor;
import uk.co.jacobmetcalf.travelblog.model.TestData;

public class AnchorParserTest {

  public final AnchorParser unit  = new AnchorParser(ElementToken.WIKI, AnchorParser.WIKIPEDIA_BASE);
  public final AnchorParser unit2 = new AnchorParser();

  @Test
  public void can_parse_valid_wiki_element() {
    String inputXml = "<wiki ref=\"New_Cathedral_of_Cuenca\">"
        + "Cathedral of the Immaculate Conception</wiki>";

    Anchor actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);

    assertThat(actual.getRef(),  equalTo("https://en.wikipedia.org/wiki/New_Cathedral_of_Cuenca"));
    assertThat(actual.getText(), equalTo("Cathedral of the Immaculate Conception"));
  }

  @Test
  public void can_parse_valid_anchor_element() {
    String inputXml = "<a href=\"https://www.royensoc.co.uk/identifying-insects\">Royal Entomological Society</a>";

    Anchor actual = TestHelper.tryParse(inputXml, unit2, TestData.QUITO);

    assertThat(actual.getRef(),  equalTo("https://www.royensoc.co.uk/identifying-insects"));
    assertThat(actual.getText(), equalTo("Royal Entomological Society"));
  }

  @Test
  public void can_parse_valid_wiki_element_with_entity_refs() {
    String inputXml = "<wiki ref=\"P&#225;ramo\">p&#225;ramo</wiki>";
    Anchor actual = TestHelper.tryParse(inputXml, unit, TestData.QUITO);

    assertThat(actual.getRef(),  equalTo("https://en.wikipedia.org/wiki/Páramo"));
    assertThat(actual.getText(), equalTo("páramo"));
  }

  @Test
  public void throws_if_nested_element() {
    String inputXml = "<wiki ref=\"abc\">abc<image/></wiki>";
    Assertions.assertThrows(IllegalStateException.class,
        () -> TestHelper.tryParse(inputXml, unit, TestData.QUITO));
  }

  @Test
  public void throws_if_missing_ref() {
    String inputXml = "<wiki>Invalid</wiki>";
    Assertions.assertThrows(IllegalStateException.class,
        () -> TestHelper.tryParse(inputXml, unit, TestData.QUITO));
  }
}
