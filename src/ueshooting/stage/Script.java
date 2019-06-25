package ueshooting.stage;

import ueshooting.script.TreeElement;
import ueshooting.script.TreeElementType;

public class Script extends TreeElement {
	public Script(TreeElementType type) {
		super(type);
	}
	public TreeElement getTimeAction(int time){
		for(int i = 0;i < list.size();i++){
			TreeElement e = list.get(i);
			if(e.type != TreeElementType.ACTION) continue;
			if((int)e.data == time) return e;
		}
		return null;
	}
	public TreeElement getNameProcedure(String name){
		for(int i = 0;i < list.size();i++){
			TreeElement e = list.get(i);
			if(e.type != TreeElementType.PROCEDURE) continue;
			if(((String)e.data).equals(name)) return e;
		}
		return null;
	}
}
