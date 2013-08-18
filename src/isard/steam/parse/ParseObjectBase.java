package isard.steam.parse;

import java.util.List;

import isard.steam.token.Token;

public abstract class ParseObjectBase implements ParseObject {
	
	protected String text;
	protected List<Token> tokens;
	
	public ParseObjectBase(String text, List<Token> tokens) {
		this.text = text;
		this.tokens = tokens;
	}
	
	public String getText() {return text;}
	public List<Token> getTokens() {return tokens;}
}
