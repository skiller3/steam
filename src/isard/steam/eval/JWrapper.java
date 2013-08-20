package isard.steam.eval;

public class JWrapper implements Value {
	
	private Object javaObject;
	
	public JWrapper(Object javaObject) {
		this.javaObject = javaObject;
	}
	
	public Object getJavaObject() {
		return javaObject;
	}
	
	@Override
	public String toString() {
		return String.valueOf(javaObject);
	}
}
