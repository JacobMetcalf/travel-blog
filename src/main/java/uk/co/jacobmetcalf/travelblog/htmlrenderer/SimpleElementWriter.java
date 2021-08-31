package uk.co.jacobmetcalf.travelblog.htmlrenderer;

import com.google.common.collect.ImmutableSet;
import java.io.PrintStream;
import org.xmlet.htmlapifaster.Area;
import org.xmlet.htmlapifaster.Base;
import org.xmlet.htmlapifaster.Br;
import org.xmlet.htmlapifaster.Col;
import org.xmlet.htmlapifaster.Element;
import org.xmlet.htmlapifaster.ElementVisitor;
import org.xmlet.htmlapifaster.Embed;
import org.xmlet.htmlapifaster.Hr;
import org.xmlet.htmlapifaster.Img;
import org.xmlet.htmlapifaster.Input;
import org.xmlet.htmlapifaster.Link;
import org.xmlet.htmlapifaster.Meta;
import org.xmlet.htmlapifaster.Param;
import org.xmlet.htmlapifaster.Source;
import org.xmlet.htmlapifaster.Text;
import org.xmlet.htmlapifaster.Track;
import org.xmlet.htmlapifaster.Wbr;

/**
 * Implementation of xmllet.htmlapifaster.ElementVisitor which does not attempt to
 * cache templates or indent.
 */
public class SimpleElementWriter extends ElementVisitor {

  private static final char BEGIN_TAG = '<';
  private static final String BEGIN_CLOSE_TAG = "</";
  private static final String BEGIN_COMMENT_TAG = "<!-- ";
  private static final String END_COMMENT_TAG = " -->";
  private static final String ATTRIBUTE_MID = "=\"";
  private static final char FINISH_TAG = '>';
  private static final char SPACE = ' ';
  private static final char QUOTATION = '"';
  private static final ImmutableSet<Class<?>> HTML_VOID_ELEMENTS = ImmutableSet.of(
      Area.class, Base.class, Br.class, Col.class, Embed.class, Hr.class, Img.class,
      Input.class, Link.class, Meta.class, Param.class, Source.class, Track.class, Wbr.class);

  private boolean isClosed = true;
  private final PrintStream out;

  public SimpleElementWriter(final PrintStream out) {
    this.out = out;
  }

  @Override
  public void visitElement(final Element element) {
      close();
      printOpenTag(element.getName()); // "<elementName"
      isClosed = false;
  }

  @Override
  public void visitAttribute(final String attributeName, final String attributeValue) {
    printAttribute(attributeName, attributeValue);
  }

  @Override
  public void visitParent(final Element element) {
    close();
    if (!isVoidElement(element)) {
      printCloseTag(element.getName());
    }
  }

  @Override
  public <R> void visitText(Text<? extends Element, R> text) {
    close();
    out.print(text.getValue());
  }

  @Override
  public <R> void visitComment(Text<? extends Element, R> comment) {
    close();
    printComment(comment.getValue());
  }

  /**
   * HTML void elements cannot have end tags. It would have been nige if HtmlApo
   * had provided this as a marker interface.
   */
  private boolean isVoidElement(final Element<?,?> element) {
    return HTML_VOID_ELEMENTS.contains(element.getClass());
  }

  private void close() {
    if (!isClosed) {
      out.print(FINISH_TAG);
      isClosed = true;
    }
  }

  private void printOpenTag(String elementName) {
    out.print(BEGIN_TAG);
    out.print(elementName);
  }

  private void printAttribute(String attributeName, String attributeValue) {
    out.print(SPACE);
    out.print(attributeName);
    out.print(ATTRIBUTE_MID);
    out.print(attributeValue);
    out.print(QUOTATION);
  }

  private void printComment(String comment) {
    out.print(BEGIN_COMMENT_TAG);
    out.print(comment);
    out.print(END_COMMENT_TAG);
  }

  private void printCloseTag(String elementName) {
    out.print(BEGIN_CLOSE_TAG);     // </
    out.print(elementName);         // </name
    out.print(FINISH_TAG);          // </name>
  }
}
