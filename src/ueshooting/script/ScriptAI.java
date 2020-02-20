package ueshooting.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ueshooting.main.GameMain;
import ueshooting.map.Map;
import ueshooting.sprite.ControlledByAI;
import ueshooting.sprite.ControlledShot;
import ueshooting.sprite.DelegatedAI;
import ueshooting.sprite.Enemy;
import ueshooting.sprite.EnemyGenerator;
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
	
	Object scriptArg;
	
	
	public ScriptAI(Enemy enemy, Script script, Script global, Map p) {
		super(enemy,p);
		scriptTree = script;
		globalTree = global;
		initGlobalVariables();
	}
	
	public ScriptAI(ControlledShot enemy, Script script, Script global, Map p, Object option) {
		super(enemy,p);
		scriptTree = script;
		globalTree = global;
		initGlobalVariables();
		scriptArg = option;
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
	
	public Stack<Object> stack = new Stack<>();
	public Stack<Object> parameter_stack = new Stack<>();
	
	//public List<ScriptVariable> globalVariables = new ArrayList<>();
	//public List<ScriptVariable> localVariables = new ArrayList<>();
	public HashMap<String, ScriptVariable> globalVariables;
	public HashMap<String, ScriptVariable> currentLocalVariables = new HashMap<>();
	
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
	
	private void initGlobalVariables() {
		HashMap<String, ScriptVariable> ret = new HashMap<>();
		ret.put("s_time", new ScriptVariableSpecial("s_time", DataType.INT, self));
		ret.put("s_rotation", new ScriptVariableSpecial("s_rotation", DataType.DOUBLE, self));
		ret.put("s_hp", new ScriptVariableSpecial("s_hp", DataType.INT, self));
		ret.put("s_x", new ScriptVariableSpecial("s_x", DataType.DOUBLE, self));
		ret.put("s_y", new ScriptVariableSpecial("s_y", DataType.DOUBLE, self));
		ret.put("s_angle", new ScriptVariableSpecial("s_angle", DataType.DOUBLE, self));
		ret.put("s_speed", new ScriptVariableSpecial("s_speed", DataType.DOUBLE, self));
		ret.put("s_xspeed", new ScriptVariableSpecial("s_xspeed", DataType.DOUBLE, self));
		ret.put("s_yspeed", new ScriptVariableSpecial("s_yspeed", DataType.DOUBLE, self));
		ret.put("M_PI", new ScriptVariableSpecial("M_PI", DataType.DOUBLE, self));
		ret.put("null", new ScriptVariableSpecial("null", DataType.ARRAY_INT, self));
		
		globalVariables = ret;
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
			currentLocalVariables = (HashMap<String, ScriptVariable>) list.get(5);
		}
	}
	
	private void save_call_information(){
		List<Object> list = new ArrayList<Object>();
		list.add(loop_stack);
		list.add(loop_counter);
		list.add(if_end_count);
		list.add(loop_flag);
		list.add(loop_end_count);
		list.add(currentLocalVariables);
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
			assignVariableValue(getVariable((String)command.getChild(0).data), command.getChild(2));
			break;
		case PRINT:
			System.out.println(command.getChild(0).data.toString());
			break;
		case PUSH:
			stack.push(getValueOfElement(command.getChild(0), DataType.ANY));
			break;
		case POP:
			assignVariableValue(getVariable((String)command.getChild(0).data), stack.pop());
			break;
		case STYPE:
			if(getVariable((String)command.getChild(0).data).type == DataType.INT){
				assignVariableValue(getVariable((String)command.getChild(0).data), 0);
			}
			if(getVariable((String)command.getChild(0).data).type == DataType.BOOLEAN){
				assignVariableValue(getVariable((String)command.getChild(0).data), 1);
			}
			if(getVariable((String)command.getChild(0).data).type == DataType.DOUBLE){
				assignVariableValue(getVariable((String)command.getChild(0).data), 2);
			}
			if(getVariable((String)command.getChild(0).data).type == DataType.STRING){
				assignVariableValue(getVariable((String)command.getChild(0).data), 3);
			}
			break;
			
		case DIM:
			String typeName = (String) command.getChild(0).data;
			DataType vtype = DataType.getType(typeName);
			String vname = (String) command.getChild(1).data;
			if(command.getChildNum() == 3) {
				Object initVal = command.getChild(2).data;
				currentLocalVariables.put(vname, new ScriptVariable(vname, vtype, initVal));
			} else {
				currentLocalVariables.put(vname, new ScriptVariable(vname, vtype));
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
			if(compareOperation(getValueOfElement(command.getChild(0), DataType.ANY),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2), DataType.ANY))){
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
			if(!compareOperation(getValueOfElement(command.getChild(0), DataType.ANY),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2), DataType.ANY))){
				if_end_count++;
			}
			break;
		case BIF:
			if(!(boolean)getValueOfElement(command.getChild(0),DataType.BOOLEAN)){
				if_end_count++;
			}
			break;
		case ELSE:
			break;
		case ELSEIF:
			if(if_end_count == 1){
				if(compareOperation(getValueOfElement(command.getChild(0), DataType.ANY),
						(String)command.getChild(1).data,getValueOfElement(command.getChild(2), DataType.ANY))){
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
			if(!compareOperation(getValueOfElement(command.getChild(0), DataType.ANY),
					(String)command.getChild(1).data,getValueOfElement(command.getChild(2), DataType.ANY))){
				loop_end_count++;
			}
			loop_flag = false;
			break;
		case FOR:
			if(loop_flag != true){
				assignVariableValue(getVariable((String)command.getChild(0).data), command.getChild(1));
				if(getValueOfElementDouble(command.getChild(0)) >= getValueOfElementDouble(command.getChild(3))){
					loop_end_count++;
					break;
				}
			}
			else{
				loop_flag = false;
				if(getValueOfElementDouble(command.getChild(0)) >= getValueOfElementDouble(command.getChild(3))){
					loop_end_count++;
					break;
				}
				assignVariableValue(getVariable((String)command.getChild(0).data), getValueOfElementDouble(command.getChild(0)) + getValueOfElementDouble(command.getChild(3)));
			}
			break;
		case BREAK:
			loop_end_count++;
			break;
			
		case LET:
			double value;
			if(command.getChild(0).type == TreeElementType.L_BOOLEAN){
				if(((String)command.getChild(1).data).equals("=")){
					assignVariableValue(getVariable((String)command.getChild(0).data), (boolean)getValueOfElement(command.getChild(2), DataType.BOOLEAN));
					break;
				}
				if(((String)command.getChild(1).data).equals("=!")){
					assignVariableValue(getVariable((String)command.getChild(0).data), !(boolean)getValueOfElement(command.getChild(2), DataType.BOOLEAN));
					break;
				}
				else {
					assignVariableValue(getVariable((String)command.getChild(0).data), compareOperation(getValueOfElement(command.getChild(0), DataType.ANY),
							(String)command.getChild(1).data,getValueOfElement(command.getChild(2), DataType.ANY)));
					break;
				}
			}
			if(command.getChild(0).type == TreeElementType.L_STRING){
				if(((String)command.getChild(1).data).equals("=")){
					assignVariableValue(getVariable((String)command.getChild(0).data), (String)getValueOfElement(command.getChild(2), DataType.STRING));
				}
				else if(((String)command.getChild(1).data).equals("+=")){
					assignVariableValue(getVariable((String)command.getChild(0).data), (String)getValueOfElement(command.getChild(0), DataType.STRING) + (String)getValueOfElement(command.getChild(2), DataType.STRING));
				}
				break;
			}
			
			double value1 = getValueOfElementDouble(command.getChild(0));
			double value2 = (double) calcExpression(command.getChild(2).list);
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
				throw new ScriptRuntimeException("Invalid operator \"" + (String)command.getChild(1).data + "\"");
			}
			if(getVariable((String)command.getChild(0).data).type == DataType.INT){
				assignVariableValue(getVariable((String)command.getChild(0).data), (int)result);
			}
			else if(getVariable((String)command.getChild(0).data).type == DataType.DOUBLE){
				assignVariableValue(getVariable((String)command.getChild(0).data), result);
			}
			break;
			
		case CMP:
			assignVariableValue(getVariable((String)command.getChild(0).data), (boolean)compareOperation(getValueOfElement(command.getChild(1), DataType.ANY)
					, (String)getValueOfElement(command.getChild(2), DataType.STRING), getValueOfElement(command.getChild(3), DataType.ANY)));
			break;
		case SQRT:
			assignVariableValue(getVariable((String)command.getChild(0).data), Math.sqrt(getValueOfElementDouble(command.getChild(1))));
			break;
		case EXP:
			assignVariableValue(getVariable((String)command.getChild(0).data), Math.pow(getValueOfElementDouble(command.getChild(1)),
					(double)getValueOfElement(command.getChild(2), DataType.DOUBLE)));
			break;
		case TORECT:
			double radius = getValueOfElementDouble(command.getChild(2));
			double theta = getValueOfElementDouble(command.getChild(3));
			double pos_x = Deg.cos(theta) * radius;
			double pos_y = Deg.sin(theta) * radius;
			assignVariableValue(getVariable((String)command.getChild(0).data), pos_x);
			assignVariableValue(getVariable((String)command.getChild(1).data), pos_y);
			break;
		case TOPOLAR:
			pos_x = getValueOfElementDouble(command.getChild(2));
			pos_y = getValueOfElementDouble(command.getChild(3));
			radius = Math.sqrt(pos_x * pos_x + pos_y * pos_y);
			theta = Deg.atan2(pos_y, pos_x);
			assignVariableValue(getVariable((String)command.getChild(0).data), radius);
			assignVariableValue(getVariable((String)command.getChild(1).data), theta);
			break;
		case DISTANCE:
			pos_x = getValueOfElementDouble(command.getChild(1));
			pos_y = getValueOfElementDouble(command.getChild(2));
			assignVariableValue(getVariable((String)command.getChild(0).data), Math.sqrt(pos_x * pos_x + pos_y * pos_y));
			break;
		case SIN:
			assignVariableValue(getVariable((String)command.getChild(0).data), Deg.sin(getValueOfElementDouble(command.getChild(1))));
			break;
		case COS:
			assignVariableValue(getVariable((String)command.getChild(0).data), Deg.cos(getValueOfElementDouble(command.getChild(1))));
			break;
		case TAN:
			assignVariableValue(getVariable((String)command.getChild(0).data), Deg.tan(getValueOfElementDouble(command.getChild(1))));
			break;
		case ATAN:
			assignVariableValue(getVariable((String)command.getChild(0).data), Deg.atan(getValueOfElementDouble(command.getChild(1))));
			break;
		case ATAN2:
			assignVariableValue(getVariable((String)command.getChild(0).data), Deg.atan2(getValueOfElementDouble(command.getChild(1)),getValueOfElementDouble(command.getChild(1))));
			break;
		case LOG:
			value = Math.log(getValueOfElementDouble(command.getChild(2))) / Math.log(getValueOfElementDouble(command.getChild(1)));
			assignVariableValue(getVariable((String)command.getChild(0).data), value);
			break;
		case RANDOM:
			assignVariableValue(getVariable((String)command.getChild(0).data), Math.random());
			break;
		case RANDOM2:
			int a = (int) ((getValueOfElementDouble(command.getChild(2)) - getValueOfElementDouble(command.getChild(1))) + getValueOfElementDouble(command.getChild(2)));
			assignVariableValue(getVariable((String)command.getChild(0).data), Math.random() * 
					(getValueOfElementDouble(command.getChild(2)) - getValueOfElementDouble(command.getChild(1))) + getValueOfElementDouble(command.getChild(1)));
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
			map.getSprite(GameMain.frame.generator.getID(getValueOfElementInt(command.getChild(0)))).time = Integer.MAX_VALUE;
			break;
		case KIL:
			((Mob)map.getSprite(GameMain.frame.generator.getID(getValueOfElementInt(command.getChild(0))))).hp = 0;
			break;
		case HIDE:
			self.visiblity = false;
			break;
		case APPEAR:
			self.visiblity = true;
			break;
		case SKIP:
			//実際にフレームを飛ばす処理は未実装
			self.skip_frame = getValueOfElementInt(command.getChild(0));
			break;
		case EFFECT:
			//未実装
			map.effect(getValueOfElementInt(command.getChild(0)), getValueOfElementDouble(command.getChild(1)),
					getValueOfElementDouble(command.getChild(2)), getValueOfElementDouble(command.getChild(3)));
			break;
		case ACTIVATE:
			((Mob) map.getSprite(GameMain.frame.generator.getID(getValueOfElementInt(command.getChild(0))))).create(1);
			break;
		case EVENT:
		{
			int id = getValueOfElementInt(command.getChild(0));
			if(id == 10){
				map.startSpellCard((String) parameter_stack.pop());
			}
			if(id == 11){
				map.endSpellCard();
			}
		}
			break;
		case SOUND:
			SystemMain.soundManager.playClip((String)getValueOfElement(command.getChild(0), DataType.STRING), getValueOfElementInt(command.getChild(1)),getValueOfElementDouble(command.getChild(2)));
			break;
		case BGM:
			break;
		case COLLIDE:
			self.setCollisionFlag((boolean)getValueOfElement(command.getChild(0), DataType.BOOLEAN));
			break;
		case STGCLEAR:
			map.stageWin();
			break;
		case SHOOT:
			Shot shot;
			if((shot = map.getControlledShot(getValueOfElementInt(command.getChild(0)), getValueOfElementInt(command.getChild(1)),
					self.getX_double(), self.getY_double(), getValueOfElementDouble(command.getChild(4)), 1)) == null){
				shot = new Shot(getValueOfElementInt(command.getChild(0)), getValueOfElementInt(command.getChild(1)),
					self.getX_double(), self.getY_double(), getValueOfElementDouble(command.getChild(4)), 1);
			}
			shot.shoot_angle(getValueOfElementDouble(command.getChild(2)), getValueOfElementDouble(command.getChild(3)));
			last_shot_id = map.setSprite(shot);
			break;
		case SETSHOT:
			if((shot = map.getControlledShot(getValueOfElementInt(command.getChild(0)), getValueOfElementInt(command.getChild(1)),
					getValueOfElementDouble(command.getChild(2)), getValueOfElementDouble(command.getChild(3)),
					getValueOfElementDouble(command.getChild(6)), 1)) == null){
				shot = new Shot(getValueOfElementInt(command.getChild(0)), getValueOfElementInt(command.getChild(1)),
						getValueOfElementDouble(command.getChild(2)), getValueOfElementDouble(command.getChild(3)),
						getValueOfElementDouble(command.getChild(6)), 1);
			}
			shot.shoot_angle(getValueOfElementDouble(command.getChild(4)), getValueOfElementDouble(command.getChild(5)));
			last_shot_id = map.setSprite(shot);
			break;
		case GETID:
			assignVariableValue(getVariable((String)command.getChild(0).data), last_shot_id);
			break;
		case SSPEED:
			Sprite sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			sprite.x_speed = getValueOfElementDouble(command.getChild(1));
			sprite.y_speed = getValueOfElementDouble(command.getChild(2));
			break;
		case SSPEEDP:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			sprite.x_speed = Deg.cos(getValueOfElementDouble(command.getChild(2))) * getValueOfElementDouble(command.getChild(1));
			sprite.y_speed = Deg.sin(getValueOfElementDouble(command.getChild(2))) * getValueOfElementDouble(command.getChild(1));
			break;
		case SANGLE:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			radius = Math.sqrt(sprite.x_speed*sprite.x_speed + sprite.y_speed*sprite.y_speed);
			theta = getValueOfElementDouble(command.getChild(0));
			sprite.x_speed = Deg.cos(theta) * radius;
			sprite.y_speed = Deg.sin(theta) * radius;
			break;
		case SROTATE:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			sprite.rotation = getValueOfElementDouble(command.getChild(1));
			break;
		case VSPEED:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			assignVariableValue(getVariable((String)command.getChild(1).data), sprite.x_speed);
			assignVariableValue(getVariable((String)command.getChild(2).data), sprite.y_speed);
			break;
		case VSPEEDP:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			radius = Math.sqrt(sprite.x_speed*sprite.x_speed + sprite.y_speed*sprite.y_speed);
			theta = Deg.atan2(sprite.y_speed, sprite.x_speed);
			assignVariableValue(getVariable((String)command.getChild(1).data), radius);
			assignVariableValue(getVariable((String)command.getChild(2).data), theta);
			break;
		case VROTATE:
			sprite = map.getSprite(getValueOfElementInt(command.getChild(0)));
			assignVariableValue(getVariable((String)command.getChild(1).data), sprite.rotation);
			break;
		case CTRLM:
			shot = (Shot) map.getSprite(getValueOfElementInt(command.getChild(0)));
			shot.controlMovement(getValueOfElementInt(command.getChild(1)), getValueOfElementDouble(command.getChild(2)),
					getValueOfElementDouble(command.getChild(3)),getValueOfElementDouble(command.getChild(4)));
			break;
		case CTRLA:
			shot = (Shot) map.getSprite(getValueOfElementInt(command.getChild(0)));
			shot.controlAction(getValueOfElementInt(command.getChild(1)), getValueOfElementDouble(command.getChild(2)),
					getValueOfElementDouble(command.getChild(3)),getValueOfElementDouble(command.getChild(4)));
			break;
			
		case DLG:
			map.delegate(getValueOfElementInt(command.getChild(0)),self,map,getValueOfElementInt(command.getChild(1)));
			break;
		case RUN:
			//未実装
			externalCall((String)getValueOfElement(command.getChild(0), DataType.STRING));
			break;
		case PRM:
			parameter_stack.push(getValueOfElement(command.getChild(0), DataType.STRING));
			break;
			
		case PCOORD:
			assignVariableValue(getVariable((String)command.getChild(0).data), map.getPlayer().getX_double());
			assignVariableValue(getVariable((String)command.getChild(1).data), map.getPlayer().getY_double());
			break;
		case PSPEED:
			assignVariableValue(getVariable((String)command.getChild(0).data), map.getPlayer().x_speed);
			assignVariableValue(getVariable((String)command.getChild(1).data), map.getPlayer().y_speed);
			break;
		case PSPEEDP:
			pos_x = map.getPlayer().x_speed;
			pos_y = map.getPlayer().y_speed;
			radius = Math.sqrt(pos_x * pos_x + pos_y * pos_y);
			theta = Deg.atan2(pos_y, pos_x);
			break;
			
		case POS:
			assignVariableValue(ScriptSpecialVariableType.S_X, getValueOfElementDouble(command.getChild(0)));
			assignVariableValue(ScriptSpecialVariableType.S_Y, getValueOfElementDouble(command.getChild(1)));
			break;
		case SPEED:
			assignVariableValue(ScriptSpecialVariableType.S_XSPEED, getValueOfElementDouble(command.getChild(0)));
			assignVariableValue(ScriptSpecialVariableType.S_YSPEED, getValueOfElementDouble(command.getChild(1)));
			break;
		case SPEEDP:
			assignVariableValue(ScriptSpecialVariableType.S_SPEED, getValueOfElementDouble(command.getChild(0)));
			assignVariableValue(ScriptSpecialVariableType.S_ANGLE, getValueOfElementDouble(command.getChild(1)));
			break;
		case MOVETO:
		{
			double p1,p2,p3;
			p1 = getValueOfElementDouble(command.getChild(0));
			p2 = getValueOfElementDouble(command.getChild(1));
			p3 = getValueOfElementDouble(command.getChild(2));
			assignVariableValue(ScriptSpecialVariableType.S_XSPEED, (p1 - self.getX_double()) / p3);
			assignVariableValue(ScriptSpecialVariableType.S_YSPEED, (p2 - self.getY_double()) / p3);
			break;
		}
			
		default:
			throw new RuntimeException("Unexpected error");
		}
	}
	
	private ScriptVariable getVariable(String data) throws ScriptRuntimeException {
		ScriptVariable tmp = currentLocalVariables.get(data);
		if(tmp == null)
		{
			tmp = globalVariables.get(data);
			if(tmp == null) throw new ScriptRuntimeException("Undefined variable \""+data+"\"");
		}
	
		return tmp;
	}

	/**
	 * 式の値を求める
	 * @param expression 式(逆ポーランド順)
	 * @return 式の値
	 * @throws ScriptRuntimeException 
	 */
	private Object calcExpression(List<TreeElement> expression) throws ScriptRuntimeException {
		Stack<Double> operandStack = new Stack<>();
		for(int i = 0;i < expression.size();i++){
			TreeElement cur = expression.get(i);
			if(cur.type == TreeElementType.L_DOUBLE || cur.type == TreeElementType.L_INT ||
					cur.type == TreeElementType.VARIABLE){
				operandStack.push(getValueOfElementDouble(cur));
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
		return operandStack.pop();		//最後にスタックに残った値が結果
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

	private boolean compareOperation(Object value1, String operator,
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

	/**
	 * 要素の値を取得
	 * 変数ならその値を、リテラルならそのまま値を返す
	 * @param element 要素
	 * @return
	 * @throws ScriptRuntimeException
	 */
	private Object getValueOfElementNoCheck(TreeElement element) throws ScriptRuntimeException {
		Object ret;
		if(element.type == TreeElementType.VARIABLE){
			ret = getVariable((String)element.data).getValue();
			if(ret.getClass() == Integer.class) ret = (int)ret;
		}
		else if(element.type == TreeElementType.EXPRESSION){
			ret = calcExpression(element.list);
			if(ret.getClass() == Integer.class) ret = (int)ret;
		}
		else{
			ret = element.data;
			if(ret.getClass() == Integer.class) ret = (int)ret;
		}
		return ret;
	}
	
	/**
	 * 要素の値を取得
	 * 変数ならその値を、リテラルならそのまま値を返す
	 * @param element 要素
	 * @return
	 * @throws ScriptRuntimeException
	 */
	private Object getValueOfElement(TreeElement element, DataType type) throws ScriptRuntimeException {
		Object tmp = getValueOfElementNoCheck(element);
		if(!typeCheck(type, tmp)) throw new ScriptRuntimeException(String.format("Incompatible parameter: (%s) = (%s)", type.toString(), tmp.getClass().getName()));
		return tmp;
	}
	
	/**
	 * 要素の値を取得
	 * 変数ならその値を、リテラルならそのまま値を返す
	 * @param element 要素
	 * @return
	 * @throws ScriptRuntimeException
	 */
	private double getValueOfElementDouble(TreeElement element) throws ScriptRuntimeException {
		return ((Number)getValueOfElement(element, DataType.DOUBLE)).doubleValue();
	}
	
	/**
	 * 要素の値を取得
	 * 変数ならその値を、リテラルならそのまま値を返す
	 * @param element 要素
	 * @return
	 * @throws ScriptRuntimeException
	 */
	private int getValueOfElementInt(TreeElement element) throws ScriptRuntimeException {
		return ((Number)getValueOfElement(element, DataType.INT)).intValue();
	}
	
	private boolean typeCheck(DataType type, Object src) {
		switch(type) {
		case INT:
			if(src instanceof Integer || src instanceof Double) {		//double->intのキャストも自動で行う
				return true;
			}
			break;
			
		case DOUBLE:
			if(src instanceof Integer || src instanceof Double) {
				return true;
			}
			break;
			
		case STRING:
			if(src instanceof String) {
				return true;
			}
			break;
			
		case BOOLEAN:
			if(src instanceof Boolean) {
				return true;
			}
			break;
			
		case ARRAY_INT:
		case ARRAY_DOUBLE:
		case ARRAY_STRING:
		case ARRAY_BOOLEAN:
			if(src instanceof List<?>) {
				return true;
			}
			break;
			
		case ANY:
			return true;
		}
		return false;
	}

	/**
	 * 変数の値を取得
	 * @param var 変数
	 * @return
	 * @throws ScriptRuntimeException
	 */
	private Object getValueOfVariable(ScriptSpecialVariableType var) throws ScriptRuntimeException {
		switch(var){
		case S_TIME:
			return self.time;

		case S_ROTATION:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).rotation;
		case S_HP:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			return ((Enemy)self).hp;
		case S_XSPEED:
			return self.x_speed;
		case S_YSPEED:
			return self.y_speed;
		case M_PI:
			return Math.PI;
		case S_ANGLE:
			return Deg.atan2(self.y_speed, self.x_speed);
		case S_SPEED:
			return Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
		case S_X:
			return self.getX_double();
		case S_Y:
			return self.getY_double();
		case VAR_NULL:
			return null;
		}
		return null;
	}

	private void assignVariableElementValue(ScriptSpecialVariableType dest, TreeElement src) throws ScriptRuntimeException {
		assignVariableValue(dest, src.data);
	}
	
	private void assignVariableValue(ScriptVariable var, Object data) throws ScriptRuntimeException {
		Object src = data;
		if(data instanceof TreeElement) {
			src = ((TreeElement)data).data;
		}
		if(!typeCheck(var,src)) {
			throw new ScriptRuntimeException(String.format("Incompatible assignment: (%s) = (%s)", var.type.toString(), src.getClass().getName()));
		}
		switch(var.type) {
		case INT:
			var.setValue(((Number)src).intValue());
			break;
			
		case DOUBLE:
			var.setValue(((Number)src).doubleValue());
			break;
			
		case STRING:
		case BOOLEAN:
		case ARRAY_INT:
		case ARRAY_DOUBLE:
		case ARRAY_STRING:
		case ARRAY_BOOLEAN:
			var.setValue(src);
		}
	}

	private boolean typeCheck(ScriptVariable var, Object src) {
		return typeCheck(var.type, src);
	}

	private void assignVariableValue(ScriptSpecialVariableType dest, Object src) throws ScriptRuntimeException {
		if(src instanceof TreeElement) {
			assignVariableElementValue(dest, (TreeElement) src);
			return;
		}
		
		if(getVariableType(dest) == int.class && src.getClass() != Integer.class && src.getClass() != Double.class ||
			getVariableType(dest) == boolean.class && src.getClass() != Boolean.class ||
			getVariableType(dest) == double.class && src.getClass() != Double.class && src.getClass() != Integer.class ||
			getVariableType(dest) == String.class && src.getClass() != String.class) {
			throw new RuntimeException("Unexpected exception");
		}
		switch(dest){
		case S_TIME:
			self.time = ((Number)src).intValue();
			break;
			
		case S_ROTATION:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).rotation = ((Number)src).doubleValue();
			break;
		case S_HP:
			if(!(self instanceof Enemy)){
				throw new ScriptRuntimeException("Unsupported operation for this sprite");
			}
			((Enemy)self).hp = ((Number)src).doubleValue();
			break;
		case S_XSPEED:
			self.x_speed = ((Number)src).doubleValue();
			break;
		case S_YSPEED:
			self.y_speed = ((Number)src).doubleValue();
			break;
		case M_PI:
			throw new ScriptRuntimeException("Assignment to a constance");
		case S_ANGLE:
			double speed = Math.sqrt(self.x_speed * self.x_speed + self.y_speed * self.y_speed);
			self.x_speed = Deg.cos(((Number)src).doubleValue()) * speed;
			self.y_speed = Deg.sin(((Number)src).doubleValue()) * speed;
			break;
		case S_SPEED:
			double angle = Deg.atan2(self.y_speed, self.x_speed);
			self.x_speed = Deg.cos(angle) * ((Number)src).doubleValue();
			self.y_speed = Deg.sin(angle) * ((Number)src).doubleValue();
			break;
		case S_X:
			self.setX(((Number)src).doubleValue());
			break;
		case S_Y:
			self.setY(((Number)src).doubleValue());
			break;
			
		case VAR_NULL:
			//どこにも代入しない
			break;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private Class getVariableType(ScriptSpecialVariableType var) {
		switch(var){
		case S_TIME:
			return int.class;
			
		case S_ANGLE:
		case S_ROTATION:
		case S_HP:
		case S_XSPEED:
		case S_YSPEED:
		case M_PI:
		case S_SPEED:
		case S_X:
		case S_Y:
			return double.class;
			
		case VAR_NULL:
			return null;
		}
		return null;
	}
	
}
