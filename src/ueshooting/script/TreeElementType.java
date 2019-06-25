package ueshooting.script;

public enum TreeElementType {
	NONE	,
	UNKNOWN	,
	
	ROOT	,
	
	PROCEDURE,
	ACTION	,		//作動時間
	//LABEL	,
	
	COMMAND	,
	
	//即値
	L_INT	,
	L_DOUBLE,
	L_STRING,
	L_BOOLEAN,
	L_LABEL	,
	L_ARRAY, //配列初期化などで使用する予定
	//変数
	VARIABLE,
	//関数呼び出し
	FUNCTION_CALL,
	//演算子
	OPERATOR,
	
	END_OF_SECTION,;

	public boolean isLiteral() {
		return this == L_INT || this == L_DOUBLE || this == L_STRING || this == L_BOOLEAN || this == L_LABEL;
	}
}
