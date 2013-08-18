package isard.steam.eval;

import isard.steam.Utils;
import isard.steam.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class STCode {
	
	private List<ParseObject> langObjects;
	
	public STCode(List<ParseObject> langObjects) {
		this.langObjects = new ArrayList<ParseObject>(langObjects);
	}
	
	public List<ParseObject> getLangObjects() {
		return langObjects;
	}
	
	@Override
	public String toString() {
		return Utils.nicelyFormat(langObjects);
	}
}
