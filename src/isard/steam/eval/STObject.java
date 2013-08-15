package isard.steam.eval;

import java.util.LinkedHashMap;
import java.util.Map;

public class STObject {
	
	private STClass stClass;
	private Map<String,STObject> fields;
	
	public STObject(STClass stClass) {
		this.stClass = stClass;
		this.fields = new LinkedHashMap<String,STObject>();
	}
	
	public STObject(STClass stClass, Map<String,STObject> fields) {
		this.stClass = stClass;
		this.fields = new LinkedHashMap<String,STObject>(fields);
	}
	
	public void addField(String name, STObject value) {
		fields.put(name, value);
	}
	
	public STClass getSTClass() {
		return stClass;
	}
	
	public Map<String,STObject> getFields() {
		return fields;
	}
}
