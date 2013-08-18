package isard.steam.eval;

public class Value {
	
	public static final Value NIL = new Value() {
		@Override
		public String toString(){return "NIL";}
	};
	
	private STObject	object;
	private STCode		code;
	
	public Value(STObject object) {
		this.object = object;
	}
	
	public Value(STCode code) {
		this.code = code;
	}
	
	private Value() {}
	
	public STObject getObject() {return object;}
	public STCode getCode() {return code;}
	
	@Override
	public String toString() {
		if (object != null) return object.toString();
		return String.valueOf(code);
	}
}
