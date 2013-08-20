package isard.steam.interp;

import isard.steam.eval.Environment;
import isard.steam.eval.Evaluator;
import isard.steam.eval.Value;
import isard.steam.parse.ParseObject;
import isard.steam.parse.Parser;
import isard.steam.parse.ParseState;
import isard.steam.token.Token;
import isard.steam.token.Tokenizer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Interpreter {

	private static final String CORE_API_FILE_NAME = "CoreAPI.st";
	
	private Evaluator evaluator = new Evaluator();
	private Parser parser = new Parser();
	private Value lastValue = Value.NIL;
	
	public void interpret(String code, Stack<Environment> envStack) {
		List<Token> tokens = new Tokenizer().tokenize(code);
		LinkedList<ParseObject> parserOutput = new LinkedList<ParseObject>();
		for (Token token : tokens) {
			parser.parse(token, parserOutput);
			if (ParseState.NO_SEXPRS_BUFFERED == parser.getParseState()) {
				lastValue = evaluator.evaluate(parserOutput, envStack);
			}
			parserOutput.clear();
		}
	}
	
	public ParseState getParserState() {return parser.getParseState();}
	public Value getLastValue() {return lastValue;}
	
	private String loadCoreAPI() {
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(CORE_API_FILE_NAME);
			byte [] core_api_code = new byte[20000];
			is.read(core_api_code);
			return new String(core_api_code);
		} catch (Throwable t) {
			String msg = "Unable to load core steam API code.";
			throw new RuntimeException(msg, t);
		} finally {
			try {
				is.close();
			} catch (Throwable t) {
				// NO-OP
			}
		}
	}
}
