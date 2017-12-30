package de.lep.rmg.view;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author paul
 * @since 29.12.17.
 */
public class IOInterface {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	private static Scanner scan;
	private static IOListener listener;

	private static Command lastCommand = null;
	private static ValueParam[] lastCommandParams = null;

	public static boolean debugMode = false;
	private static SimpleDateFormat format = new SimpleDateFormat("[HH:mm:ss:SSS]");
	public static boolean running = true;
	private static List<Command> commands = new ArrayList<Command>();

	public static String greeting = "Random Music Generator CLI\n\n" +
			"For a list of available commands enter 'help'";
	public static String[] boolAnswers = {"yes", "true", "y"};


	/**
	 * Initializes the IOInterface.<br>
	 * Has to be executed before {@link IOInterface#run()}
	 */
	public static void init() {
		scan = new Scanner(System.in);
		Command help = new Command("help", "Lists all available commands " +
				"and their description.");
		Command helpC = new Command("helpc",
				"Prints detailed information about given command");
		helpC.addStringParam("command",
				"A command which information will be displayed");
		Command exit = new Command("exit", "Terminates the programm");
		commands.add(help);
		commands.add(helpC);
		commands.add(exit);
	}

	/**
	 * Runs the IOInterface.<br>
	 * It does not start a new thread, so use it as your last line of code.<br>
	 * To stop the Interface set {@link IOInterface#running} to <code>false</code><br>
	 *
	 * Will call {@link IOListener} if a
	 * registered {@link Command} is entered.<br>
	 * Will call {@link IOListener#close()} if it gets closed.<br>
	 */
	public static void run() {
		System.out.println(greeting);
		while (running) {
			String in = waitForCommand();
			if (processCommand(in) && listener != null) {
				listener.command(lastCommand, lastCommandParams);
			}
		}
		System.out.println("Terminating");
		close();
		System.out.println("Bye");
	}

	/**
	 * Sets a Listener for the Interface which will get called if a Command is entered or
	 * the Interface gets closed
	 * @param listener The Listener for the Interface
	 */
	public static void setListener(IOListener listener) {
		IOInterface.listener = listener;
	}

	private static boolean processCommand(String line) {
		// Split Command Parts (" Does not yet have any effect)
		String[] parts = line.split(" ");
		String commandStr = parts[0];

		// Get the command
		Command comm = null;
		for (Command command : commands) {
			if (command.getName().equals(commandStr)) {
				comm = command;
			}
		}
		if (comm == null) {
			printError("Invalid command.");
			printHelpMessage();
			return false;
		}

		// Check Params length
		if (parts.length-1 != comm.getParams().size()) {
			printError("Invalid number of params.");
			printHelpMessage(comm.getName());
			return false;
		}

		// Check/Convert params
		ValueParam[] valueParams = new ValueParam[parts.length-1];
		for (int i = 1; i < parts.length; i++) {
			CommandParam cparam = comm.getParams().get(i-1);
			String paramStr = parts[i];

			// Check if type is correct
			if (!cparam.fits(paramStr)) {
				printError("Invalid Parameter Type for Parameter " + (i-1));
				printHelpMessage(comm.getName());
				return false;
			}

			// Cast and set param
			ValueParam param = cparam.castValue(paramStr);
			valueParams[i-1] = param;
		}

		// Catch preregistered Commands
		switch (comm.getName()) {
			case "exit":  // Exit terminates the Interface
				running = false;
				return false;
			case "help":  // Help prints all available commands
				if (valueParams.length != 0) {
					printError("help accepts no parameter");
					printHelpMessage("help");
				} else {
					printHelpMessage();
				}
				return false;
			case "helpc":  // Helpc prints the parameters of a command
				if (valueParams.length != 1) {
					printError("helpc accepts only one parameter");
					printHelpMessage("help");
				} else {
					printHelpMessage(valueParams[0].getValueS());
				}
				return false;
			default:  // Else command can be given to the listener
				lastCommand = comm;
				lastCommandParams = valueParams;
				return true;
		}
	}

	private static String waitForCommand() {
		System.out.print(ANSI_BLUE + "# " + ANSI_RESET);
		return getInputString();
	}

	/**
	 * Prints all available commands
	 */
	public static void printHelpMessage() {
		System.out.println("-- Help --\nAvailable Commands " +
				"(For more information enter 'help <command>':\n");
		for (Command comm : commands) {
			System.out.println("\t" + comm.getName() + "\t\t" + comm.getDescription());
		}
	}

