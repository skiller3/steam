package isard.steam.parse;

import java.util.Arrays;

import isard.steam.token.Token;

public class Symbol extends ParseObjectBase {

	public Symbol(Token token) {
		super(token.getCode(), Arrays.asList(token));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Symbol) {
			Symbol oSymbol = (Symbol)o;
			if (getText().equals(oSymbol.getText()))
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getText().hashCode();
	}
	
}
