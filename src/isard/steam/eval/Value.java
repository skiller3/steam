package isard.steam.eval;

public class Value {
	
	public static final Value NIL = new Value() {
		@Override
		public String toString(){return "NIL";}
	};
	
	private STObject	object;
	private STCode		code;
	private STMacro		macro;
	
	public Value(STObject object) {
		this.object = object;
	}
	
	public Value(STCode code) {
		this.code = code;
	}
	
	public Value(STMacro macro) {
		this.macro = macro;
	}
	
	private Value() {}
	
	public STObject getObject() {return object;}
	public STCode getCode() {return code;}
	public STMacro getMacro() {return macro;}
	
	@Override
	public String toString() {
		if (object != null) return object.toString();
		if (code != null) return code.toString();
		return String.valueOf(macro);
	}
}
