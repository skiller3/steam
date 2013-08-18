package isard.steam.parse;

import isard.steam.token.Token;

import java.util.List;

public interface ParseObject {
	String getText();
	List<Token> getTokens();
}
