package ueshooting.script;

public enum TreeElementType {
	NONE	,
	UNKNOWN	,
	
	ROOT	,
	
	PROCEDURE,
	ACTION	,		//�쓮����
	//LABEL	,
	
	COMMAND	,
	
	//���l
	L_INT	,
	L_DOUBLE,
	L_STRING,
	L_BOOLEAN,
	L_LABEL	,
	L_ARRAY, //�z�񏉊����ȂǂŎg�p����\��
	//�ϐ�
	VARIABLE,
	//�֐��Ăяo��
	FUNCTION_CALL,
	//���Z�q
	OPERATOR,
	
	END_OF_SECTION,;

	public boolean isLiteral() {
		return this == L_INT || this == L_DOUBLE || this == L_STRING || this == L_BOOLEAN || this == L_LABEL;
	}
}
