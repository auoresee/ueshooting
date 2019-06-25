package ueshooting.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ueshooting.map.Map;
import ueshooting.sprite.ControlledByAI;
import ueshooting.sprite.ControlledShot;
import ueshooting.sprite.DelegatedAI;
import ueshooting.sprite.Enemy;
import ueshooting.sprite.Mob;
import ueshooting.sprite.Shot;
import ueshooting.sprite.Sprite;
import ueshooting.sprite.SpriteAI;
import ueshooting.stage.Script;
import ueshooting.stage.ScriptRuntimeException;
import ueshooting.system.Deg;
import ueshooting.system.SystemMain;

public class ScriptAI extends SpriteAI {
	Script scriptTree;
	Script globalTree;
	//CommandProcessor cp = new CommandProcessor();
	
	
	public ScriptAI(Enemy enemy, Script script, Script global, Map p) {
		super(enemy,p);
		scriptTree = script;
		globalTree = global;
	}
	
	public ScriptAI(Shot shot, Script script, Script global, Map p_map, double parameter) {
		super(shot,p_map);
		scriptTree = script;
		globalTree = global;
		w = parameter;
	}
	
	public void spawn() {
		TreeElement procedure = scriptTree.getNameProcedure("spawn");
		if(procedure == null) return;
		callRoutine(procedure);
	}

	public void destroy() {
		TreeElement procedure = scriptTree.getNameProcedure("destroy");
		if(procedure == null) return;
		callRoutine(procedure);
	}
	
	public void timeAction() {
		if(self.skip_frame > 0){
			return;
		}
		TreeElement procedure = scriptTree.getTimeAction(self.time);
		if(procedure == null) return;
		callRoutine(procedure);
	}
	
	public void update() {
		if(self.skip_frame > 0){
			self.skip_frame--;
			return;
		}
		TreeElement procedure = scriptTree.getNameProcedure("update");
		if(procedure == null) return;
		callRoutine(procedure);
	}
	
	public int a, b, c, d, e, f, g, h, i, j, k, l;
	public boolean m, n, o;
	public double p, q, r, s, t, u, v, w, x, y, z;
	public String B, C;
	public int[] D = new int[50], E = new int[50];
	public double[] F = new double[50], G = new double[50];
	public Stack<Object> stack = new Stack<>();
	public Stack<Object> parameter_stack = new Stack<>();
	
	Stack<List<Object>> call_stack = new Stack<>();	//条件分岐やループ関連の情報を保存
	Stack<Integer> loop_stack = new Stack<Integer>();
	int loop_counter = 0;
	boolean return_flag = false;
	boolean quit_flag = false;
	int if_end_count = 0;
	//loop_flag: led から whl / for に戻る瞬間だけtrueになる
	boolean loop_flag = false;
	int loop_end_count = 0;
	
	//外部からの呼び出しに使う
	public void callRoutine(TreeElement procedure) {
		executeRoutine(procedure);
		clean();
	}
	
	public void clean(){
		if(!call_stack.isEmpty()) {
			call_stack = new Stack<>();
		}
		loop_stack = new Stack<Integer>();
		loop_counter = 0;
		return_flag = false;
		quit_flag = false;
		if_end_count = 0;
		loop_flag = false;
		loop_end_count = 0;
	}
	
	private void clean_call_information(){
		loop_stack = new Stack<Integer>();
		loop_counter = 0;
		if_end_count = 0;
		loop_flag = false;
		loop_end_count = 0;
	}
	
	@SuppressWarnings("unchecked")
	private void load_call_information(){
		if(!call_stack.isEmpty()){
			List<Object> list = call_stack.pop();
			loop_stack = (Stack<Integer>) list.get(0);
			loop_counter = (int) list.get(1);
			if_end_count = (int) list.get(2);
			loop_flag = (boolean) list.get(3);
			loop_end_count = (int) list.get(4);
		}
	}
	
	private void save_call_information(){
		List<Object> list = new ArrayList<Object>();
		list.add(loop_stack);
		list.add(loop_counter);
		list.add(if_end_count);
		list.add(loop_flag);
		list.add(loop_end_count);
		call_stack.add(list);
	}
	
