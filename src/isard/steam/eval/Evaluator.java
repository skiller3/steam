package isard.steam.eval;

import isard.steam.Utils;
import isard.steam.parse.Comment;
import isard.steam.parse.LangObject;
import isard.steam.parse.SExpr;
import isard.steam.parse.Symbol;
import isard.steam.token.Token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Pattern;

import bsh.Interpreter;

public class Evaluator {
	
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+\\.?[0-9]*$");
	
	public Value evaluate(List<LangObject> langObjects, Stack<Environment> envStack) {
		Value last = null;
		for (LangObject langObject : langObjects) last = evaluate(langObject, envStack);
		return last;
	}
	
	private Value evaluate(LangObject langObject, Stack<Environment> envStack) {
		if (langObject instanceof Comment) {
			return evaluate((Comment)langObject, envStack);
		}
		if (langObject instanceof Symbol) {
			return evaluate((Symbol)langObject, envStack);
		}
		return evaluate((SExpr)langObject, envStack);
	}
	
	private Value evaluate(Comment comment, Stack<Environment> envStack) {
		return null;
	}
	
	private Value evaluate(Symbol symbol, Stack<Environment> envStack) {
		
		Token token = symbol.getTokens().get(0);
		String strValue = token.getCode();
		Value value = null;
		
		switch (token.getType()) {
			case DOUBLE_QUOTE_STRING_LITERAL:
				strValue = strValue.substring(1, strValue.length() - 1);
				value = new Value(new STObject(strValue));
				break;
			case SINGLE_QUOTE_STRING_LITERAL:
				strValue = strValue.substring(1, strValue.length() - 1);
				value = new Value(new STObject(strValue));
				break;
			default:
				if (NUMBER_PATTERN.matcher(strValue).find()) {
					value = new Value(new STObject(new BigDecimal(strValue)));
				}
				else {
					try {
						value = dereferenceSymbol(strValue, envStack);
					} catch (Throwable t) {
						throw new EvaluatorException("Unrecognized symbol: " + strValue);
					}
				}
		}
		
		return value;
	}
	
	private Value evaluate(SExpr sexpr, Stack<Environment> envStack) {
		if (sexpr.getParts().size() < 1) return Value.NIL;
		
		// remember, the "ACTION" corresponds to the 2nd token, since the 1st token is 
		// an open parenthesis.
		Token token = sexpr.getTokens().get(1); 
		
		Keyword keyword = null;
		try {keyword = Keyword.fromCodeText(token.getCode());}
		catch (Throwable t) {/*NO-OP*/}
		
		Value value = null;

		if (keyword != null) {
			switch (keyword) {
			case CODE: 			value = handleCode(sexpr);					break;
			case DEF:			value = handleDef(sexpr, envStack);			break;
			/*
			case JAVA_CLASS:	value = handleJavaClass(sexpr, envStack);	break;
			case JAVA_NEW:		value = handleJavaNew(sexpr, envStack);		break;
			*/
			case JAVA:			value = handleJava(sexpr, envStack);		break;
			}
		}
		else {
			throw new EvaluatorException("Unrecognized action: " + token.getCode());
		}
		
		return value;
	}
	
	private Value handleCode(SExpr sexpr) {
		List<LangObject> langObjects = new ArrayList<LangObject>(sexpr.getParts());
		if (langObjects.size() > 0) langObjects.remove(0);
		return new Value(new STCode(langObjects));
	}
	
	private Value handleDef(SExpr sexpr, Stack<Environment> envStack) {
		List<LangObject> parts = Utils.filterComments(sexpr.getParts());
		if (parts.size() != 3) {
			throw new EvaluatorException("Invalid def syntax: " + sexpr);
		}
		
		Symbol symbol = null;
		Value value = null;
		try {
			symbol = (Symbol)parts.get(1);
		} catch (Throwable t) {
			throw new EvaluatorException("Cannot assign value to invalid symbol: " + parts.get(1));
		}
		
		value = evaluate(parts.get(2), envStack);
		envStack.peek().put(symbol.getText(), value);
		
		return value;
	}
	
