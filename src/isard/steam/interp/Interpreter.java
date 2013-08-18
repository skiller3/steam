package isard.steam.interp;

import isard.steam.eval.Environment;
import isard.steam.eval.Evaluator;
import isard.steam.eval.Value;
import isard.steam.parse.ParseObject;
import isard.steam.parse.Parser;
import isard.steam.parse.ParseState;
import isard.steam.token.Token;
import isard.steam.token.Tokenizer;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Interpreter {
	
	private Evaluator evaluator = new Evaluator();
	private Parser parser = new Parser();
	private Value lastValue = Value.NIL;
	
	public void interpret(String code, Stack<Environment> envStack) {
		List<Token> tokens = new Tokenizer().tokenize(code);
		LinkedList<ParseObject> parserOutput = new LinkedList<ParseObject>();
		for (Token token : tokens) {
			parser.parse(token, parserOutput);
			if (ParseState.NO_TOKENS_BUFFERED == parser.getParseState()) {
				lastValue = evaluator.evaluate(parserOutput, envStack);
			}
			parserOutput.clear();
		}
	}
	
	public ParseState getParserState() {return parser.getParseState();}
	public Value getLastValue() {return lastValue;}
	
}
