package ueshooting.script;

import ueshooting.stage.ScriptRuntimeException;

public enum DataType {
	INT,
	DOUBLE,
	BOOLEAN,
	STRING,
	
	ARRAY_INT,
	ARRAY_DOUBLE,
	ARRAY_BOOLEAN,
	ARRAY_STRING,
	
	ANY,
	;
	
	public boolean isArray() {
		if(this == ARRAY_INT || this == ARRAY_DOUBLE || 
				this == ARRAY_BOOLEAN || this == ARRAY_STRING) return true;
		
		return false;
	}
	
	public static DataType getType(String typeName) throws ScriptRuntimeException {
		switch(typeName) {
		case "int":
			return INT;
		
		case "double":
			return DOUBLE;
			
		case "bool":
			return BOOLEAN;
			
		case "string":
			return STRING;
			
		case "arrayint":
			return ARRAY_INT;
			
		case "arraydouble":
			return ARRAY_DOUBLE;
			
		case "arraybool":
			return ARRAY_BOOLEAN;
			
		case "arraystring":
			return ARRAY_STRING;
			
		default:
			throw new ScriptRuntimeException("Invalid type name \""+typeName+"\"");
		}
	}
}
