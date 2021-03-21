module uk.co.jacobmetcalf.travelblog {
  opens uk.co.jacobmetcalf.travelblog to jcommander;
  requires java.xml;
  requires com.google.common;
  requires htmlApiFaster;
  requires jcommander;
  requires org.apache.commons.io;
  requires org.slf4j;
  requires static org.checkerframework.checker.qual;
  requires static org.immutables.value;
}
