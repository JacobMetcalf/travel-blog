package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.JCommander;

public class Main {

  public static void main(String[] args) {
    Executor executor = new Executor();
    JCommander jc = JCommander.newBuilder()
        .addObject(executor)
        .build();
    jc.parse(args);

    if (executor.isHelp()) {
      jc.usage();
    } else {
      executor.execute();
    }
  }
}
