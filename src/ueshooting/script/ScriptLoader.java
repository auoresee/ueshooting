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
	 * スクリプトを解析し構文木を生成する
	 * ルート(階層0)
	 * @param source スクリプトのソース
	 * @return Script 構文木
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
	
	//階層1
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

	//階層2
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
				ltmp = toPostfixNotation(ltmp);	//逆ポーランド順に変換
				tmp.addChilds(ltmp);
				ret.add(tmp);
			}
			System.out.println(pos);
		}
		return ret;
	}

	private List<TreeElement> getAssignmentExpression(String source) throws ScriptSyntaxException {
		List<TreeElement> ret = new ArrayList<>();
		
		//左辺の変数を取得
		ParameterFormatSpecifier format = new ParameterFormatSpecifier(ParameterType.VAR_ANY);
		TreeElement child = getParameter(source, format);
		//ParameterType varType = getVariableParameterType((ScriptSpecialVariable) child.data);
		ret.add(child);
		
		//varTypeを変数型→値型に変換 (後の処理のため)　
		/*if(varType == ParameterType.VAR_ANY) varType = ParameterType.ANY;
		if(varType == ParameterType.VAR_INT || varType == ParameterType.VAR_DOUBLE) varType = ParameterType.DOUBLE_OR_INT;
		if(varType == ParameterType.VAR_STRING) varType = ParameterType.STRING;
		if(varType == ParameterType.VAR_BOOLEAN) varType = ParameterType.BOOLEAN;*/
		
		//代入演算子を取得
		format = new ParameterFormatSpecifier("=","+=","-=","*=","/=","%=","=!");
		child = getParameter(source, format);
		//if(!isOperatorCompatible((String) child.data, varType)){	//使えない演算子の組み合わせ(文字列型に-=など)ならエラー
		//	throw new ScriptSyntaxException(String.format("Incompatible operator %s with type %s", (String) child.data, varType.toString()), pos);
		//}
		ret.add(child);
		
		//右辺を取得
		List<TreeElement> elements = getExpression(source, ParameterType.ANY);
		elements = toPostfixNotation(elements);
		child = new TreeElement(TreeElementType.EXPRESSION);
		child.list = elements;
		ret.add(child);
		return ret;
	}

	/**
	 * 式を処理しトークンのリストを取得
	 * (逆ポーランド順にはしない)
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
	
	// 演算子
	// 優先順位 : 演算子 : 結合性
	// 4 :   !   : 右結合性
	// 3 : * / % : 左結合性
	// 2 :  + -  : 左結合性
	// 1 :   =   : 右結合性
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
	        // 左結合性
	        case "*": case "/": case "%": case "+": case "-":
	            return true;
	        // 右結合性
	        case "=": case "!":
	            return false;
	    }
	    return false;
	}
	
	/**
	 * 式を逆ポーランド記法に変換
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
	 
		TreeElement[] stack = new TreeElement[32];       // 演算子スタック
	    int stackLength = 0;  // スタック長（深さ）
	    TreeElement stackCounter;              // スタック要素の記録用
	 
	    while (index < input.size()) {
	        // 入力ストリームからトークンを1つ読み込む
	        c = input.get(index);
	        if (c.type.isLiteral() || c.type == TreeElementType.VARIABLE) {
	            // トークンが数値（識別子）なら、出力キューに追加する
	            output.add(c);
	        } else if (c.type == TreeElementType.FUNCTION_CALL) {
	            // トークンが関数なら、スタックにプッシュする。
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals(",")) {
	            // トークンが関数の引数のセパレータ（例えばカンマ）の場合
	            boolean pe = false;
	            while (stackLength > 0) {
	                stackCounter = stack[stack.length - 1];
	                if (((String)stackCounter.data).equals("(")) {
	                    pe = true;
	                    break;
	                } else {
	                    // スタックのトップのトークンが左括弧になるまで
	                    // スタック上の演算子を出力キューにポップし続ける
	                	output.add(stackCounter);
	                    stackLength--;
	                }
	            }
	            // 左括弧が出てこなかった場合、すなわちセパレータの位置が変だった場合
	            // あるいは括弧が正しく対応していない場合
	            if (!pe) {
	                System.out.printf("エラー：セパレータか括弧の不一致\n");
	                return null;
	            }
	        } else if (c.type == TreeElementType.OPERATOR && !((String)c.data).equals("(") && !((String)c.data).equals(")")) {
	            // トークンが演算子 op1 の場合
	            while (stackLength > 0) {
	                stackCounter = stack[stackLength - 1];
	                // op1 が左結合性で優先順位が op2 と等しいか低い場合
	                // あるいは op1 の優先順位が op2 より低い場合
	                // 演算子トークン op2 がスタックのトップにある間ループする。
	                // 1^2+3 のような式を正しく 12^3+ に変換するため
	                // "+" と "^" は右結合性とする。
	                // 演算子の優先順位の違いからポップするかプッシュするかを判断する。
	                // 2つの演算子の優先順位が等しいなら、結合性から判断する。
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
	            // op1 をスタックにプッシュ
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals("(")) {
	            // トークンが左括弧なら、スタックにプッシュ
	            stack[stackLength++] = c;
	        } else if (((String)c.data).equals(")")) {
	            // トークンが右括弧の場合
	            boolean pe = false;
	            // スタックのトップにあるトークンが左括弧になるまで
	            // スタックから出力キューに演算子をポップし続ける
	            while (stackLength > 0) {
	                stackCounter = stack[--stackLength];
	                if (((String)stackCounter.data).equals("(")) {
	                    // スタックから左括弧をポップするが、出力キューには置かない
	                    pe = true;
	                    break;
	                } else {
	                	output.add(stackCounter);
	                }
	            }
	            // スタックを全部見ても左括弧に到達しなかった場合、左右の括弧の不一致があることになる
	            if (!pe) {
	                System.out.printf("エラー：括弧の不一致\n");
	                return null;
	            }
	            // スタックのトップにあるトークンが関数トークンなら、それを出力キューにポップする
	            if (stackLength > 0) {
	                stackCounter = stack[stackLength - 1];
	                if (stackCounter.type == TreeElementType.FUNCTION_CALL) {
	                	output.add(stackCounter);
	                    stackLength--;
	                }
	            }
	        } else {
	        	System.out.printf("不明なトークン：%c\n", c);
	            return null; // 不明なトークン
	        }
	        index++;
	    }
	    // 読み込むべきトークンが尽きた際
	    // スタック上に演算子トークンが残っていたら、それらを出力する
	    while (stackLength > 0) {
	        stackCounter = stack[--stackLength];
	        if (((String)stackCounter.data).equals("(") || ((String)stackCounter.data).equals(")")) {
	            System.out.printf("エラー：括弧の不一致\n");
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
			token = getToken(source);	//一度戻したposを進めるため
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
				token = getToken(source);	//一度戻したposを進めるため
				if(token.body.equals("true")){
					parent = new TreeElement(TreeElementType.L_BOOLEAN);
					parent.data = true;
				}
				if(token.body.equals("false")){
					parent = new TreeElement(TreeElementType.L_BOOLEAN);
					parent.data = false;
				}
				else{
					if(!checkVarName(token.body)) {		//変数名が予約語でないかチェック
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
				token = getToken(source);	//一度戻したposを進めるため
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
	 * 変数名が予約語でないかチェック (未実装)
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
		//空のループ
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
			term = term + c;	//これでOKらしい
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

	//半角スペース、タブを飛ばす
	private void skipSpace(String source) {
		while(pos < source.length()){
			char c = source.charAt(pos);
			if(c != ' ' && c != '\t' && c != '\0'){
				break;
			}
			pos++;
		}
	}
	
	//半角スペース、タブ、改行を飛ばす
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
