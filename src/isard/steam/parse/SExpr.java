package isard.steam.parse;

import java.util.List;

public interface SExpr extends LangObject {
	List<LangObject> getParts();
}
