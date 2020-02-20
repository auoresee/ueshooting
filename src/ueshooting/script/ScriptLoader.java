package ueshooting.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ueshooting.stage.Script;

public class ScriptLoader
{
	protected int pos = 0;
	protected int prev_pos = 0;
	protected char prev_delimiter = '\0';
	private final char sign_list[] = {'+','-','*','/','^','\\','%','=','{','}','>','<','\"','&','|',',','(',')'};
	private final String operator_list[] = {"+","-","*","/","^","%","+=","-=","*=","/=","%=","==","=","!=","=!",">","<",">=","<=","&&","||","(",")"};
	
	/**
	 * �X�N���v�g����͂��\���؂𐶐�����
	 * ���[�g(�K�w0)
	 * @param source �X�N���v�g�̃\�[�X
	 * @return Script �\����
	 * @throws ScriptSyntaxException
	 */
	public Script generateTree(String source) throws ScriptSyntaxException
	{
		Script root = new Script(TreeElementType.ROOT);
		while(pos < source.length())
		{
			TreeElement e = generateTreeLabel(source);
			if(e == null) break;
			root.addChild(e);
		}
		if(pos > source.length())throw new RuntimeException();
		return root;
	}
	
	//�K�w1
	private TreeElement generateTreeLabel(String source) throws ScriptSyntaxException {
		TreeElement parent = null;

		//while(pos < source.length())
		//{
		skipSpaceBreak(source);
		//if(pos >= source.length())break;
		//prev_pos = pos;
		ScriptTerm term = getTermSection(source);
		if(term == null) return null;
		if(getTermType(term) == TreeElementType.PROCEDURE){
			parent = new TreeElement(TreeElementType.PROCEDURE);
			parent.data = term.body;
			//break;
		}
		else if(getTermType(term) == TreeElementType.ACTION){
			parent = new TreeElement(TreeElementType.ACTION);
			parent.data = Integer.parseInt(term.body);
			//break;
		}

		if(term.body == null || term.body.equals("")) {
			throw new ScriptSyntaxException("Illegal procedure name", pos);
		}
		
		//else if(getTermType(term) == TreeElementType.LABEL){
		//	parent = new TreeElement(TreeElementType.LABEL);
			//break;
		//}
		//if(pos >= source.length())break;
		
		//throw new ScriptSyntaxException(String.format("Illegal token %s(type: %s) at the beginning of the line",
		//		term, getTermType(term).toString()), prev_pos);
		//}
		if(pos > source.length())throw new RuntimeException();
		
		while(pos < source.length())
		{
			skipSpaceBreak(source);
			if(pos >= source.length())break;
			
			TreeElement child = generateTreeLine(source);
			if(child.type == TreeElementType.END_OF_SECTION) {
				break;
			}
			else if(child.type != TreeElementType.COMMAND) {
				throw new ScriptSyntaxException("Unexpected token", pos);
			}
			parent.addChild(child);
			
			//throw new ScriptSyntaxException(String.format("Illegal token %s(type: %s) at the beginning of the line",
			//		term, getTermType(term).toString()), prev_pos);
		}
		
		return parent;
	}

	//�K�w2
	private TreeElement generateTreeLine(String source) throws ScriptSyntaxException {
		ScriptToken token = getToken(source);
		if(token.body.equals("}")) {
			seekNextLine(source);
			return new TreeElement(TreeElementType.END_OF_SECTION);
		}
		if(token.body.equals("//")) {
			seekNextLine(source);
			return generateTreeLine(source);
		}
		TreeElement parent = new TreeElement(TreeElementType.COMMAND);
		ScriptCommand command = getCommand(token.body);
		if(command == null) {
			throw new ScriptSyntaxException("Undefined Command", pos);
		}
		parent.data = command;
		if(command == ScriptCommand.LET) {
			List<TreeElement> elements = getAssignmentExpression(source);
			for(TreeElement t : elements){
				parent.addChild(t);
			}
		}
		else {
			ParameterFormat format = ParameterFormat.getParameterFormat(command);
			parent.addChilds(getParameters(source, format));
		}
		seekNextLine(source);
		return parent;
	}
	