	/*
	 * private void executeRoutine(TreeElement procedure) {
		for (int i = 0;i < procedure.getChildNum();i++){
			TreeElement command = procedure.getChild(i);
			if((ScriptCommand)command.data == ScriptCommand.IF && if_end_count > 0){
				if_end_count++;
			}
			//この三つのコマンドの処理はここで行う(制御構文フラグによって無視されない)
			if((ScriptCommand)command.data == ScriptCommand.ELSE){
				if(if_end_count > 0){
					continue;
				}
				
				if(else_count > 0){
					else_count--;
				}
				else{
					if_end_count = 1;
				}
				continue;
			}
			if((ScriptCommand)command.data == ScriptCommand.IFEND){
				if(if_end_count > 0){
					if_end_count--;
				}
				continue;
			}
			if(else_count > 0 || if_end_count > 0) continue;
			if((ScriptCommand)command.data == ScriptCommand.LEND){
				if(loop_end_count > 0){
					loop_end_count--;
					loop_stack.pop();
					continue;
				}
				i = loop_stack.peek() - 1;
				loop_flag = true;
				continue;
			}
			if(loop_end_count > 0) continue;
			if((ScriptCommand)command.data == ScriptCommand.CONTINUE){
				i = loop_stack.peek() - 1;
				loop_flag = true;
				continue;
			}
			try {
				executeCommand(command);
			} catch (ScriptRuntimeException e) {
				e.printStackTrace();
				SystemMain.debugDialog(e.getMessage());
				SystemMain.pause();
				SystemMain.abort();
			}
			if(return_flag || quit_flag){
				break;
			}
		}
		
		if(else_count != 0) {
			SystemMain.debugDialog("Command \"els\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		if(if_end_count != 0) {
			SystemMain.debugDialog("Command \"ife\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		if(loop_end_count != 0) {
			SystemMain.debugDialog("Command \"led\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		return_flag = false;
	}
	 */
	
	private void executeRoutine(TreeElement procedure) {
		for (int i = 0;i < procedure.getChildNum();i++){
			TreeElement command = procedure.getChild(i);
			if((ScriptCommand)command.data == ScriptCommand.IF && if_end_count > 0){
				if_end_count++;
			}
			//この三つのコマンドの処理はここで行う(制御構文フラグによって無視されない)
			if((ScriptCommand)command.data == ScriptCommand.ELSE){
				if(if_end_count == 0){
					if_end_count = 1;
				}
				else if(if_end_count == 1){
					if_end_count = 0;
				}
				continue;
			}
			if((ScriptCommand)command.data == ScriptCommand.IFEND){
				if(if_end_count > 0){
					if_end_count--;
				}
				continue;
			}
			if(if_end_count > 0 || if_end_count > 0) continue;
			if((ScriptCommand)command.data == ScriptCommand.LEND){
				if(loop_end_count > 0){
					loop_end_count--;
					loop_stack.pop();
					continue;
				}
				i = loop_stack.peek() - 1;
				loop_flag = true;
				continue;
			}
			if(loop_end_count > 0) continue;
			if((ScriptCommand)command.data == ScriptCommand.CONTINUE){
				i = loop_stack.peek() - 1;
				loop_flag = true;
				continue;
			}
			try {
				executeCommand(command);
			} catch (ScriptRuntimeException e) {
				e.printStackTrace();
				SystemMain.debugDialog(e.getMessage());
				SystemMain.pause();
				SystemMain.abort();
			}
			if(return_flag || quit_flag){
				break;
			}
		}
		
		if(if_end_count != 0) {
			SystemMain.debugDialog("Command \"els\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		if(if_end_count != 0) {
			SystemMain.debugDialog("Command \"ife\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		if(loop_end_count != 0) {
			SystemMain.debugDialog("Command \"led\" is expected");
			SystemMain.pause();
			SystemMain.abort();
		}
		return_flag = false;
	}

	int last_shot_id = -1;
	
	private void executeCommand(TreeElement command) throws ScriptRuntimeException {
		Script currentTree = (Script)command.parent.parent;
		switch((ScriptCommand)command.data){
		case MOVE:
			assignVariableValue((ScriptVariable)command.getChild(0).data, command.getChild(2));
			break;
		case PRINT:
			System.out.println(command.getChild(0).data.toString());
			break;
		case PUSH:
			stack.push(getValueOfElement(command.getChild(0)));
			break;
		case POP:
			assignVariableValue((ScriptVariable)command.getChild(0).data, stack.pop());
			break;
		case STYPE:
			if(getVariableType((ScriptVariable)command.getChild(0).data) == int.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, 0);
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == boolean.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, 1);
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == double.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, 2);
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == String.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, 3);
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == null){
				throw new RuntimeException();
			}
			break;
			
