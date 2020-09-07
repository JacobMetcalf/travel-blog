package uk.co.jacobmetcalf.travelblog.model;

public interface ParagraphPart {

  void visit(Visitor visitor);

  interface Visitor {
    void visit(Anchor anchor);
    void visit(Location location);
    void visit(Text text);
  }
}
