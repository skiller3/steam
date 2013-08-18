package isard.steam.repl;

import isard.steam.eval.Environment;
import isard.steam.eval.Value;
import isard.steam.interp.Interpreter;

import java.util.Scanner;
import java.util.Stack;

public class SimpleREPL {
	
	private static final String TERMINATE_SIGNAL = "#exit";
	private static final String DELIMITER = "\n";
	
	public static void main(String [] args) {
		Interpreter interpreter = new Interpreter();
		Stack<Environment> envStack = new Stack<Environment>();
		envStack.push(new Environment());
		
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(DELIMITER);
		
		String input = null;
		while (!(input = scanner.next()).startsWith(TERMINATE_SIGNAL)) {
			try {
				Value value = interpreter.interpret(input, envStack);
				System.out.println(String.valueOf(value));
			} catch (Throwable t) {
				t.printStackTrace(System.err);
			}
		}
		System.exit(0);
	}
	
}
