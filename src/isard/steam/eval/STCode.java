package isard.steam.eval;

import isard.steam.Utils;
import isard.steam.parse.LangObject;

import java.util.ArrayList;
import java.util.List;

public class STCode {
	
	private List<LangObject> langObjects;
	
	public STCode(List<LangObject> langObjects) {
		this.langObjects = new ArrayList<LangObject>(langObjects);
	}
	
	public List<LangObject> getLangObjects() {
		return langObjects;
	}
	
	@Override
	public String toString() {
		return Utils.nicelyFormat(langObjects);
	}
}
