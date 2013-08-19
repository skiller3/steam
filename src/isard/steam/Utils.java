package isard.steam;

import isard.steam.parse.Comment;
import isard.steam.parse.ParseObject;
import isard.steam.parse.SExpr;
import isard.steam.parse.SExprSimple;
import isard.steam.parse.Symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Utils {
	
	public static boolean equal(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o2 != null && o1.equals(o2));
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> zip(Collection<? extends K> keys, Collection<? extends V> values) {
		if (keys.size() != values.size()) {
			String msg = "Collections of keys and values must be the same size.";
			throw new RuntimeException(msg);
		}
		Iterator<K> keyIter = (Iterator<K>)keys.iterator();
		Iterator<V> valueIter = (Iterator<V>)values.iterator();
		Map<K,V> map = new LinkedHashMap<K,V>();
		while (keyIter.hasNext()) map.put(keyIter.next(), valueIter.next());
		return map;
	}
	
	public static String nicelyFormat(List<ParseObject> langObjects) {
		boolean first = true;
		StringBuilder buf = new StringBuilder();
		
		for (ParseObject langObject : langObjects) {
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
	
	public static List<ParseObject> filterComments(Collection<? extends ParseObject> langObjects) {
		List<ParseObject> filteredObjects = new ArrayList<ParseObject>();
		for (ParseObject langObject : langObjects)
			if (!(langObject instanceof Comment)) filteredObjects.add(langObject);
		return filteredObjects;
	}
	
	public static SExpr stripComments(SExpr sexpr) {
		SExprSimple strippedSExpr = new SExprSimple();
		for (ParseObject part : filterComments(sexpr.getParts()))
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
