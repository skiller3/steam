package isard.steam;

import isard.steam.parse.Comment;
import isard.steam.parse.LangObject;
import isard.steam.parse.SExpr;
import isard.steam.parse.Symbol;

import java.util.List;

public class Utils {
	
	public static boolean equal(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
	}
	
	public static String nicelyFormat(List<LangObject> langObjects) {
		boolean first = true;
		StringBuilder buf = new StringBuilder();
		
		for (LangObject langObject : langObjects) {
			if (!first) buf.append(" ");
			else first = false;
			
			
			if (langObject instanceof Comment)
				buf.append(langObject.getText()).append("\n");
			else if (langObject instanceof Symbol)
				buf.append(langObject.getText());
			else if (langObject instanceof SExpr)
				buf.append("(")
				   .append(nicelyFormat( ((SExpr)langObject).getParts() ))
				   .append(")");
		}
		
		return buf.toString();
	}
}
