package isard.steam.parse;

import isard.steam.Utils;
import isard.steam.token.Token;
import isard.steam.token.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SExprSimple implements SExpr {
	
	private List<ParseObject> parts = new ArrayList<ParseObject>();
	
	public SExprSimple() {}
	
	@Override
	public List<ParseObject> getParts() {
		return Collections.unmodifiableList(parts);
	}
	
	@Override
	public String getText() {
		return Utils.nicelyFormat(parts);
	}
	
	@Override
	public List<Token> getTokens() {
		List<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("(", TokenType.OPEN_PARENTHESIS));
		for (ParseObject langObject : parts) {
			tokens.addAll(langObject.getTokens());
		}
		tokens.add(new Token(")", TokenType.CLOSE_PARENTHESIS));
		return tokens;
	}
	
	public void addPart(ParseObject langObject) {
		parts.add(langObject);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SExpr) {
			Object [] parts = this.parts.toArray();
			Object [] oParts = ((SExpr)o).getParts().toArray();
			return Arrays.equals(parts, oParts);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = 1;
		for (ParseObject part : parts) code = code * 31 + part.hashCode();
		return code;
	}

}
