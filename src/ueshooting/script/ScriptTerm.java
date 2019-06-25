package ueshooting.script;

public class ScriptTerm {
	public ScriptTerm(String b) {
		type = TreeElementType.UNKNOWN;
		body = b;
	}
	public ScriptTerm(TreeElementType t, String b) {
		type = t;
		body = b;
	}
	public TreeElementType type;
	public String body;
}
