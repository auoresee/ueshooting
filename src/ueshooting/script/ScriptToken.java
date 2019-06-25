package ueshooting.script;

public class ScriptToken {
	public TokenType type;
	public String body;
	public ScriptToken(TokenType t, String b){
		type = t;
		body = b;
	}
}
