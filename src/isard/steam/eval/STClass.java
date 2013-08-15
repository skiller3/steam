package isard.steam.eval;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class STClass extends Type {
	
	private Map<String,Type> slots = new LinkedHashMap<String,Type>();
	
	public STClass(String name) {
		super(name);
	}
	
	public void addSlot(String name, Type type) {
		this.slots.put(name, type);
	}
	
	public abstract STObject create(Map<String,Object> params);
	
}
