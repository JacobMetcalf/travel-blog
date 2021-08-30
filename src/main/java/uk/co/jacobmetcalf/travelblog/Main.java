package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.JCommander;

/**
 * Main method, calls JCommander then invokes {@link Executor}.
 */
public class Main {

  public static void main(String[] args) {
    Executor executor = new Executor();
    JCommander jc = JCommander.newBuilder()
        .addObject(executor)
        .build();
    jc.parse(args);

    if (executor.isHelp()) {
      jc.usage();
      jc.getConsole().println("Tip: for ease of use put the arguments in a file with key and value "
          + "on alternating lines then just call with: @:\\dir\\my.properties ");
    } else {
      executor.execute();
    }
  }
}
