package isard.steam.eval;

public interface Value {
	
	public static final Value NIL = new Value() {
		@Override
		public String toString(){return "NIL";}
	};
	
}