		case CALL:
			TreeElement procedure;
			procedure = currentTree.getNameProcedure((String)command.getChild(0).data);
			if(procedure == null){
				if(currentTree != globalTree){
					procedure = globalTree.getNameProcedure((String)command.getChild(0).data);
					if(procedure == null){
						throw new ScriptRuntimeException(String.format("No such method: \"%s\"", (String)command.getChild(3).data));
					}
				}else{
					throw new ScriptRuntimeException(String.format("No such method: \"%s\"", (String)command.getChild(3).data));
				}
			}
			save_call_information();
			clean_call_information();
			executeRoutine(procedure);
			load_call_information();
			break;
		case JUMP:
			if(logicalOperation(getValueOfElement(command.getChild(0)),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2)))){
				procedure = currentTree.getNameProcedure((String)command.getChild(3).data);
				if(procedure == null){
					if(currentTree != globalTree){
						procedure = globalTree.getNameProcedure((String)command.getChild(3).data);
						if(procedure == null){
							throw new ScriptRuntimeException(String.format("No such method: \"%s\"", (String)command.getChild(3).data));
						}
					}else{
						throw new ScriptRuntimeException(String.format("No such method: \"%s\"", (String)command.getChild(3).data));
					}
				}
				executeRoutine(procedure);
			}
			break;
		case IF:
			if(!logicalOperation(getValueOfElement(command.getChild(0)),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2)))){
				if_end_count++;
			}
			break;
		case BIF:
			if(!(boolean)getValueOfElement(command.getChild(0))){
				if_end_count++;
			}
			break;
		case ELSE:
			break;
		case ELSEIF:
			if(if_end_count == 1){
				if(logicalOperation(getValueOfElement(command.getChild(0)),
						(String)command.getChild(1).data,getValueOfElement(command.getChild(2)))){
					if_end_count = 0;
				}
			}
			else if(if_end_count == 0){
				if_end_count = 1;
			}
			break;
		case IFEND:
			break;
		case WHILE:
			if(!logicalOperation(getValueOfElement(command.getChild(0)),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2)))){
				loop_end_count++;
			}
			loop_flag = false;
			break;
		case FOR:
			if(loop_flag != true){
				assignVariableValue((ScriptVariable)command.getChild(0).data, command.getChild(1));
				if((double)getValueOfElement(command.getChild(0)) >= (double)getValueOfElement(command.getChild(3))){
					loop_end_count++;
					break;
				}
			}
			else{
				loop_flag = false;
				if((double)getValueOfElement(command.getChild(0)) >= (double)getValueOfElement(command.getChild(3))){
					loop_end_count++;
					break;
				}
				assignVariableValue((ScriptVariable)command.getChild(0).data, (double)getValueOfElement(command.getChild(0)) + (double)getValueOfElement(command.getChild(3)));
			}
			break;
		case BREAK:
			loop_end_count++;
			break;
			
		case LET:
			double value;
			if(command.getChild(0).type == TreeElementType.L_BOOLEAN){
				if(((String)command.getChild(1).data).equals("=")){
					assignVariableValue((ScriptVariable)command.getChild(0).data, (boolean)getValueOfElement(command.getChild(2)));
					break;
				}
				if(((String)command.getChild(1).data).equals("=!")){
					assignVariableValue((ScriptVariable)command.getChild(0).data, !(boolean)getValueOfElement(command.getChild(2)));
					break;
				}
				else {
					assignVariableValue((ScriptVariable)command.getChild(0).data, logicalOperation(getValueOfElement(command.getChild(0)),
							(String)command.getChild(1).data,getValueOfElement(command.getChild(2))));
				}
			}
			if(command.getChild(0).type == TreeElementType.L_STRING){
				if(((String)command.getChild(1).data).equals("=")){
					assignVariableValue((ScriptVariable)command.getChild(0).data, (String)getValueOfElement(command.getChild(2)));
				}
				else if(((String)command.getChild(1).data).equals("+=")){
					assignVariableValue((ScriptVariable)command.getChild(0).data, (String)getValueOfElement(command.getChild(0)) + (String)getValueOfElement(command.getChild(2)));
				}
				break;
			}
			List<TreeElement> expression = command.getChild(2).list;
			Stack<Double> operandStack = new Stack<>();
			for(int i = 0;i < expression.size();i++){
				TreeElement cur = expression.get(i);
				if(cur.type == TreeElementType.L_DOUBLE || cur.type == TreeElementType.L_INT ||
						cur.type == TreeElementType.VARIABLE){
					operandStack.push(toDouble(getValueOfElement(cur)));
				}
				if(cur.type == TreeElementType.OPERATOR){
					double value1;
					double value2;
					double result;
					value2 = operandStack.pop();
					value1 = operandStack.pop();
					switch((String)cur.data){
					case "+":
						result = value1 + value2;
						break;
					case "-":
						result = value1 - value2;
						break;
					case "*":
						result = value1 * value2;
						break;
					case "/":
						result = value1 / value2;
						break;
					case "^":
						result = Math.pow(value1, value2);	//value1 の value2乗
						break;
					case "%":
						result = value1 % value2;
						break;
					default:
						throw new ScriptRuntimeException("Invalid operator");
					}
					operandStack.push(result);
				}
			}
			
			double value1 = toDouble(getValueOfElement(command.getChild(0)));
			double value2 = operandStack.pop();		//最後にスタックに残った値が結果
			double result;
			switch((String)command.getChild(1).data){
			case "=":
				result = value2;
				break;
			case "+=":
				result = value1 + value2;
				break;
			case "-=":
				result = value1 - value2;
				break;
			case "*=":
				result = value1 * value2;
				break;
			case "/=":
				result = value1 / value2;
				break;
			case "%=":
				result = value1 % value2;
				break;
			default:
				throw new ScriptRuntimeException("Invalid operator");
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == int.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, (int)result);
			}
			else if(getVariableType((ScriptVariable)command.getChild(0).data) == double.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, result);
			}
			else {
				for(;;);
			}
			break;
		case LETV:
			if((String)command.getChild(1).data == "=!"){
				assignVariableValue((ScriptVariable)command.getChild(0).data, !(boolean)getValueOfElement(command.getChild(2)));
				break;
			}
			value1 = (double)getValueOfElement(command.getChild(0));
			value2 = (double)getValueOfElement(command.getChild(2));
			switch((String)command.getChild(1).data){
			case "+=":
				result = value1 + value2;
				break;
			case "-=":
				result = value1 - value2;
				break;
			case "*=":
				result = value1 * value2;
				break;
			case "/=":
				result = value1 / value2;
				break;
			case "%=":
				result = value1 % value2;
				break;
			default:
				throw new ScriptRuntimeException("Invalid operator");
			}
			if(getVariableType((ScriptVariable)command.getChild(0).data) == int.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, (int)result);
			}
			else if(getVariableType((ScriptVariable)command.getChild(0).data) == double.class){
				assignVariableValue((ScriptVariable)command.getChild(0).data, result);
			}
			break;
		case CMP:
			assignVariableValue((ScriptVariable)command.getChild(0).data, (boolean)logicalOperation(getValueOfElement(command.getChild(1))
					, (String)getValueOfElement(command.getChild(2)), getValueOfElement(command.getChild(3))));
			break;
		case SQRT:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Math.sqrt((double)getValueOfElement(command.getChild(1))));
			break;
		case EXP:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Math.pow((double)getValueOfElement(command.getChild(1)),
					(double)getValueOfElement(command.getChild(2))));
			break;
		case TORECT:
			double radius = (double)getValueOfElement(command.getChild(0));
			double theta = (double)getValueOfElement(command.getChild(1));
			double pos_x = Deg.cos(theta) * radius;
			double pos_y = Deg.sin(theta) * radius;
			assignVariableValue(ScriptVariable.VAR_p, pos_x);
			assignVariableValue(ScriptVariable.VAR_q, pos_y);
			break;
		case TOPOLAR:
			pos_x = (double)getValueOfElement(command.getChild(0));
			pos_y = (double)getValueOfElement(command.getChild(1));
			radius = Math.sqrt(pos_x * pos_x + pos_y * pos_y);
			theta = Deg.atan2(pos_y, pos_x);
			assignVariableValue(ScriptVariable.VAR_p, radius);
			assignVariableValue(ScriptVariable.VAR_q, theta);
			break;
		case DISTANCE:
			pos_x = (double)getValueOfElement(command.getChild(1));
			pos_y = (double)getValueOfElement(command.getChild(2));
			assignVariableValue((ScriptVariable)command.getChild(0).data, Math.sqrt(pos_x * pos_x + pos_y * pos_y));
			break;
		case SIN:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Deg.sin(toDouble(getValueOfElement(command.getChild(1)))));
			break;
		case COS:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Deg.cos(toDouble(getValueOfElement(command.getChild(1)))));
			break;
		case TAN:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Deg.tan(toDouble(getValueOfElement(command.getChild(1)))));
			break;
		case ATAN:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Deg.atan(toDouble(getValueOfElement(command.getChild(1)))));
			break;
		case ATAN2:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Deg.atan2(toDouble(getValueOfElement(command.getChild(1))),toDouble(getValueOfElement(command.getChild(1)))));
			break;
		case LOG:
			value = Math.log(toDouble(getValueOfElement(command.getChild(2)))) / Math.log(toDouble(getValueOfElement(command.getChild(1))));
			assignVariableValue((ScriptVariable)command.getChild(0).data, value);
			break;
		case RANDOM:
			assignVariableValue((ScriptVariable)command.getChild(0).data, Math.random());
			break;
		case RANDOM2:
			int a = (int) ((toDouble(getValueOfElement(command.getChild(2))) - toDouble(getValueOfElement(command.getChild(1)))) + toDouble(getValueOfElement(command.getChild(2))));
			assignVariableValue((ScriptVariable)command.getChild(0).data, Math.random() * 
					(toDouble(getValueOfElement(command.getChild(2))) - toDouble(getValueOfElement(command.getChild(1)))) + toDouble(getValueOfElement(command.getChild(1))));
			break;
			
		case DEBUG:
			SystemMain.debugDialog("Script: Break");
			break;
		case RETURN:
			return_flag = true;
			break;
		case END:
			quit_flag = true;
			self.time = Integer.MAX_VALUE;
			break;
		case DIE:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			quit_flag = true;
			((Enemy)self).hp = 0;
			break;
		case DEL:
			map.getSprite((int) getValueOfElement(command.getChild(0))).time = Integer.MAX_VALUE;
			break;
		case KIL:
			((Mob)map.getSprite((int) getValueOfElement(command.getChild(0)))).hp = 0;
			break;
		case HIDE:
			self.visiblity = false;
			break;
		case APPEAR:
			self.visiblity = true;
			break;
		case SKIP:
			//実際にフレームを飛ばす処理は未実装
			self.skip_frame = (int)getValueOfElement(command.getChild(0));
			break;
		case EFFECT:
			//未実装
			map.effect((int)getValueOfElement(command.getChild(0)), toDouble(getValueOfElement(command.getChild(1))),
					toDouble(getValueOfElement(command.getChild(2))), toDouble(getValueOfElement(command.getChild(3))));
			break;
		case EVENT:
		{
			int id = (int)getValueOfElement(command.getChild(0));
			if(id == 10){
				map.startSpellCard((String) parameter_stack.pop());
			}
			if(id == 11){
				map.endSpellCard();
			}
		}
			break;
		case SOUND:
			SystemMain.soundManager.playClip((String)getValueOfElement(command.getChild(0)), (int)getValueOfElement(command.getChild(1)),toDouble(getValueOfElement(command.getChild(2))));
			break;
		case BGM:
			break;
		case COLLIDE:
			self.setCollisionFlag((boolean)getValueOfElement(command.getChild(0)));
			break;
		case STGCLEAR:
			map.stageWin();
			break;
		case SHOOT:
			Shot shot;
			if((shot = map.getControlledShot((int)getValueOfElement(command.getChild(0)), (int)getValueOfElement(command.getChild(1)),
					self.getX_double(), self.getY_double(), toDouble(getValueOfElement(command.getChild(4))), 1)) == null){
				shot = new Shot((int)getValueOfElement(command.getChild(0)), (int)getValueOfElement(command.getChild(1)),
					self.getX_double(), self.getY_double(), toDouble(getValueOfElement(command.getChild(4))), 1);
			}
			shot.shoot_angle(toDouble(getValueOfElement(command.getChild(2))), toDouble(getValueOfElement(command.getChild(3))));
			last_shot_id = map.setSprite(shot);
			break;
		case SETSHOT:
			if((shot = map.getControlledShot((int)getValueOfElement(command.getChild(0)), (int)getValueOfElement(command.getChild(1)),
					toDouble(getValueOfElement(command.getChild(2))), toDouble(getValueOfElement(command.getChild(3))),
					toDouble(getValueOfElement(command.getChild(6))), 1)) == null){
				shot = new Shot((int)getValueOfElement(command.getChild(0)), (int)getValueOfElement(command.getChild(1)),
						toDouble(getValueOfElement(command.getChild(2))), toDouble(getValueOfElement(command.getChild(3))),
						toDouble(getValueOfElement(command.getChild(6))), 1);
			}
			shot.shoot_angle(toDouble(getValueOfElement(command.getChild(4))), toDouble(getValueOfElement(command.getChild(5))));
			last_shot_id = map.setSprite(shot);
			break;
		case GETID:
			assignVariableValue((ScriptVariable)command.getChild(0).data, last_shot_id);
			break;
		case SSPEED:
			Sprite sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			sprite.x_speed = (double)getValueOfElement(command.getChild(1));
			sprite.y_speed = (double)getValueOfElement(command.getChild(2));
			break;
		case SSPEEDP:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			sprite.x_speed = Deg.cos(toDouble(getValueOfElement(command.getChild(2)))) * toDouble(getValueOfElement(command.getChild(1)));
			sprite.y_speed = Deg.sin(toDouble(getValueOfElement(command.getChild(2)))) * toDouble(getValueOfElement(command.getChild(1)));
			break;
		case SANGLE:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			radius = Math.sqrt(sprite.x_speed*sprite.x_speed + sprite.y_speed*sprite.y_speed);
			theta = (double)getValueOfElement(command.getChild(0));
			sprite.x_speed = Deg.cos(theta) * radius;
			sprite.y_speed = Deg.sin(theta) * radius;
			break;
		case SROTATE:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			sprite.rotation = toDouble(getValueOfElement(command.getChild(1)));
			break;
		case VSPEED:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			assignVariableValue((ScriptVariable)command.getChild(1).data, sprite.x_speed);
			assignVariableValue((ScriptVariable)command.getChild(2).data, sprite.y_speed);
			break;
		case VSPEEDP:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			radius = Math.sqrt(sprite.x_speed*sprite.x_speed + sprite.y_speed*sprite.y_speed);
			theta = Deg.atan2(sprite.y_speed, sprite.x_speed);
			assignVariableValue((ScriptVariable)command.getChild(1).data, radius);
			assignVariableValue((ScriptVariable)command.getChild(2).data, theta);
			break;
		case VROTATE:
			sprite = map.getSprite((int)getValueOfElement(command.getChild(0)));
			assignVariableValue((ScriptVariable)command.getChild(1).data, sprite.rotation);
			break;
		case CTRLM:
			shot = (Shot) map.getSprite((int)getValueOfElement(command.getChild(0)));
			shot.controlMovement((int)getValueOfElement(command.getChild(1)), toDouble(getValueOfElement(command.getChild(2))),
					toDouble(getValueOfElement(command.getChild(3))),toDouble(getValueOfElement(command.getChild(4))));
			break;
		case CTRLA:
			shot = (Shot) map.getSprite((int)getValueOfElement(command.getChild(0)));
			shot.controlAction((int)getValueOfElement(command.getChild(1)), toDouble(getValueOfElement(command.getChild(2))),
					toDouble(getValueOfElement(command.getChild(3))),toDouble(getValueOfElement(command.getChild(4))));
			break;
			
		case DLG:
			map.delegate((int)getValueOfElement(command.getChild(0)),self,map,(int)getValueOfElement(command.getChild(1)));
			break;
		case RUN:
			//未実装
			externalCall((String)getValueOfElement(command.getChild(0)));
			break;
		case PRM:
			parameter_stack.push(getValueOfElement(command.getChild(0)));
			break;
			
		case PCOORD:
			assignVariableValue(ScriptVariable.VAR_p, map.getPlayer().getX_double());
			assignVariableValue(ScriptVariable.VAR_q, map.getPlayer().getY_double());
			break;
		case PSPEED:
			assignVariableValue(ScriptVariable.VAR_p, map.getPlayer().x_speed);
			assignVariableValue(ScriptVariable.VAR_q, map.getPlayer().y_speed);
			break;
		case PSPEEDP:
			pos_x = map.getPlayer().x_speed;
			pos_y = map.getPlayer().y_speed;
			radius = Math.sqrt(pos_x * pos_x + pos_y * pos_y);
			theta = Deg.atan2(pos_y, pos_x);
			break;
			
		case POS:
			assignVariableValue(ScriptVariable.VAR_X, toDouble(getValueOfElement(command.getChild(0))));
			assignVariableValue(ScriptVariable.VAR_Y, toDouble(getValueOfElement(command.getChild(1))));
			break;
		case SPEED:
			assignVariableValue(ScriptVariable.VAR_I, toDouble(getValueOfElement(command.getChild(0))));
			assignVariableValue(ScriptVariable.VAR_J, toDouble(getValueOfElement(command.getChild(1))));
			break;
		case SPEEDP:
			assignVariableValue(ScriptVariable.VAR_V, toDouble(getValueOfElement(command.getChild(0))));
			assignVariableValue(ScriptVariable.VAR_A, toDouble(getValueOfElement(command.getChild(1))));
			break;
			
		default:
			throw new RuntimeException("Unexpected error");
		}
	}
	
	/**
	 * is numeric literal/variable
	 */
	private boolean isNumber(TreeElement child) {
		if(child.type == TreeElementType.VARIABLE){
			@SuppressWarnings("rawtypes")
			Class c = getVariableType((ScriptVariable)child.data);
			return c == int.class || c == double.class;
		}
		return child.data == int.class || child.data == double.class;
	}

	private double toDouble(Object value) {
		if(value.getClass() == Integer.class){
			return ((Integer)value).doubleValue();
		}
		return (double)value;
	}

	private void externalCall(String methodName) {
		// TODO Auto-generated method stub
		
	}

	private boolean logicalOperation(Object value1, String operator,
			Object value2) {
		switch(operator){
		case "==":
			if(value1 instanceof String){
				return value1.equals(value2);
			}
			if(value1 instanceof Integer || value1 instanceof Double){
				return toDouble(value1) == toDouble(value2);
			}
			return value1 == value2;
			
		case "!=":
			if(value1 instanceof String){
				return !value1.equals(value2);
			}
			if(value1 instanceof Integer || value1 instanceof Double){
				return !(toDouble(value1) == toDouble(value2));
			}
			return !(value1 == value2);
			
		case "=!":
			return !(boolean)value1;
			
		case ">":
			return ((Number)value1).doubleValue() > ((Number)value2).doubleValue();
			
		case "<":
			return ((Number)value1).doubleValue() < ((Number)value2).doubleValue();
			
		case ">=":
			return ((Number)value1).doubleValue() >= ((Number)value2).doubleValue();
			
		case "<=":
			return ((Number)value1).doubleValue() <= ((Number)value2).doubleValue();
			
		case "&&":
			return (boolean)value1 && (boolean)value2;
			
		case "||":
			return (boolean)value1 || (boolean)value2;
		}
		return false;
	}

	private Object getValueOfElement(TreeElement element) throws ScriptRuntimeException {
		Object ret;
		if(element.type == TreeElementType.VARIABLE){
			ret = getValueOfVariable((ScriptVariable)element.data);
			if(ret.getClass() == Integer.class) ret = (int)ret;
		}
		else{
			ret = element.data;
			if(ret.getClass() == Integer.class) ret = (int)ret;
		}
		return ret;
	}

	private Object getValueOfVariable(ScriptVariable var) throws ScriptRuntimeException {
		switch(var){
		case VAR_a:
			return a;
		case VAR_b:
			return b;
		case VAR_c:
			return c;
		case VAR_d:
			return d;
		case VAR_e:
			return e;
		case VAR_f:
			return f;
		case VAR_g:
			return g;
		case VAR_h:
			return h;
		case VAR_i:
			return i;
		case VAR_j:
			return j;
		case VAR_k:
			return k;
		case VAR_l:
			return l;
		case VAR_T:
			return self.time;
		case VAR_D:
			return D[var.index];
		case VAR_E:
			return E[var.index];

		case VAR_m:
			return m;
		case VAR_n:
			return n;
		case VAR_o:
			return o;
			
		case VAR_p:
			return p;
		case VAR_q:
			return q;
		case VAR_r:
			return r;
		case VAR_s:
			return s;
		case VAR_t:
			return t;
		case VAR_u:
			return u;
		case VAR_v:
			return v;
		case VAR_w:
			return w;
		case VAR_x:
			return x;
		case VAR_y:
			return y;
		case VAR_z:
			return z;
		case VAR_R:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).rotation;
		case VAR_H:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).hp;
		case VAR_I:
			return self.x_speed;
		case VAR_J:
			return self.y_speed;
		case VAR_P:
			return Math.PI;
		case VAR_A:
			return Deg.atan2(self.y_speed, self.x_speed);
		case VAR_V:
			return Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
		case VAR_X:
			return self.getX_double();
		case VAR_Y:
			return self.getY_double();
		case VAR_B:
			return B;
		case VAR_C:
			return C;
		case VAR_F:
			return F[var.index];
		case VAR_G:
			return G[var.index];
		case VAR_N:
			return null;
		}
		return null;
	}

	private void assignVariableValue(ScriptVariable dest, TreeElement src) throws ScriptRuntimeException {
		if(src.type == TreeElementType.VARIABLE){
			if(getVariableType(dest) != getVariableType((ScriptVariable)src.data)) {
				throw new RuntimeException("Unexpected exception");
			}
			assignVariableValue(dest, getValueOfVariable((ScriptVariable)src.data));
		}
		else{
			assignVariableValue(dest, src.data);
		}
	}

	private void assignVariableValue(ScriptVariable dest, Object src) throws ScriptRuntimeException {
		if(getVariableType(dest) == int.class && src.getClass() != Integer.class && src.getClass() != Double.class ||
			getVariableType(dest) == boolean.class && src.getClass() != Boolean.class ||
			getVariableType(dest) == double.class && src.getClass() != Double.class && src.getClass() != Integer.class ||
			getVariableType(dest) == String.class && src.getClass() != String.class) {
			throw new RuntimeException("Unexpected exception");
		}
		switch(dest){
		case VAR_a:
			a = ((Number)src).intValue();
			break;
		case VAR_b:
			b = ((Number)src).intValue();
			break;
		case VAR_c:
			c = ((Number)src).intValue();
			break;
		case VAR_d:
			d = ((Number)src).intValue();
			break;
		case VAR_e:
			e = ((Number)src).intValue();
			break;
		case VAR_f:
			f = ((Number)src).intValue();
			break;
		case VAR_g:
			g = ((Number)src).intValue();
			break;
		case VAR_h:
			h = ((Number)src).intValue();
			break;
		case VAR_i:
			i = ((Number)src).intValue();
			break;
		case VAR_j:
			j = ((Number)src).intValue();
			break;
		case VAR_k:
			k = ((Number)src).intValue();
			break;
		case VAR_l:
			l = ((Number)src).intValue();
			break;
		case VAR_T:
			self.time = ((Number)src).intValue();
			break;
		case VAR_D:
			D[dest.index] = ((Number)src).intValue();
			break;
		case VAR_E:
			E[dest.index] = ((Number)src).intValue();
			break;
			
		case VAR_m:
			m = (boolean) src;
			break;
		case VAR_n:
			n = (boolean) src;
			break;
		case VAR_o:
			o = (boolean) src;
			break;
			
		case VAR_p:
			if(self instanceof ControlledShot){
				p = 0;
			}
			p = ((Number)src).doubleValue();
			break;
		case VAR_q:
			q = ((Number)src).doubleValue();
			break;
		case VAR_r:
			r = ((Number)src).doubleValue();
			break;
		case VAR_s:
			s = ((Number)src).doubleValue();
			break;
		case VAR_t:
			t = ((Number)src).doubleValue();
			break;
		case VAR_u:
			u = ((Number)src).doubleValue();
			break;
		case VAR_v:
			v = ((Number)src).doubleValue();
			break;
		case VAR_w:
			w = ((Number)src).doubleValue();
			break;
		case VAR_x:
			x = (double) src;
			break;
		case VAR_y:
			y = ((Number)src).doubleValue();
			break;
		case VAR_z:
			z = ((Number)src).doubleValue();
			break;
		case VAR_R:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).rotation = ((Number)src).doubleValue();
			break;
		case VAR_H:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).hp = ((Number)src).doubleValue();
			break;
		case VAR_I:
			self.x_speed = ((Number)src).doubleValue();
			break;
		case VAR_J:
			self.y_speed = ((Number)src).doubleValue();
			break;
		case VAR_P:
			throw new ScriptRuntimeException("Assignment to a constance");
		case VAR_A:
			double speed = Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
			self.x_speed = Deg.cos(((Number)src).doubleValue()) * speed;
			self.y_speed = Deg.sin(((Number)src).doubleValue()) * speed;
			break;
		case VAR_V:
			double angle = Deg.atan2(self.y_speed, self.x_speed);
			self.x_speed = Deg.cos(angle) * ((Number)src).doubleValue();
			self.y_speed = Deg.sin(angle) * ((Number)src).doubleValue();
			break;
		case VAR_X:
			self.setX(((Number)src).doubleValue());
			break;
		case VAR_Y:
			self.setY(((Number)src).doubleValue());
			break;
		case VAR_F:
			F[dest.index] = ((Number)src).doubleValue();
			break;
		case VAR_G:
			G[dest.index] = ((Number)src).doubleValue();
			break;
			
		case VAR_B:
			B = (String) src;
			break;
		case VAR_C:
			C = (String) src;
			break;
			
		case VAR_N:
			//どこにも代入しない
			break;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Class getVariableType(ScriptVariable var) {
		switch(var){
		case VAR_a:
		case VAR_b:
		case VAR_c:
		case VAR_d:
		case VAR_e:
		case VAR_f:
		case VAR_g:
		case VAR_h:
		case VAR_i:
		case VAR_j:
		case VAR_k:
		case VAR_l:
		case VAR_T:
		case VAR_D:
		case VAR_E:
			return int.class;
			
		case VAR_m:
		case VAR_n:
		case VAR_o:
			return boolean.class;
			
		case VAR_p:
		case VAR_q:
		case VAR_r:
		case VAR_s:
		case VAR_t:
		case VAR_u:
		case VAR_v:
		case VAR_w:
		case VAR_x:
		case VAR_y:
		case VAR_z:
		case VAR_A:
		case VAR_R:
		case VAR_H:
		case VAR_I:
		case VAR_J:
		case VAR_P:
		case VAR_V:
		case VAR_X:
		case VAR_Y:
		case VAR_F:
		case VAR_G:
			return double.class;
			
		case VAR_B:
		case VAR_C:
			return String.class;
			
		case VAR_N:
			return null;
		}
		return null;
	}
	
}