	private List<TreeElement> getParameters(String source, ParameterFormat format) throws ScriptSyntaxException {
		List<TreeElement> ret = new ArrayList<>();
		for(int i = 0;i < format.format_specifiers.length;i++){
			if(pos >= source.length()) throw new ScriptSyntaxException("Expecting argument", pos);
			List<TreeElement> ltmp = getExpression(source, ParameterType.ANY);
			if(ltmp.size() == 0) {
				throw new ScriptSyntaxException("Parameters cannot be omitted", pos);
			}
			else if(ltmp.size() == 1) {
				ret.add(ltmp.get(0));
			}
			else {
				TreeElement tmp = new TreeElement(TreeElementType.EXPRESSION);
				ltmp = toPostfixNotation(ltmp);	//�t�|�[�����h���ɕϊ�
				tmp.addChilds(ltmp);
				ret.add(tmp);
			}
			System.out.println(pos);
		}
		return ret;
	}

	private List<TreeElement> getAssignmentExpression(String source) throws ScriptSyntaxException {
		List<TreeElement> ret = new ArrayList<>();
		
		//���ӂ̕ϐ����擾
		ParameterFormatSpecifier format = new ParameterFormatSpecifier(ParameterType.VAR_ANY);
		TreeElement child = getParameter(source, format);
		//ParameterType varType = getVariableParameterType((ScriptSpecialVariable) child.data);
		ret.add(child);
		
		//varType��ϐ��^���l�^�ɕϊ� (��̏����̂���)�@
		/*if(varType == ParameterType.VAR_ANY) varType = ParameterType.ANY;
		if(varType == ParameterType.VAR_INT || varType == ParameterType.VAR_DOUBLE) varType = ParameterType.DOUBLE_OR_INT;
		if(varType == ParameterType.VAR_STRING) varType = ParameterType.STRING;
		if(varType == ParameterType.VAR_BOOLEAN) varType = ParameterType.BOOLEAN;*/
		
		//������Z�q���擾
		format = new ParameterFormatSpecifier("=","+=","-=","*=","/=","%=","=!");
		child = getParameter(source, format);
		//if(!isOperatorCompatible((String) child.data, varType)){	//�g���Ȃ����Z�q�̑g�ݍ��킹(������^��-=�Ȃ�)�Ȃ�G���[
		//	throw new ScriptSyntaxException(String.format("Incompatible operator %s with type %s", (String) child.data, varType.toString()), pos);
		//}
		ret.add(child);
		
		//�E�ӂ��擾
		List<TreeElement> elements = getExpression(source, ParameterType.ANY);
		elements = toPostfixNotation(elements);
		child = new TreeElement(TreeElementType.EXPRESSION);
		child.list = elements;
		ret.add(child);
		return ret;
	}

	/**
	 * �����������g�[�N���̃��X�g���擾
	 * (�t�|�[�����h���ɂ͂��Ȃ�)
	 * @param source
	 * @param pType
	 * @return
	 * @throws ScriptSyntaxException
	 */
	private List<TreeElement> getExpression(String source, ParameterType pType) throws ScriptSyntaxException {
		List<TreeElement> ret = new ArrayList<>();
		
		prev_pos = pos;
		skipSpace(source);
		if(getToken(source).body.equals("(")){
			TreeElement child = new TreeElement(TreeElementType.OPERATOR);
			child.data = "(";
			ret.add(child);
		}
		else{
			pos = prev_pos;
		}
		
		ParameterFormatSpecifier format = new ParameterFormatSpecifier(ParameterType.ANY);
		TreeElement child = getParameter(source, format);
		
		ret.add(child);
		while(true){
			format = new ParameterFormatSpecifier("+","-","*","/","%","^");
			prev_pos = pos;
			skipSpace(source);
			ScriptToken t = getToken(source);
			if(t.body.equals(")")){
				child = new TreeElement(TreeElementType.OPERATOR);
				child.data = ")";
				ret.add(child);
			}
			else {
				pos = prev_pos;
			}
			if(t.type == TokenType.BREAK || t.type == TokenType.EOF || t.body.equals(",")){
				break;
			}
			child = getParameter(source, format);
			if(!isOperatorCompatible((String) child.data, pType)){
				throw new ScriptSyntaxException(String.format("Incompatible operator %s with type %s", (String) child.data, pType.toString()), pos);
			}
			ret.add(child);
			prev_pos = pos;
			skipSpace(source);
			if(getToken(source).body.equals("(")){
				child = new TreeElement(TreeElementType.OPERATOR);
				child.data = "(";
			}
			else{
				pos = prev_pos;
			}
			format = new ParameterFormatSpecifier(ParameterType.ANY);
			child = getParameter(source, format);

			ret.add(child);
		}
		return ret;
	}
	
