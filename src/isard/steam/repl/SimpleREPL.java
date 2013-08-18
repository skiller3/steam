package isard.steam.repl;

import isard.steam.eval.Environment;
import isard.steam.interp.Interpreter;
import isard.steam.parse.ParseState;

import java.util.Scanner;
import java.util.Stack;

public class SimpleREPL {
	
	private static final String TERMINATE_SIGNAL = "#exit";
	private static final String DELIMITER = "\n";
	private static final String NEW_INPUT_PROMPT = ">>>>";
	private static final String CONT_INPUT_PROMPT = "++++";
	
	public static void main(String [] args) {
		Interpreter interpreter = new Interpreter();
		Stack<Environment> envStack = new Stack<Environment>();
		envStack.push(new Environment());
		
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(DELIMITER);
		
		System.out.println(NEW_INPUT_PROMPT);
		
		String input = null;
		while (!(input = scanner.next()).startsWith(TERMINATE_SIGNAL)) {
			try {
				interpreter.interpret(input, envStack);
				ParseState parseState = interpreter.getParserState();
				switch (parseState) {
					case NO_TOKENS_BUFFERED:
						System.out.println(String.valueOf(interpreter.getLastValue()));
						System.out.println(NEW_INPUT_PROMPT);
						break;
					case TOKENS_BUFFERED:
						System.out.println(CONT_INPUT_PROMPT);
						break;
				}
			} catch (Throwable t) {
				t.printStackTrace(System.err);
				System.out.println(NEW_INPUT_PROMPT);
			}
		}
		
		System.exit(0);
	}
	
}
