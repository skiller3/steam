package isard.steam.parse;

import java.util.List;

public interface SExpr extends ParseObject {
	List<ParseObject> getParts();
}
