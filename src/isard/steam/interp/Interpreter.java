package isard.steam.interp;

import isard.steam.eval.Environment;
import isard.steam.eval.Evaluator;
import isard.steam.eval.Value;
import isard.steam.parse.Parser;
import isard.steam.token.Tokenizer;

import java.util.Stack;

public class Interpreter {
	
	public Value interpret(String code, Stack<Environment> envStack) {
		Evaluator evaluator = new Evaluator();
		Parser parser = new Parser();
		Tokenizer tokenizer = new Tokenizer();
		return evaluator.evaluate(parser.parse(tokenizer.tokenize(code)), envStack);
	}
	
}
