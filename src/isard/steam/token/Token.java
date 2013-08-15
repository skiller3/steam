package isard.steam.token;

import java.util.Arrays;

public class Token {
	
	private String code;
	private TokenType type;

	public Token(String code, TokenType type) {
		this.code = code;
		this.type = type;
	}
	
	public String getCode() {
		return code;
	}

	public TokenType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return code;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Token) {
			Token otherToken = (Token)o;
			Object [] thisAttr = new Object[]{code, type};
			Object [] otherAttr = new Object[]{otherToken.code, otherToken.type};
			return Arrays.equals(thisAttr, otherAttr);
		}
		return false;
	}
}