	// ���Z�q
	// �D�揇�� : ���Z�q : ������
	// 4 :   !   : �E������
	// 3 : * / % : ��������
	// 2 :  + -  : ��������
	// 1 :   =   : �E������
	int op_preced(String c)
	{
	    switch (c) {
	        case "!":
	            return 4;
	        case "*":  case "/": case "%":
	            return 3;
	        case "+": case "-":
	            return 2;
	        case "=":
	            return 1;
	    }
	    return 0;
	}
	 
	boolean op_left_assoc(String c)
	{
	    switch (c) {
	        // ��������
	        case "*": case "/": case "%": case "+": case "-":
	            return true;
	        // �E������
	        case "=": case "!":
	            return false;
	    }
	    return false;
	}
	
	/**
	 * �����t�|�[�����h�L�@�ɕϊ�
	 * 
	 * @param input
	 * @return
	 */
	private List<TreeElement> toPostfixNotation(List<TreeElement> input)
	{
		List<TreeElement> output = new ArrayList<>();
	    int index = 0;
	    TreeElement c;
		char outpos;// = output;
	 
		TreeElement[] stack = new TreeElement[32];       // ���Z�q�X�^�b�N
	    int stackLength = 0;  // �X�^�b�N���i�[���j
	    TreeElement stackCounter;              // �X�^�b�N�v�f�̋L�^�p
	 
	    while (index < input.size()) {
	        // ���̓X�g���[������g�[�N����1�ǂݍ���
	        c = input.get(index);
	        if (c.type.isLiteral() || c.type == TreeElementType.VARIABLE) {
	            // �g�[�N�������l�i���ʎq�j�Ȃ�A�o�̓L���[�ɒǉ�����
	            output.add(c);
	        } else if (c.type == TreeElementType.FUNCTION_CALL) {
	            // �g�[�N�����֐��Ȃ�A�X�^�b�N�Ƀv�b�V������B
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals(",")) {
	            // �g�[�N�����֐��̈����̃Z�p���[�^�i�Ⴆ�΃J���}�j�̏ꍇ
	            boolean pe = false;
	            while (stackLength > 0) {
	                stackCounter = stack[stack.length - 1];
	                if (((String)stackCounter.data).equals("(")) {
	                    pe = true;
	                    break;
	                } else {
	                    // �X�^�b�N�̃g�b�v�̃g�[�N���������ʂɂȂ�܂�
	                    // �X�^�b�N��̉��Z�q���o�̓L���[�Ƀ|�b�v��������
	                	output.add(stackCounter);
	                    stackLength--;
	                }
	            }
	            // �����ʂ��o�Ă��Ȃ������ꍇ�A���Ȃ킿�Z�p���[�^�̈ʒu���ς������ꍇ
	            // ���邢�͊��ʂ��������Ή����Ă��Ȃ��ꍇ
	            if (!pe) {
	                System.out.printf("�G���[�F�Z�p���[�^�����ʂ̕s��v\n");
	                return null;
	            }
	        } else if (c.type == TreeElementType.OPERATOR && !((String)c.data).equals("(") && !((String)c.data).equals(")")) {
	            // �g�[�N�������Z�q op1 �̏ꍇ
	            while (stackLength > 0) {
	                stackCounter = stack[stackLength - 1];
	                // op1 �����������ŗD�揇�ʂ� op2 �Ɠ��������Ⴂ�ꍇ
	                // ���邢�� op1 �̗D�揇�ʂ� op2 ���Ⴂ�ꍇ
	                // ���Z�q�g�[�N�� op2 ���X�^�b�N�̃g�b�v�ɂ���ԃ��[�v����B
	                // 1^2+3 �̂悤�Ȏ��𐳂��� 12^3+ �ɕϊ����邽��
	                // "+" �� "^" �͉E�������Ƃ���B
	                // ���Z�q�̗D�揇�ʂ̈Ⴂ����|�b�v���邩�v�b�V�����邩�𔻒f����B
	                // 2�̉��Z�q�̗D�揇�ʂ��������Ȃ�A���������画�f����B
	                if (stackCounter.type == TreeElementType.OPERATOR &&
	                    ((op_left_assoc((String) c.data) && (op_preced((String) c.data) <= op_preced((String) stackCounter.data))) ||
	                        (op_preced((String) c.data) < op_preced((String) stackCounter.data)))) {
	                    // Pop op2 off the stack, onto the output queue;
	                	output.add(stackCounter);
	                    stackLength--;
	                } else {
	                    break;
	                }
	            }
	            // op1 ���X�^�b�N�Ƀv�b�V��
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals("(")) {
	            // �g�[�N���������ʂȂ�A�X�^�b�N�Ƀv�b�V��
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals(")")) {
	            // �g�[�N�����E���ʂ̏ꍇ
	            boolean pe = false;
	            // �X�^�b�N�̃g�b�v�ɂ���g�[�N���������ʂɂȂ�܂�
	            // �X�^�b�N����o�̓L���[�ɉ��Z�q���|�b�v��������
	            while (stackLength > 0) {
	                stackCounter = stack[--stackLength];
	                if (((String)stackCounter.data).equals("(")) {
	                    // �X�^�b�N���獶���ʂ��|�b�v���邪�A�o�̓L���[�ɂ͒u���Ȃ�
	                    pe = true;
	                    break;
	                } else {
	                	output.add(stackCounter);
	                }
	            }
	            // �X�^�b�N��S�����Ă������ʂɓ��B���Ȃ������ꍇ�A���E�̊��ʂ̕s��v�����邱�ƂɂȂ�
	            if (!pe) {
	                System.out.printf("�G���[�F���ʂ̕s��v\n");
	                return null;
	            }
	            // �X�^�b�N�̃g�b�v�ɂ���g�[�N�����֐��g�[�N���Ȃ�A������o�̓L���[�Ƀ|�b�v����
	            if (stackLength > 0) {
	                stackCounter = stack[stackLength - 1];
	                if (stackCounter.type == TreeElementType.FUNCTION_CALL) {
	                	output.add(stackCounter);
	                    stackLength--;
	                }
	            }
	        } else {
	        	System.out.printf("�s���ȃg�[�N���F%c\n", c);
	            return null; // �s���ȃg�[�N��
	        }
	        index++;
	    }
	    // �ǂݍ��ނׂ��g�[�N�����s������
	    // �X�^�b�N��ɉ��Z�q�g�[�N�����c���Ă�����A�������o�͂���
	    while (stackLength > 0) {
	        stackCounter = stack[--stackLength];
	        if (((String)stackCounter.data).equals("(") || ((String)stackCounter.data).equals(")")) {
	            System.out.printf("�G���[�F���ʂ̕s��v\n");
	            return null;
	        }
	        output.add(stackCounter);
	    }
	    return output;
	}


