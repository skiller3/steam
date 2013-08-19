package isard.steam.parse;

import isard.steam.token.Token;

import java.util.Arrays;

public class Comment extends ParseObjectBase {
	
	public Comment(Token token) {
		super(token.getCode(), Arrays.asList(token));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Comment) {
			Comment oComment = (Comment)o;
			if (getText().equals(oComment.getText()))
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return getText().hashCode();
	}
}
