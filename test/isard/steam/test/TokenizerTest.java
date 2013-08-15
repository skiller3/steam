package isard.steam.test;

import static org.junit.Assert.*;

import isard.steam.token.Token;
import isard.steam.token.TokenType;
import isard.steam.token.Tokenizer;

import java.util.Arrays;

import org.junit.Test;

public class TokenizerTest {
	
	private static final TokenType OP = TokenType.OPEN_PARENTHESIS;
	private static final TokenType CP = TokenType.CLOSE_PARENTHESIS;
	private static final TokenType C = TokenType.COMMENT;
	private static final TokenType DQ = TokenType.DOUBLE_QUOTE_STRING_LITERAL;
	private static final TokenType SQ = TokenType.SINGLE_QUOTE_STRING_LITERAL;
	private static final TokenType O = TokenType.OTHER;
	
	private static final String 	CODE1 = "(+ 1 2 3 4)";
	private static final Token [] 	TOKENS1 = new Token[] {
		tok("(", OP),
		tok("+", O), 
		tok("1", O), 
		tok("2", O), 
		tok("3", O), 
		tok("4", O), 
		tok(")", CP)
	};
	
	private static final String 	CODE2 = "   (  +      1\t2\r3\n  4 )       ";
	private static final Token [] 	TOKENS2 = new Token[] {
		tok("(", OP),
		tok("+", O), 
		tok("1", O), 
		tok("2", O), 
		tok("3", O), 
		tok("4", O), 
		tok(")", CP)
	};
	
	private static final String 	CODE3 = " (-  (  +      1\t2\r3\n  4 )   \"d f\rask'hja\nhsf'kasjh\\\"\\\"\\\"\\\"  \"  ";
	private static final Token [] 	TOKENS3 = new Token[] {
		tok("(", OP),
		tok("-", O),
		tok("(", OP),
		tok("+", O), 
		tok("1", O), 
		tok("2", O), 
		tok("3", O), 
		tok("4", O), 
		tok(")", CP),
		tok("\"d f\rask'hja\nhsf'kasjh\"\"\"\"  \"", DQ)
	};
	
	private static final String 	CODE4 = "28342189 dfsaf 4fghaudfh 'sd\\'f\\\"\\\"as\\rfs\\nsa\\\"fdsaf' (+++++)";
	private static final Token [] 	TOKENS4 = new Token[] {
		tok("28342189", O),
		tok("dfsaf", O),
		tok("4fghaudfh", O),
		tok("'sd'f\"\"as\rfs\nsa\"fdsaf'", SQ), 
		tok("(", OP), 
		tok("+++++", O), 
		tok(")", CP), 
	};
	
	private static final String		CODE5 = "(Here is some cool code) ... // And here is a comment\n" +
											"I love lispy languages\n" + 
											"//Here is a comment that \"takes\" 'an' entire line\n" +
											"Some additional functionality";
	private static final Token [] 	TOKENS5 = new Token[] {
		tok("(", OP),
		tok("Here", O),
		tok("is", O),
		tok("some", O), 
		tok("cool", O), 
		tok("code", O), 
		tok(")", CP),
		tok("...", O),
		tok("// And here is a comment", C),
		tok("I", O),
		tok("love", O),
		tok("lispy", O),
		tok("languages", O),
		tok("//Here is a comment that \"takes\" 'an' entire line", C),
		tok("Some", O),
		tok("additional", O),
		tok("functionality", O)
	};
	
	private static final String		CODE6 = "a b c d \"String literal with // . So cool...\" ()";
	private static final Token [] 	TOKENS6 = new Token[] {
		tok("a", O),
		tok("b", O),
		tok("c", O),
		tok("d", O), 
		tok("\"String literal with // . So cool...\"", DQ), 
		tok("(", OP), 
		tok(")", CP)
	};	
	
	@Test
	public void smokeTestBasic() {
		test(CODE1, TOKENS1);
	}
	
	@Test
	public void testWhitespace() {
		test(CODE2, TOKENS2);
	}
	
	@Test
	public void testDoubleQuoteStr() {
		test(CODE3, TOKENS3);
	}
	
	@Test
	public void testSingleQuoteStr() {
		test(CODE4, TOKENS4);
	}
	
	@Test
	public void testComments() {
		test(CODE5, TOKENS5);
	}
	
	@Test
	public void testDQWithCommentChars() {
		test(CODE6, TOKENS6);
	}
	
	private static void test(String code, Token [] tokens) {
		Token [] tokenzizedTokens = tokenize(code);
		System.out.println(Arrays.toString(tokens));
		assertArrayEquals(tokens, tokenzizedTokens);
	}
	
	private static Token tok(String code, TokenType type) {
		return new Token(code, type);
	}
	
	private static Token [] tokenize(String code) {
		return new Tokenizer().tokenize(code).toArray(new Token[0]);
	}

}