	private boolean isOperandTypesCompatible(ParameterType type2,
			ParameterType varType) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private boolean isOperandTypesCompatible(TreeElementType type2,
			ParameterType varType) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean isOperatorCompatible(String operator, ParameterType varType) {
		// TODO Auto-generated method stub
		return true;
	}

	private TreeElement getParameter(String source,
			ParameterFormatSpecifier parameter) throws ScriptSyntaxException {
		skipSpaceBreak(source);
		if(pos >= source.length()) throw new ScriptSyntaxException("Expecting argument", pos);
		prev_pos = pos;
		ScriptToken token = getToken(source);
		if(token.body.equals(",")){
			prev_pos = pos;
			token = getToken(source);
		}
		pos = prev_pos;
		if(token.type == TokenType.NONE) throw new ScriptSyntaxException("Expecting argument", pos);
		TreeElement parent;
		if(parameter.type == ParameterType.OPERATOR){
			token = getToken(source);	//��x�߂���pos��i�߂邽��
			if(token.type != TokenType.SIGN) throw new ScriptSyntaxException("Invalid operator", pos);
			String str = token.body;
			for(int i = 0;i < operator_list.length;i++){
				if(str.equals(operator_list[i])){
					parent = new TreeElement(TreeElementType.OPERATOR);
					parent.data = str;
					return parent;
				}
			}
			throw new ScriptSyntaxException("Undefined operator", pos);
		}
		else{
			if(token.type == TokenType.NUMBER || token.body.equals("-")){
				double num = getLiteralRealNumber(source);
				if(num % 1 == 0) {
					parent = new TreeElement(TreeElementType.L_INT);
					parent.data = (int)num;
				}
				else {
					parent = new TreeElement(TreeElementType.L_DOUBLE);
					parent.data = num;
				}
			}
			else if(token.body.equals("\"")){
				String str = getLiteralString(source);
				parent = new TreeElement(TreeElementType.L_STRING);
				parent.data = str;
			}
			else if(token.type == TokenType.ALPHABET){
				token = getToken(source);	//��x�߂���pos��i�߂邽��
				if(token.body.equals("true")){
					parent = new TreeElement(TreeElementType.L_BOOLEAN);
					parent.data = true;
				}
				if(token.body.equals("false")){
					parent = new TreeElement(TreeElementType.L_BOOLEAN);
					parent.data = false;
				}
				else{
					if(!checkVarName(token.body)) {		//�ϐ������\���łȂ����`�F�b�N
						throw new ScriptSyntaxException("Invalid variable name \"" + token.body + "\"", pos);
					}
					parent = new TreeElement(TreeElementType.VARIABLE);
					parent.data = token.body;
				}
					//if(getVariableParameterType(var) != parameter.type) {
					//	throw new ScriptSyntaxException("incompatible type for the argument", pos);
					//}
					/*if(token.body.equals("D") || token.body.equals("E") || token.body.equals("F") || token.body.equals("G")){
						parent = new TreeElement(TreeElementType.SPECIAL_VARIABLE);
						if(!getToken(source).body.equals("["))throw new ScriptSyntaxException("Invalid argument", pos);
						var.index = (int) getLiteralRealNumber(source);
						if(!getToken(source).body.equals("]"))throw new ScriptSyntaxException("Invalid argument", pos);
						parent.data = var;
					}
					else{
						parent = new TreeElement(TreeElementType.SPECIAL_VARIABLE);
						parent.data = var;
					}*/
			}
			else if(token.body.equals("*")){
				token = getToken(source);	//��x�߂���pos��i�߂邽��
				ScriptToken token2 = getToken(source);
				if(token2.type != TokenType.ALPHABET) throw new ScriptSyntaxException("Invalid argument", pos);
				String str = token2.body;
				parent = new TreeElement(TreeElementType.L_LABEL);
				parent.data = str;
			}
			else {
				throw new ScriptSyntaxException("Invalid argument", pos);
			}
			
			switch(parameter.type){
			case ANY:
				return parent;
			case INT:
				if(parent.type == TreeElementType.L_INT){
					return parent;
				}
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case DOUBLE_OR_INT:
				if(parent.type == TreeElementType.L_INT ||
						parent.type == TreeElementType.L_DOUBLE){
					return parent;
				}
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case DOUBLE_INT_BOOLEAN:
				if(parent.type == TreeElementType.L_INT ||
						parent.type == TreeElementType.L_DOUBLE ||
						parent.type == TreeElementType.L_BOOLEAN){
					return parent;
				}
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case STRING:
				if(parent.type == TreeElementType.L_STRING){
					return parent;
				}
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case BOOLEAN:
				if(parent.type == TreeElementType.L_BOOLEAN){
					return parent;
				}
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case PROCEDURE:
				if(parent.type == TreeElementType.L_LABEL){
					return parent;
				}
				break;
			case VAR_ANY:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_INT:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_DOUBLE:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_DOUBLE_OR_INT:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_DOUBLE_INT_BOOLEAN:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_STRING:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			case VAR_BOOLEAN:
				if(parent.type == TreeElementType.VARIABLE){
					return parent;
				}
				break;
			default:
				throw new RuntimeException("Unexcepted error");
			}
		}
		throw new ScriptSyntaxException("Invalid argument", pos);
	}

