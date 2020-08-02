package uk.co.jacobmetcalf.travelblog.generator;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class Main {

  public static void main(String[] args) throws XMLStreamException {

    try (final InputStream input = Preconditions.checkNotNull(
        Main.class.getResourceAsStream("diary.xml"), "Could not load xml file")) {


    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}