package isard.steam.parse;

import isard.steam.token.Token;

import java.util.List;

public interface LangObject {
	String getText();
	List<Token> getTokens();
}
