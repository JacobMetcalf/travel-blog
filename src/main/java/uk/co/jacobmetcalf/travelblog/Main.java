package uk.co.jacobmetcalf.travelblog;

import com.beust.jcommander.JCommander;

/**
 * Main method, calls JCommander then invokes {@link Command}.
 */
public class Main {

  public static void main(String[] args) {
    Command command = new Command();
    JCommander jc = JCommander.newBuilder()
        .addObject(command)
        .build();
    jc.parse(args);

    if (command.isHelp()) {
      jc.usage();
      jc.getConsole().println("Tip: for ease of use put the arguments in a file with key and value "
          + "on alternating lines then just call with: @:\\dir\\my.properties ");
    } else {
      command.execute();
    }
  }
}
