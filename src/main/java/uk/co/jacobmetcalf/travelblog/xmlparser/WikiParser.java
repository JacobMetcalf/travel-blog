package uk.co.jacobmetcalf.travelblog.xmlparser;

import com.google.common.base.Preconditions;
import com.google.common.collect.Streams;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import uk.co.jacobmetcalf.travelblog.model.ImmutableLocation.Builder;
import uk.co.jacobmetcalf.travelblog.model.ImmutableWikiRef;
import uk.co.jacobmetcalf.travelblog.model.WikiRef;

/**
 * Pulls a &lt;wiki&gt; element and the collection children from the event reader.
 */
public class WikiParser implements ElementPullParser<WikiRef> {

  private final StringPullParser stringPullParser = new StringPullParser();

  @Override
  public WikiRef pullElement(XMLEventReader xmlEventReader, Builder parentLocation)
      throws XMLStreamException {

    Preconditions.checkArgument(xmlEventReader.hasNext());
    StartElement wikiElement = ElementToken
        .asStartElement(xmlEventReader.nextEvent(), ElementToken.WIKI);

    final ImmutableWikiRef.Builder wikiBuilder = ImmutableWikiRef.builder();
    Streams.stream(wikiElement.getAttributes())
        .forEach( a -> {
          AttributeToken attributeToken = AttributeToken.fromAttributeName(a);
          switch (attributeToken) {
            case REF: wikiBuilder.ref(a.getValue()); break;
            default: throw new IllegalStateException("Unexpected attribute: "
                + a.getName() + ", expected REF");
          }});

    wikiBuilder.text(stringPullParser.pullString(xmlEventReader, ElementToken.WIKI));
    ElementToken.asEndElement(xmlEventReader.nextEvent(), ElementToken.WIKI); // consume end
    return wikiBuilder.build();
  }
}
