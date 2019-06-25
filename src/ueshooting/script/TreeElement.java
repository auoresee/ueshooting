package ueshooting.script;

import java.util.ArrayList;
import java.util.List;

public class TreeElement {
	public TreeElementType type;
	public Object data = null;
	public TreeElement parent = null;	//ƒ‹[ƒg‚Ìê‡‚Ínull
	protected List<TreeElement> list = new ArrayList<>();
	
	public TreeElement(TreeElementType type) {
		this.type = type;
	}
	
	public void setParent(TreeElement parent){
		this.parent = parent;
	}
	
	public TreeElement(TreeElementType type, TreeElement parent) {
		this.type = type;
		this.parent = parent;
	}
	
	public void addChild(TreeElement child){
		child.setParent(this);
		list.add(child);
	}
	
	public TreeElement getChild(int index){
		return list.get(index);
	}

	public int getChildNum() {
		return list.size();
	}
}
