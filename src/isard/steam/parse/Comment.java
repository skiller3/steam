package isard.steam.parse;

import isard.steam.token.Token;

import java.util.Arrays;

public class Comment extends LangObjectBase {
	
	public Comment(Token token) {
		super(token.getCode(), Arrays.asList(token));
	}
	
}
