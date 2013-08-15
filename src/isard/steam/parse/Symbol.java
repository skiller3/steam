package isard.steam.parse;

import java.util.Arrays;

import isard.steam.token.Token;

public class Symbol extends LangObjectBase {

	public Symbol(Token token) {
		super(token.getCode(), Arrays.asList(token));
	}
	
}