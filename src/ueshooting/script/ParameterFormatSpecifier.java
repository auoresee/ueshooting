package ueshooting.script;

public class ParameterFormatSpecifier {
	public ParameterType type;
	public String[] operators;
	//public ParameterFormatSpecifier(TreeElementType type){
	//	types = new TreeElementType[]{type};
	//}
	
	public ParameterFormatSpecifier(ParameterType type){
		this.type = type;
	}
	
	//public ParameterFormatSpecifier(char p){
	//	types = new TreeElementType[]{TreeElementType.OPERATOR};
	//	operators = new char[]{p};
	//}
	
	public ParameterFormatSpecifier(String... p){
		type = ParameterType.OPERATOR;
		operators = p;
	}
}
