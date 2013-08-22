package isard.steam.eval;

import isard.steam.parse.Symbol;

import java.util.List;

public class Fun implements Value {

	private List<Symbol> parameterSymbols;
	private Code bodyCode;
	
	public Fun(List<Symbol> parameterSymbols, Code bodyCode) {
		this.parameterSymbols = parameterSymbols;
		this.bodyCode = bodyCode;
	}
	
	public List<Symbol> getParameterSymbols() {
		return parameterSymbols;
	}
	
	public Code getBodyCode() {
		return bodyCode;
	}
	
	@Override
	public String toString() {
		return "PARAMETERS:\n" + parameterSymbols + "\nBODY CODE:\n" + bodyCode;
	}
}
