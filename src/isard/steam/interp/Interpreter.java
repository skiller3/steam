package isard.steam.interp;

import isard.steam.eval.Environment;
import isard.steam.eval.Evaluator;
import isard.steam.eval.Value;
import isard.steam.parse.ParseObject;
import isard.steam.parse.ParseState;
import isard.steam.parse.Parser;
import isard.steam.token.Token;
import isard.steam.token.Tokenizer;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class Interpreter {

	private static final String CORE_API_FILE_NAME = "CoreAPI.st";
	
	private Evaluator evaluator = new Evaluator();
	private Parser parser = new Parser();
	private Value lastValue = Value.NIL;
	private Environment rootEnv = new Environment();
	
	public Interpreter() {loadCoreAPI();}
	
	public void interpret(String code, LinkedList<Environment> envStack) {
		envStack = new LinkedList<Environment>(envStack);
		envStack.addLast(rootEnv);
		
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
	
	private void loadCoreAPI() {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(CORE_API_FILE_NAME);
			StringBuilder buf = new StringBuilder();
			int next = -1;
			while ((next = is.read()) != -1) {
				buf.append((char)next);
			}
			
			interpret(buf.toString(), new LinkedList<Environment>());
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
