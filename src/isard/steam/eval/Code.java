package isard.steam.eval;

import isard.steam.Utils;
import isard.steam.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class Code implements Value {
	
	private List<ParseObject> langObjects;
	
	public Code(List<ParseObject> langObjects) {
		this.langObjects = new ArrayList<ParseObject>(langObjects);
	}
	
	public List<ParseObject> getParseObjects() {
		return langObjects;
	}
	
	@Override
	public String toString() {
		return Utils.nicelyFormat(langObjects);
	}
}
