package isard.steam.parse;

import isard.steam.Utils;
import isard.steam.token.Token;
import isard.steam.token.TokenType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SExprSimple implements SExpr {
	
	private List<LangObject> parts = new ArrayList<LangObject>();
	
	public SExprSimple() {}
	
	@Override
	public List<LangObject> getParts() {
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
		for (LangObject langObject : parts) {
			tokens.addAll(langObject.getTokens());
		}
		tokens.add(new Token(")", TokenType.CLOSE_PARENTHESIS));
		return tokens;
	}
	
	public void addPart(LangObject langObject) {
		parts.add(langObject);
	}
}
