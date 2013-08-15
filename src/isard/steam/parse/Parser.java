package isard.steam.parse;

import isard.steam.Utils;
import isard.steam.token.Token;
import isard.steam.token.TokenType;

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
	
	private static class SExprImpl implements SExpr {
		private List<LangObject> parts = new ArrayList<LangObject>();
		public SExprImpl() {}
		@Override
		public List<LangObject> getParts() {
			return parts;
		}
		@Override
		public String getText() {
			return Utils.nicelyFormat(parts);
		}
		@Override
		public List<Token> getTokens() {
			List<Token> tokens = new ArrayList<Token>();
			tokens.add(new Token("(", TokenType.OPEN_PARENTHESIS));
			for (LangObject langObject : parts) {
				tokens.addAll(langObject.getTokens());
			}
			tokens.add(new Token(")", TokenType.CLOSE_PARENTHESIS));
			return tokens;
		}
	}
	
	private static class StateMachine {
		
		private List<Token> tokens;
		private List<LangObject> langObjects;
		private Stack<SExprImpl> sExprStack;
		
		public StateMachine(List<Token> tokens) {
			this.tokens = new ArrayList<Token>(tokens);
			this.langObjects = new ArrayList<LangObject>();
			this.sExprStack = new Stack<SExprImpl>();
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
					sExprStack.peek().parts.add(comment);
				else 
					langObjects.add(comment);
				break;
			case OPEN_PARENTHESIS:
				sExprStack.push(new SExprImpl());
				break;
			case CLOSE_PARENTHESIS:
				if (sExprStack.size() < 1) {
					String msg = "Unmatched closing parenthesis encountered";
					throw new ParserException(msg);
				}
				SExprImpl sExpr = sExprStack.pop();
				if (sExprStack.size() > 0) 
					sExprStack.peek().parts.add(sExpr);
				else
					langObjects.add(sExpr);
				break;
			case DOUBLE_QUOTE_STRING_LITERAL:
				if (sExprStack.size() < 1) {
					String msg = "String literal " + token.getCode() + " not embedded in " +
							"valid expression";
					throw new ParserException(msg);
				}
				sExprStack.peek().parts.add(new Symbol(token));
				break;
			case SINGLE_QUOTE_STRING_LITERAL:
				if (sExprStack.size() < 1) {
					String msg = "String literal " + token.getCode() + " not embedded in " +
							"valid expression";
					throw new ParserException(msg);
				}
				sExprStack.peek().parts.add(new Symbol(token));
				break;
			default:
				if (sExprStack.size() < 1) {
					String msg = "Symbol " + token.getCode() + " not embedded in " +
							"valid expression";
					throw new ParserException(msg);
				}
				sExprStack.peek().parts.add(new Symbol(token));
				break;
			}
			
		}
	}
	
	
	
}
