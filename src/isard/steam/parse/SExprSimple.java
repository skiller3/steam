package isard.steam.parse;

import isard.steam.Utils;
import isard.steam.token.Token;
import isard.steam.token.TokenType;

import java.util.ArrayList;
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
}
