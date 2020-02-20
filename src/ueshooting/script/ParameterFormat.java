package ueshooting.script;

public class ParameterFormat {
	public ParameterFormatSpecifier[] format_specifiers;
	public ParameterFormat(){
		format_specifiers = new ParameterFormatSpecifier[0];
	}
	public ParameterFormat(ParameterFormatSpecifier... specifiers){
		format_specifiers = specifiers;
	}
	
		
	public static ParameterFormat getParameterFormat(ScriptCommand command) {
		switch(command) {
		case MOVE:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_ANY),
					new ParameterFormatSpecifier("="), new ParameterFormatSpecifier(ParameterType.ANY));
			
		case DIM:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_ANY), new ParameterFormatSpecifier(ParameterType.VAR_ANY));
			
		case PRINT:
		case POP:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_ANY));
			
		case PUSH:
		case PRM:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.ANY));
			
		case STYPE:
		case GETID:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_INT));
			
		case RANDOM:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT));
			
		case CALL:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.PROCEDURE));
			
		case JUMP:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.ANY),
					new ParameterFormatSpecifier("=="), new ParameterFormatSpecifier(ParameterType.ANY));
			
		case IF:
		case ELSEIF:
		case WHILE:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.ANY),
					new ParameterFormatSpecifier("==","!=",">","<",">=","<=","||","&&"), new ParameterFormatSpecifier(ParameterType.ANY));
			
		case BIF:
		case COLLIDE:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.BOOLEAN));
			
		case ELSE:
		case IFEND:
		case LEND:
		case BREAK:
		case CONTINUE:
		case DEBUG:
		case RETURN:
		case END:
		case DIE:
		case HIDE:
		case APPEAR:
		case STGCLEAR:
		case PCOORD:
		case PSPEED:
		case PSPEEDP:
			return new ParameterFormat();
			
		case FOR:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT), new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case EXP:
		case DISTANCE:
		case ATAN2:
		case LOG:
		case RANDOM2:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT), new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case LET:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT),
					new ParameterFormatSpecifier("="), new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier("+","-","*","/","%"), new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case LETV:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_INT_BOOLEAN),
					new ParameterFormatSpecifier("+=","-=","*=","/=","%=","=!"), new ParameterFormatSpecifier(ParameterType.DOUBLE_INT_BOOLEAN));
			
		case CMP:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.BOOLEAN),
					new ParameterFormatSpecifier(ParameterType.ANY),
					new ParameterFormatSpecifier("==","&&","||",">","<",">=","<="), new ParameterFormatSpecifier(ParameterType.ANY));
			
		case SQRT:
		case SIN:
		case COS:
		case TAN:
		case ATAN:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case TORECT:
		case TOPOLAR:
		case POS:
		case SPEED:
		case SPEEDP:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case SANGLE:
		case SROTATE:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case VROTATE:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT));
			
		case VSPEED:
		case VSPEEDP:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.VAR_DOUBLE_OR_INT));
			
		case SSPEED:
		case SSPEEDP:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case MOVETO:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
		
		case DEL:
		case KIL:
		case SKIP:
		case ACTIVATE:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.INT));
			
		case DLG:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.INT), new ParameterFormatSpecifier(ParameterType.INT));
			
		case EFFECT:
		case EVENT:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case SHOOT:
		case CTRLM:
		case CTRLA:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case SETSHOT:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case RUN:
		case BGM:
			return new ParameterFormat(new ParameterFormatSpecifier(ParameterType.STRING));
			
		case SOUND:
			return new ParameterFormat(
					new ParameterFormatSpecifier(ParameterType.STRING),
					new ParameterFormatSpecifier(ParameterType.INT),
					new ParameterFormatSpecifier(ParameterType.DOUBLE_OR_INT));
			
		case UNDEFINED:
			return null;
			
		default:
			throw new RuntimeException();
		}
	}
}
