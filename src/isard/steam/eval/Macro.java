package isard.steam.eval;

import isard.steam.Utils;
import isard.steam.parse.ParseObject;
import isard.steam.parse.SExpr;
import isard.steam.parse.SExprSimple;
import isard.steam.parse.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Macro implements Value {
	
	private List<Symbol> substitutableSymbols;
	private Code bodyCode;
	
	public Macro(List<Symbol> substitutableSymbols, Code bodyCode) {
		this.substitutableSymbols = substitutableSymbols;
		this.bodyCode = bodyCode;
	}
	
	public Code expand(List<ParseObject> substitutions) {
		Map<Symbol,ParseObject> substitutionMap = Utils.zip(substitutableSymbols, substitutions);
		List<ParseObject> originalObjects = bodyCode.getParseObjects();
		List<ParseObject> expandedObjects = new ArrayList<ParseObject>(originalObjects.size());
		for (ParseObject originalObject : originalObjects) 
			expandedObjects.add(expand(originalObject, substitutionMap));
		return new Code(expandedObjects);
	}
	
	private static ParseObject expand(ParseObject parseObject, Map<Symbol,ParseObject> substitutionMap) {
		ParseObject expandedObject = null;
		if (substitutionMap.containsKey(parseObject)) {
			expandedObject = substitutionMap.get(parseObject);
		}
		else if (parseObject instanceof SExpr) {
			List<ParseObject> parts = ((SExpr)parseObject).getParts();
			SExprSimple expanded_sexpr = new SExprSimple();
			for (ParseObject part : parts) expanded_sexpr.addPart(expand(part, substitutionMap));
			expandedObject = expanded_sexpr;
		}
		else {
			expandedObject = parseObject;
		}
		return expandedObject;
	}
	
	public List<Symbol> getSubstitutableSymbols() {
		return substitutableSymbols;
	}
	
	public Code getBodyCode() {
		return bodyCode;
	}
	
	@Override
	public String toString() {
		return "SUBSTUTIONS:\n" + substitutableSymbols + "\nBODY CODE:\n" + bodyCode;
	}
}