	/**
	 * �ϐ������\���łȂ����`�F�b�N (������)
	 * @param body
	 * @return
	 */
	private boolean checkVarName(String body) {
		// TODO Auto-generated method stub
		return true;
	}

	private String getLiteralString(String source) throws ScriptSyntaxException {
		ScriptToken token = getToken(source);
		if(!token.body.equals("\"")){
			throw new RuntimeException();
		}
		String ret = "";
		boolean escape = false;
		while(pos < source.length()){
			char c = source.charAt(pos);
			pos++;
			if(c == '\\'){
				escape = true;
			}
			if(!escape && c == '\"'){
				return ret;
			}
			if(c != '\\'){
				escape = false;
			}
			ret += c;
		}
		throw new ScriptSyntaxException("Double quotation is not closed", pos);
	}

	private double getLiteralRealNumber(String source) throws ScriptSyntaxException {
		String ret = "";
		while(pos < source.length()){
			char c = source.charAt(pos);
			if(!Character.isDigit(c) && c != '.' && c != '-'){
				break;
			}
			pos++;
			ret += c;
		}
		try {
			return Double.parseDouble(ret);
		} catch (NumberFormatException e) {
			throw new ScriptSyntaxException("Invalid Number",pos);
		}
	}

	private ScriptCommand getCommand(String name) {
		return ScriptCommand.getCommand(name);
	}

