package isard.steam.eval;

public class Value {
	
//	private Internal 		type;
	private STObject	object;
	private STCode		code;
	private JWrapper	jWrapper;
	 
	
	public Value(STObject object) {
		this.object = object;
//		this.type = Internal.OBJECT;
	}
	
	public Value(STCode code) {
		this.code = code;
//		this.type= Internal.CODE;
	}
	
	public Value(JWrapper jWrapper) {
		this.jWrapper = jWrapper;
//		this.type = Internal.JWRAPPER;
	}
	
//	public Internal getType() {return type;}
	public STObject getObject() {return object;}
	public STCode getCode() {return code;}
	public JWrapper getJWrapper() {return jWrapper;}
	
//	public static enum Internal {
//		CODE,
//		OBJECT,
//		TYPE
//	}
}
