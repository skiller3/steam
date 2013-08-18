package isard.steam.parse;

import isard.steam.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
	
	public List<LangObject> parse(List<Token> tokens) {
		return new StateMachine(tokens).parse();
	}
	
	@SuppressWarnings("serial")
	public static class ParserException extends RuntimeException {
		public ParserException(String msg, Throwable t) {
			super(msg, t);
		}
		public ParserException(String msg) {
			super(msg);
		}
	}
	
	private static class StateMachine {
		
		private List<Token> tokens;
		private List<LangObject> langObjects;
		private Stack<SExprSimple> sExprStack;
		
		public StateMachine(List<Token> tokens) {
			this.tokens = new ArrayList<Token>(tokens);
			this.langObjects = new ArrayList<LangObject>();
			this.sExprStack = new Stack<SExprSimple>();
		}
		
		public List<LangObject> parse() {
			for (Token token : tokens) process(token);
			return langObjects;
		}
		
		private void process(Token token) {
			switch(token.getType()) {
			case COMMENT: 
				Comment comment = new Comment(token);
				if (sExprStack.size() > 0) 
					sExprStack.peek().addPart(comment);
				else 
					langObjects.add(comment);
				break;
			case OPEN_PARENTHESIS:
				sExprStack.push(new SExprSimple());
				break;
			case CLOSE_PARENTHESIS:
				if (sExprStack.size() < 1) {
					String msg = "Unmatched closing parenthesis encountered";
					throw new ParserException(msg);
				}
				SExprSimple sExpr = sExprStack.pop();
				if (sExprStack.size() > 0) 
					sExprStack.peek().addPart(sExpr);
				else
					langObjects.add(sExpr);
				break;
			case DOUBLE_QUOTE_STRING_LITERAL:
				if (sExprStack.size() < 1) langObjects.add(new Symbol(token));
				else sExprStack.peek().addPart(new Symbol(token));
				break;
			case SINGLE_QUOTE_STRING_LITERAL:
				if (sExprStack.size() < 1) langObjects.add(new Symbol(token));
				else sExprStack.peek().addPart(new Symbol(token));
				break;
			default:
				if (sExprStack.size() < 1) langObjects.add(new Symbol(token));
				else sExprStack.peek().addPart(new Symbol(token));
				break;
			}
			
		}
	}
	
	
	
}