	private TreeElementType getTermType(ScriptTerm term) {
		return term.type;
	}
	
	//get a term at the beginning of the Section
	private ScriptTerm getTermSection(String source) throws ScriptSyntaxException {
		ScriptToken token;
		
		token = getToken(source);
		if(token.body.equals("//") || token.type == TokenType.BREAK) {
			seekNextLine(source);
			//prev_pos = pos;
			return getTermSection(source);
		}
		ScriptTerm term;
		/*if(token.body.equals("*")) {
			token = getToken(source);
			if(token.type != TokenType.ALPHABET) {
				throw new ScriptSyntaxException("Illegal token", pos);
			}
			term = new ScriptTerm(TreeElementType.LABEL, token.body);
			token = getToken(source);
			if(!token.body.equals("{")) {
				throw new ScriptSyntaxException("Unexpected token: expected ':'", pos);
			}
			return term;
		}*/
		if(token.type == TokenType.ALPHABET) {
			term = new ScriptTerm(TreeElementType.PROCEDURE, token.body);
			token = getToken(source);
			if(!token.body.equals("{")) {
				throw new ScriptSyntaxException("Unexpected token: expected '{'", pos);
			}
			return term;
		}
		if(token.type == TokenType.NUMBER) {
			term = new ScriptTerm(TreeElementType.ACTION, token.body);
			token = getToken(source);
			if(!token.body.equals("{")) {
				throw new ScriptSyntaxException("Unexpected token: expected '{'", pos);
			}
			return term;
		}
		if(token.body.equals("")) return null;
		throw new ScriptSyntaxException("Illegal token", pos);
	}

	private void seekNextLine(String source) {
		//��̃��[�v
		while(!getToken(source).body.equals("\n") && pos < source.length());
		
		if(pos < source.length()) {
			pos++;
		}
	}

	//lexical analyzer
	private ScriptToken getToken(String source) {
		TokenType cur_type = TokenType.NONE;
		String term = new String();
		while(pos < source.length()) {
			char c = source.charAt(pos);
			TokenType type = getTokenType(c);
			//if(cur_type != TokenType.NONE && type != cur_type){
			//	break;
			//}
			if(cur_type == TokenType.NUMBER){
				if(type != TokenType.NUMBER)break;
			}
			if(cur_type == TokenType.SIGN){
				if(type != TokenType.SIGN)break;
				if(c == '-') break;
			}
			if(cur_type == TokenType.ALPHABET){
				//if(type == TokenType.NONE || type == TokenType.BREAK)break;
				if(type != TokenType.ALPHABET && type != TokenType.NUMBER)break;
			}
			if(cur_type == TokenType.NONE) {
				if(type == TokenType.NONE){
					skipSpace(source);
					continue;
				}
				if(c == '\r') {
					pos++;
					continue;
				}
				if(type == TokenType.BREAK){
					return new ScriptToken(type, "\n");
				}
				if(c == '\"'){
					pos++;
					return new ScriptToken(TokenType.SIGN, "\"");
				}
				cur_type = type;
			}
			term = term + c;	//�����OK�炵��
			pos++;
		}
		skipSpace(source);
		return new ScriptToken(cur_type, term);
	}

	private TokenType getTokenType(char c) {
		if(c == ' ' || c == '\0' || c == '\t') {
			return TokenType.NONE;
		}
		if(c == '\n') {
			return TokenType.BREAK;
		}
		if(Character.isAlphabetic(c) || c == '_') {
			return TokenType.ALPHABET;
		}
		if(Character.isDigit(c)) {
			return TokenType.NUMBER;
		}
		for(int i = 0;i < sign_list.length;i++){
			if(c == sign_list[i]) {
				return TokenType.SIGN;
			}
		}
		return TokenType.UNKNOWN;
	}

	//���p�X�y�[�X�A�^�u���΂�
	private void skipSpace(String source) {
		while(pos < source.length()){
			char c = source.charAt(pos);
			if(c != ' ' && c != '\t' && c != '\0'){
				break;
			}
			pos++;
		}
	}
	
	//���p�X�y�[�X�A�^�u�A���s���΂�
	private void skipSpaceBreak(String source) {
		while(pos < source.length()){
			char c = source.charAt(pos);
			if(c != ' ' && c != '\t' && c != '\0' && c != '\n' && c != '\r'){
				break;
			}
			pos++;
		}
	}
}
