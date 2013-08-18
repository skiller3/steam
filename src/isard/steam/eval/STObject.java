package isard.steam.eval;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class STObject {
	
	private Map<String,STObject> fields;
	private Object javaObject;
	
	public STObject(Map<String,STObject> fields) {
		this.fields = new LinkedHashMap<String,STObject>(fields);
	}
	
	public STObject(Object javaObject) {
		this.javaObject = javaObject;
	}
	
	public Map<String,STObject> getFields() {
		return fields;
	}
	
	public Object getJavaObject() {
		return javaObject;
	}
	
	@Override
	public String toString() {
		if (javaObject != null) {
			if (javaObject instanceof String || javaObject instanceof BigDecimal) {
				return javaObject.toString();
			}
			else {
				return "Java Wrapper: {" + javaObject.getClass().getName() + "}{" + 
						javaObject.toString() + "}";
			}
		}
			
		return "UNKNOWN";
	}
}
