package ueshooting.script;

public enum TreeElementType {
	NONE	,
	UNKNOWN	,
	
	ROOT	,
	
	PROCEDURE,
	ACTION	,		//ì“®ŠÔ
	//LABEL	,
	
	COMMAND	,
	
	//‘¦’l
	L_INT	,
	L_DOUBLE,
	L_STRING,
	L_BOOLEAN,
	L_LABEL	,
	//®
	EXPRESSION,
	//•Ï”
	VARIABLE,	//data: •Ï”–¼(string)
	SPECIAL_VARIABLE,
	//ŠÖ”ŒÄ‚Ño‚µ
	FUNCTION_CALL,
	//‰‰Zq
	OPERATOR,
	
	END_OF_SECTION,;

	public boolean isLiteral() {
		return this == L_INT || this == L_DOUBLE || this == L_STRING || this == L_BOOLEAN || this == L_LABEL;
	}
}
