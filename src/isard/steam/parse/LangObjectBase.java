package isard.steam.parse;

import java.util.List;

import isard.steam.token.Token;

public abstract class LangObjectBase implements LangObject {
	
	protected String text;
	protected List<Token> tokens;
	
	public LangObjectBase(String text, List<Token> tokens) {
		this.text = text;
		this.tokens = tokens;
	}
	
	public String getText() {return text;}
	public List<Token> getTokens() {return tokens;}
}
