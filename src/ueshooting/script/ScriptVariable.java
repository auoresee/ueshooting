package ueshooting.script;

public class ScriptVariable {
	public String name;
	public DataType type;
	protected Object value;
	
	public ScriptVariable(String p_name, DataType p_type) {
		name = p_name;
		type = p_type;
		value = 0;
	}
	
	public ScriptVariable(String p_name, DataType p_type, Object p_value) {
		name = p_name;
		type = p_type;
		value = p_value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object pvalue) {
		value = pvalue;
	}
}