	/**
	 * Prints information and a list of params of the given command.
	 *
	 * @param name The command to print information of
	 */
	public static void printHelpMessage(String name) {
		Command comm = null;
		for (Command command : commands) {
			if (command.getName().equals(name)) {
				comm = command;
			}
		}
		if (comm == null) {
			printError("There is no command with the name '" + name + "'");
			printHelpMessage();
			return;
		}

		System.out.println("-- Help: " + name + " --\n");
		System.out.println("\t" + comm.getName() + "\t\t" + comm.getDescription());
		System.out.println("\tParams:\n");
		for (CommandParam param : comm.getParams()) {
			System.out.format("\t%s (%s)\t\t%s\n", param.getName(), param.getType(),
					param.getDescription());
		}
		if (comm.getParams().size() == 0) {
			System.out.println("\t No params");
		}
	}

	/**
	 * Adds new commands
	 * @param commands The commands to add
	 */
	public static void addCommands(List<Command> commands) {
		IOInterface.commands.addAll(commands);
	}

	/**
	 * Prints a simple Message
	 * @param message The Message
	 */
	public static void printMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Prints an Error. (To System.out)<br>
	 * If you want to print to System.err, use {@link IOInterface#printDebugError(String, Object...)}
	 * @param error The Error to print
	 */
	public static void printError(String error) {
		System.out.println(ANSI_RED + "Error: " + error + ANSI_RESET);
	}

	/**
	 * Prints a debug Message and the time if {@link IOInterface#debugMode} is <code>true</code>
	 * @param message The Message to print
	 * @param args Optional Format-Arguments
	 */
	public static void printDebugMessage(String message, Object... args) {
		if (debugMode) {
			System.out.print(ANSI_CYAN_BACKGROUND);
			System.out.print(getTime() + " Message:" + ANSI_RESET + " ");
			System.out.format(message, args);
			System.out.println();
		}
	}

	/**
	 * Prints a debug Warning in yellow
	 * and the time if {@link IOInterface#debugMode} is <code>true</code>
	 * @param message The Warning to print
	 * @param args Optional Format-Arguments
	 */
	public static void printDebugWarning(String message, Object... args) {
		if (debugMode) {
			System.out.print(ANSI_YELLOW_BACKGROUND);
			System.out.print(getTime() + " Warning: ");
			System.out.format(message, args);
			System.out.println(ANSI_RESET);
		}
	}

	/**
	 * Prints a debug Error to System.err
	 * and the time if {@link IOInterface#debugMode} is <code>true</code>
	 * @param message The Error to print
	 * @param args Optional Format-Arguments
	 */
	public static void printDebugError(String message, Object... args) {
		if (debugMode) {
			System.err.print(getTime() + " Error: ");
			System.err.format(message, args);
		}
	}

	private static String getTime() {
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		return format.format(stamp);
	}

	/**
	 * Prints a Select-Dialog.
	 *
	 * @param options The Options
	 * @param message The Message which gets printed before the dialog gets printed
	 * @return A pointer to the options that got selected
	 */
	public static int getInputSelect(String[] options, String message) {
		message += "\n" + toOptionsString(options);
		while (true) {
			int in = getInputRange(0, options.length-1, message);
			if (in >= 0 && in < options.length) {
				return in;
			} else {
				System.out.printf("Error: Your input does not match any options (%s). Try again\n",
						toOptionsString(options));
			}
		}
	}

	/**
	 * Prints a Select-Dialog.
	 *
	 * @param options The Options
	 * @param message The Message which gets printed before the dialog gets printed
	 * @param defaultV The default Value if the input is empty
	 * @return A pointer to the options that got selected
	 */
	public static int getInputSelect(String[] options, String message, int defaultV) {
		message += "\n" + toOptionsString(options);
		while (true) {
			int in = getInputRange(0, options.length-1, message, defaultV);
			if (in >= 0 && in < options.length) {
				return in;
			} else {
				System.out.printf("Error: Your input does not match any options (%s). Try again\n",
						toOptionsString(options));
			}
		}
	}

	/**
	 * Prints a Select-Dialog.
	 *
	 * @param options The Options
	 * @return A pointer to the options that got selected
	 */
	public static int getInputSelect(String[] options) {
		while (true) {
			int in = getInputRange(0, options.length-1);
			if (in >= 0 && in < options.length) {
				return in;
			} else {
				System.out.printf("Error: Your input does not match any options (%s). Try again\n",
						toOptionsString(options));
			}
		}
	}

	private static String toOptionsString(String[] options) {
		// See:
		// https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < options.length; i++) {
			sb.append("\t");
			sb.append(i);
			sb.append(" = ");
			sb.append(options[i]);
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Prints a Range-Dialog (Integer-Input with boundaries)
	 * @param min The minimal Value
	 * @param max The maximal Value
	 * @param message The Message to print
	 * @return The Number which got entered
	 */
	public static int getInputRange(int min, int max, String message) {
		while (true) {
			int in = getInputInt(message);
			if (in < min || in > max) {
				System.out.printf("Error: Your input was out of range (%d - %d). Try again\n",
						min, max);
			} else {
				return in;
			}
		}
	}

	/**
	 * Prints a Range-Dialog (Integer-Input with boundaries)
	 *
	 * @param message The Message to print
	 * @param defaultV The default Value if the input is empty
	 * @return The entered Integer
	 */
	public static int getInputRange(int min, int max, String message, int defaultV) {
		while (true) {
			int in = getInputInt(message, defaultV);
			if (in < min || in > max) {
				System.out.printf("Error: Your input was out of range (%d - %d). Try again\n",
						min, max);
			} else {
				return in;
			}
		}
	}

	/**
	 * Prints a Range-Dialog (Integer-Input with boundaries)
	 * @param min The minimal Value
	 * @param max The maximal Value
	 * @return The Number which got entered
	 */
	public static int getInputRange(int min, int max) {
		while (true) {
			int in = getInputInt();
			if (in < min || in > max) {
				System.out.printf("Error: Your input was out of range (%d - %d). Try again\n",
						min, max);
			} else {
				return in;
			}
		}
	}

	/**
	 * Prints a Number-Dialog for Integer-Numbers
	 * @param message The Message to print
	 * @return The entered Integer
	 */
	public static int getInputInt(String message) {
		while (true) {
			String in = getInputString(message);
			try {
				return Integer.parseInt(in);
			} catch (NumberFormatException e) {
				printError("Your input has to be a number. Try again.");
			}
		}
	}

	/**
	 * Prints a Number-Dialog for Integer-Numbers
	 *
	 * @param message The Message to print
	 * @param defaultV The default Value if the input is empty
	 * @return The entered Integer
	 */
	public static int getInputInt(String message, int defaultV) {
		while (true) {
			String in = getInputString(message, Integer.toString(defaultV));
			try {
				return Integer.parseInt(in);
			} catch (NumberFormatException e) {
				printError("Your input has to be a number. Try again.");
			}
		}
	}

	/**
	 * Prints a Number-Dialog for Integer-Numbers
	 * @return The entered Integer
	 */
	public static int getInputInt() {
		while (true) {
			String in = getInputString();
			try {
				return Integer.parseInt(in);
			} catch (NumberFormatException e) {
				printError("Your input has to be a number. Try again.");
			}
		}
	}

	/**
	 * Prints a Yes-No-Dialog
	 *
	 * @param message The Message to print
	 * @return Yes/No
	 */
	public static boolean getInputBoolean(String message) {
		String in = getInputString(message);
		return toInputBoolean(in);
	}

	/**
	 * Prints a Yes-No-Dialog
	 *
	 * @param message The Message to print
	 * @param defaultV The default Value if the input is empty
	 * @return Yes/No
	 */
	public static boolean getInputBoolean(String message, boolean defaultV) {
		String in = getInputString(message, defaultV ? boolAnswers[0] : "no");
		return toInputBoolean(in);
	}

	/**
	 * Prints a Yes-No-Dialog
	 *
	 * @return Yes/No
	 */
	public static boolean getInputBoolean() {
		String in = getInputString();
		return toInputBoolean(in);
	}

	private static boolean toInputBoolean(String in) {
		in = in.toLowerCase();
		for (String ans : boolAnswers) {
			if (ans.equals(in)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Print a simple Input-Dialog
	 *
	 * @param message The message to print
	 * @return The entered String
	 */
	public static String getInputString(String message) {
		System.out.println(ANSI_GREEN + "-> " + ANSI_RESET + message);
		System.out.print(ANSI_GREEN + ">>> " + ANSI_RESET);
		return getInputString();
	}

	/**
	 * Print a simple Input-Dialog
	 *
	 * @param message The message to print
	 * @param defaultV The default Value if the input is empty
	 * @return The entered String
	 */
	public static String getInputString(String message, String defaultV) {
		System.out.format("%s->%s %s (Default: %s)",
				ANSI_GREEN, ANSI_RESET, message, defaultV);
		System.out.print(ANSI_GREEN + ">>> " + ANSI_RESET);
		String in = getInputString();
		if (in.equals("")) {
			return defaultV;
		} else {
			return in;
		}
	}

	/**
	 * Print a simple Input-Dialog
	 *
	 * @return The entered String
	 */
	public static String getInputString() {
		if (scan == null) {
			throw new NullPointerException("IOInterface not initialized");
		}
		return scan.nextLine();
	}

	private static void close() {
		if (listener != null) {
			listener.close();
			if (running) {
				return;
			}
		}
		if (scan == null) {
			throw new NullPointerException("IOInterface not initialized");
		}
		scan.close();
	}

	private static boolean fitsType(int type, String value) {
		switch (type) {
			case ValueParam.V_INT:
				try {
					Integer.parseInt(value);
				} catch(NumberFormatException e) {
					return false;
				}
				return true;
			case ValueParam.V_FLOAT:
				try {
					Float.parseFloat(value);
				} catch(NumberFormatException e) {
					return false;
				}
				return true;
			default:
				return true;
		}
	}

	/**
	 * A Interface which gets called if a command is entered or the Session gets closed
	 */
	public interface IOListener {
		void command(Command command, ValueParam[] params);
		void close();
	}

	/**
	 * A Command for the IOInterface.<br>
	 * Saves the Name, Description and Params of the Command.
	 */
	public static class Command {

		private String name;
		private String description;
		private List<CommandParam> params = new ArrayList<>();


		public Command(String name, String description, List<CommandParam> params) {
			this.name = name;
			this.description = description;
			this.params = params;
		}

		public Command(String name, String description) {
			this.name = name;
			this.description = description;
		}

		public void addIntegerParam(String name, String description) {
			params.add(new CommandParam(ValueParam.V_INT, name, description));
		}

		public void addFloatParam(String name, String description) {
			params.add(new CommandParam(ValueParam.V_FLOAT, name, description));
		}

		public void addStringParam(String name, String description) {
			params.add(new CommandParam(ValueParam.V_STRING, name, description));
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public List<CommandParam> getParams() {
			return params;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Command) {
				return name.equals(((Command) o).getName());
			}
			return super.equals(o);
		}
	}

	private static class CommandParam {

		private int type;
		private String name;
		private String description;


		private CommandParam(int type, String name, String description) {
			this.name = name;
			this.description = description;
			this.type = type;
		}

		private boolean fits(String param) {
			return fitsType(type, param);
		}

		private ValueParam castValue(String value) {
			return new ValueParam(type, value);
		}

		public String getType() {
			switch (type) {
				case ValueParam.V_INT:
					return "Integer";
				case ValueParam.V_FLOAT:
					return "Float";
				default:
					return "String";
			}
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}
	}

	/**
	 * A Param of a Command.<br>
	 * Value is stored in valueI, valueF or valueS, depending on its type
	 */
	public static class ValueParam {

		public static final int V_INT = 1, V_FLOAT = 2, V_STRING = 3;

		private int type;
		private int valueI;
		private float valueF;
		private String valueS;


		public ValueParam(int type, String value) {
			this.type = type;
			switch (type) {
				case V_INT:
					valueI = Integer.parseInt(value);
					break;
				case V_FLOAT:
					valueF = Float.parseFloat(value);
					break;
				default:
					valueS = value;
					type = V_STRING;
			}
		}

		public int getValueI() {
			if (type == V_INT)
				return valueI;
			throw new NullPointerException("Illegal Method-call: Value is not an Integer");
		}

		public float getValueF() {
			if (type == V_FLOAT)
				return valueF;
			throw new NullPointerException("Illegal Method-call: Value is not a Float");
		}

		public String getValueS() {
			if (type == V_STRING)
				return valueS;
			throw new NullPointerException("Illegal Method-call: Value is not a String");
		}
	}
}
