package ueshooting.script;

@SuppressWarnings("serial")
public class ScriptSyntaxException extends Throwable {
	public String message;
	public int error_position;
	public ScriptSyntaxException(String string, int pos) {
		super(string);
		message = string;
		error_position = pos;
	}

}
