package isard.steam;

import isard.steam.token.Token;
import isard.steam.token.Tokenizer;

import java.util.List;

public class Interpreter {

	public void interpret(String code) {
		List<Token> tokens = tokenize(code);
	}
	
	private static List<Token> tokenize(String code) {
		return new Tokenizer(code).tokenize();
	}
}
