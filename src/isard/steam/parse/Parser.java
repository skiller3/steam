package isard.steam.parse;

import isard.steam.token.Token;

import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Parser {
	
	private StateMachine stateMachine = new StateMachine();
	private ParseState parseState = ParseState.NO_TOKENS_BUFFERED;
	
	public Parser() {}
	
	public void parse(List<Token> tokens, Queue<? extends ParseObject> outputQueue) {
		for (Token token : tokens) parse(token, outputQueue);
	}
	
	public ParseState getParseState() {
		return parseState;
	}
	
	@SuppressWarnings("unchecked")
	public void parse(Token token, Queue<? extends ParseObject> outputQueue) {
		ParseObject parseObject = stateMachine.processNext(token);
		if (parseObject != null) {
			((Queue<ParseObject>)outputQueue).add(parseObject);
			parseState = ParseState.NO_TOKENS_BUFFERED;
		}
		else {
			parseState = ParseState.TOKENS_BUFFERED;
		}
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
		
		private Stack<SExprSimple> sExprStack;
		
		public StateMachine() {
			this.sExprStack = new Stack<SExprSimple>();
		}
		
		public ParseObject processNext(Token token) {
			
			ParseObject retObject = null;
			
			switch(token.getType()) {
				case COMMENT: 
					Comment comment = new Comment(token);
					if (sExprStack.size() > 0) 
						sExprStack.peek().addPart(comment);
					else 
						retObject = comment;
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
						retObject = sExpr;
					break;
				case DOUBLE_QUOTE_STRING_LITERAL:
					if (sExprStack.size() < 1) retObject = new Symbol(token);
					else sExprStack.peek().addPart(new Symbol(token));
					break;
				case SINGLE_QUOTE_STRING_LITERAL:
					if (sExprStack.size() < 1) retObject = new Symbol(token);
					else sExprStack.peek().addPart(new Symbol(token));
					break;
				default:
					if (sExprStack.size() < 1) retObject = new Symbol(token);
					else sExprStack.peek().addPart(new Symbol(token));
					break;
			}
			
			return retObject;
		}
	}
	
	
	
}
