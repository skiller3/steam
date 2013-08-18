package isard.steam;

import isard.steam.parse.Comment;
import isard.steam.parse.LangObject;
import isard.steam.parse.SExpr;
import isard.steam.parse.SExprSimple;
import isard.steam.parse.Symbol;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public static List<LangObject> filterComments(Collection<? extends LangObject> langObjects) {
		List<LangObject> filteredObjects = new ArrayList<LangObject>();
		for (LangObject langObject : langObjects)
			if (!(langObject instanceof Comment)) filteredObjects.add(langObject);
		return filteredObjects;
	}
	
	public static SExpr stripComments(SExpr sexpr) {
		SExprSimple strippedSExpr = new SExprSimple();
		for (LangObject part : filterComments(sexpr.getParts()))
			strippedSExpr.addPart(part);
		return strippedSExpr;
	}
	
	public static String join(Collection<? extends Object> objects, String sep) {
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for (Object o : objects) {
			if (first) first = false;
			else buf.append(sep);
			buf.append(String.valueOf(o));
		}
		return buf.toString();
	}
}
