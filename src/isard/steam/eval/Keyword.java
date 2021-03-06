package isard.steam.eval;

public enum Keyword {
	CODE, EVAL, DEF, MACRO, EXPAND, DO, FUN, /*HEAD, TAIL, FRONT, BACK,
	HADD, TADD, CAT, JAVA_CLASS, JAVA_NEW,*/ JAVA;
	
	public static Keyword fromCodeText(String codeText) {
		return valueOf(codeText.replace("-", "_").toUpperCase());
	}
	
	public String toCodeText() {
		return toString().toLowerCase().replaceAll("_", "-");
	}
	
}