	/*
	private Value handleJavaClass(SExpr sexpr, Stack<Environment> envStack) {
		List<LangObject> parts = Utils.filterComments(sexpr.getParts());
		if (parts.size() != 2) {
			throw new EvaluatorException("Invalid java-class syntax: " + sexpr);
		}
		Value classNameValue = evaluate(parts.get(1), envStack);
		try {
			String className = (String)classNameValue.getObject().getJavaObject();
			return new Value(new STObject(Class.forName(className)));
		} catch (Throwable t) {
			String msg = "Invalid java class name: " + classNameValue;
			throw new EvaluatorException(msg);
		}
	}
	
	private Value handleJavaNew(SExpr sexpr, Stack<Environment> envStack) {
		List<LangObject> parts = Utils.filterComments(sexpr.getParts());
		if (parts.size() != 4) {
			throw new EvaluatorException("Invalid java-new syntax: " + sexpr);
		}
		
		Value classValue = evaluate(parts.get(1), envStack);
		
		List<LangObject> paramClassExprs = null;
		try {
			paramClassExprs = (List<LangObject>)((SExpr)parts.get(2)).getParts();
		} catch (Throwable t) {
			throw new EvaluatorException("Invalid java-new syntax: " + sexpr);
		}
		
		List<LangObject> paramExprs = null;
		try {
			paramExprs = (List<LangObject>)((SExpr)parts.get(3)).getParts();
		} catch (Throwable t) {
			throw new EvaluatorException("Invalid java-new syntax: " + sexpr);
		}
		
		List<Value> paramClassValues = null;
		try {
			paramClassValues = evaluateEach(paramClassExprs, envStack);
		} catch (Throwable t) {
			String msg = "Failed to evaluate each sub-expression of parameter class expression: " + 
					parts.get(2);
			throw new EvaluatorException(msg, t);
		}
		
		List<Value> paramValues = null;
		try {
			paramValues = evaluateEach(paramExprs, envStack);
		} catch (Throwable t) {
			String msg = "Failed to evaluate each sub-expression of parameter expression: " + parts.get(3);
			throw new EvaluatorException(msg, t);
		}
		
		Class<?> classToNew = extractJObjectFromRefValue(classValue);
		
		Class<?> [] paramClasses = new Class<?> [paramClassValues.size()];
		for (int i = 0; i < paramClasses.length; i++) {
			Value paramClassValue = paramClassValues.get(i);
			Class<?> paramClass = null;
			try {
				paramClass = extractJObjectFromRefValue(paramClassValue);
			} catch (Throwable t) {
				String msg = "Class reference parameter number " + (i + 1) + " is invalid.";
				throw new EvaluatorException(msg, t);
			}
			paramClasses[i] = paramClass;
		}
		
		Constructor<?> constructor = null;
		try {
			constructor = classToNew.getConstructor(paramClasses);
		} catch (Throwable t) {
			String msg = "No " + classToNew + " constructor found for: " + Arrays.toString(paramClasses);
			throw new EvaluatorException(msg, t);
		}
		
		Object [] params = new Object[paramValues.size()];
		for (int i = 0; i < params.length; i++) {
			Value paramValue = paramValues.get(i);
			Object param = null;
			try {
				param = extractJObjectFromRefValue(paramValue);
			} catch (Throwable t) {
				String msg = "Method invocation parameter number " + (i + 1) + " is invalid.";
				throw new EvaluatorException(msg, t);
			}
			params[i] = param;
		}
		
		try {
			Object newJavaObject = constructor.newInstance(params);
			return new Value(new STObject(newJavaObject));
		} catch (Throwable t) {
			String msg = "Failed invocation of constructor " + constructor + " with parameters: " +
					Arrays.toString(params);
			throw new EvaluatorException(msg, t);
		}
	}
	*/
	
	private Value handleJava(SExpr sexpr, Stack<Environment> envStack) {
		Interpreter beanShellInterp = new Interpreter();
		
		// Set-up variables in the interpreter.
		Enumeration<Environment> environments = envStack.elements();
		while (environments.hasMoreElements()) {
			Environment env = environments.nextElement();
			for (Entry<String,Value> entry : env.entrySet()) {
				STObject stObject = entry.getValue().getObject();
				if (stObject != null) {
					Object javaObject = stObject.getJavaObject();
					if (javaObject != null) {
						String symbolName = entry.getKey();
						try {
							beanShellInterp.set(symbolName, javaObject);
						} catch (Throwable t) {
							String msg = "Failed to assign symbol " + symbolName + "in bean shell";
							throw new EvaluatorException(msg, t);
						}
					}
				}
			}
		}
		
		List<LangObject> parts = sexpr.getParts();
		if (parts.size() != 2) {
			String msg = "Invalid bean-shell syntax: " + sexpr;
			throw new EvaluatorException(msg);
		}
		
		Value tailValue = evaluate(parts.get(1), envStack);
		if (tailValue.getCode() == null) {
			String msg = "Invalid bean-shell syntax: " + sexpr + ". Second part of expression must " +
					"evaluate to a code object.";
			throw new EvaluatorException(msg);
		}
		
		String beanShellCode = Utils.nicelyFormat(tailValue.getCode().getLangObjects());
		try {
			beanShellInterp.eval(beanShellCode);
		} catch (Throwable t) {
			String msg = "Failed to evaluate " + beanShellCode + " with bean shell.";
			throw new EvaluatorException(msg, t);
		}
		
		try {
			Object retValue = beanShellInterp.get("_RETVALUE_");
			if (retValue == null) return Value.NIL;
			return new Value(new STObject(retValue));
		} catch (Throwable t) {
			throw new EvaluatorException("Could not extract _RETVALUE_ variable from bean shell.");
		}
	}
	
	private List<Value> evaluateTail(SExpr sexpr, Stack<Environment> envStack) {
		List<LangObject> parts = new ArrayList<LangObject>(sexpr.getParts());
		parts.remove(0);
		return evaluateEach(parts, envStack);
	}
	
	private List<Value> evaluateEach(List<LangObject> langObjects, Stack<Environment> envStack) {
		List<Value> values = new ArrayList<Value>(langObjects.size());
		for (LangObject langObject : langObjects) {
			values.add(evaluate(langObject, envStack));
		}
		return values;
	}
	
	private Value dereferenceSymbol(String symbolName, Stack<Environment> envStack) {
		Environment [] envArray = envStack.toArray(new Environment[0]);
		for (int i = envArray.length - 1; i >= 0; i--) {
			Environment env = envArray[i];
			if (env.containsKey(symbolName))
				return env.get(symbolName);
		}
		throw new EvaluatorException("Could not dereference symbol: " + symbolName);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T extractJObjectFromRefValue(Value value) {
		try {
			return (T)value.getObject().getJavaObject();
		} catch (Throwable t) {
			throw new EvaluatorException("Invalid java object reference value: " + value);
		}
	}
	
	@SuppressWarnings("serial")
	public static class EvaluatorException extends RuntimeException {
		public EvaluatorException(String msg) {super(msg);}
		public EvaluatorException(String msg, Throwable t) {super(msg, t);}
	}
}
