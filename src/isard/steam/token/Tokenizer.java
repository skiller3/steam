package isard.steam.token;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
	
	public List<Token> tokenize(String code) {
		return new StateMachine(code).tokenize();
	}
	
	@SuppressWarnings("serial")
	public static class TokenizerException extends RuntimeException {
		public TokenizerException(String msg) {super(msg);}
	}
	
	private class StateMachine {
		private boolean inSingleQuote = false; 		// For string literals
		private boolean inDoubleQuote = false; 		// For string literals
		private boolean inComment = false;     		// For comments
		private boolean backSlashActivated = false; // For string literal character escapes
		private boolean fwdSlashActivated = false;  // For comments
		
		private StringBuilder singleTokenBuffer = new StringBuilder();
		private List<Token> tokens = new ArrayList<Token>();
		
		private String code;
		
		public StateMachine(String code) {this.code = code;}
		
		public List<Token> tokenize() {
			for (int charidx = 0; charidx < code.length(); charidx++) 
				processChar(code.charAt(charidx));
			
			if (singleTokenBuffer.length() > 0) {
				if (inStringLiteral()) {
					String msg = "Incomplete string literal encountered: " + singleTokenBuffer.toString();
					throw new TokenizerException(msg);
				}
				else {
					endToken(TokenType.OTHER);
				}
			}
			
			return tokens;
		}
		
		private void processChar(char c) {
			switch (c) {
			case '\'': processSingleQuote(); 		break;
			case '\"': processDoubleQuote(); 		break;
			case '\\': processBackSlash(); 	 		break;
			case '/' : processFwdSlash();	 		break;
			case '(' : processOpenParenthesis();	break;
			case ')' : processCloseParenthesis();	break;
			default  : processNormalChar(c);
			}
		}
		
		private void processSingleQuote() {
			buffer('\'');
			if (!inDoubleQuote && !inComment && !backSlashActivated) {
				inSingleQuote = !inSingleQuote;
				if (!inSingleQuote) endToken(TokenType.SINGLE_QUOTE_STRING_LITERAL);
			}
			backSlashActivated = false;
		}
		
		private void processDoubleQuote() {
			buffer('\"');
			if (!inSingleQuote && !inComment && !backSlashActivated) {
				inDoubleQuote = !inDoubleQuote;
				if (!inDoubleQuote) endToken(TokenType.DOUBLE_QUOTE_STRING_LITERAL);
			}
			backSlashActivated = false;
		}
		
		private void processBackSlash() {
			if (inStringLiteral()) {
				if (backSlashActivated) buffer('\\');
				backSlashActivated = !backSlashActivated;
			}
			else buffer('\\');
		}
		
		private void processFwdSlash() {
			buffer('/');
			if (!inStringLiteral() && !inComment) {
				if (fwdSlashActivated) inComment = true;
				fwdSlashActivated = !fwdSlashActivated;
			}
			backSlashActivated = false;
		}
		
		private void processOpenParenthesis() {
			if (!inStringLiteral() && !inComment && singleTokenBuffer.length() > 0)
				endToken(TokenType.OTHER);
			buffer('(');
			endToken(TokenType.OPEN_PARENTHESIS);
			backSlashActivated = false;
		}
		
		private void processCloseParenthesis() {
			if (!inStringLiteral() && !inComment && singleTokenBuffer.length() > 0)
				endToken(TokenType.OTHER);
			buffer(')');
			endToken(TokenType.CLOSE_PARENTHESIS);
			backSlashActivated = false;
		}
		
		private void processNormalChar(char c) {
			if (backSlashActivated) {
				processEscapedChar(c);
			}
			else if (inComment && c == '\n') 
				endToken(TokenType.COMMENT);
			else if (inStringLiteral() || inComment || (c >= 33 && c <= 126))
				buffer(c);
			else if (singleTokenBuffer.length() > 0) 
				endToken(TokenType.OTHER);
			// Otherwise, NO-OP
		}
		
		private void processEscapedChar(char c) {
			switch(c) {
			case 'b': buffer('\b'); break;
			case 'f': buffer('\f'); break;
			case 'n': buffer('\n'); break;
			case 'r': buffer('\r'); break;
			case 't': buffer('\t'); break;
			default: 
				String msg = "Unable to tokenize string literal: \\" + c + " is an invalid escape sequence.";
				throw new TokenizerException(msg);
			}
			backSlashActivated = false;
		}
		
		private boolean inStringLiteral() {
			return inSingleQuote || inDoubleQuote;
		}
		
		private void buffer(char c) {
			singleTokenBuffer.append(c);
		}
		
		private void endToken(TokenType tokenType) {
			Token token = new Token(singleTokenBuffer.toString(), tokenType);
			tokens.add(token);
			singleTokenBuffer.setLength(0);
			inSingleQuote = false;
			inDoubleQuote = false;
			inComment = false;
			backSlashActivated = false;
			fwdSlashActivated = false;
		}
	}
}
