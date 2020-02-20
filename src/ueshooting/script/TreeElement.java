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
	
	//e‚Ì‘¤‚É‚Í’Ç‰Á‚³‚ê‚È‚¢
	void setParent(TreeElement parent){
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
	
	public void addChilds(List<TreeElement> childs){
		for(int i = 0; i < childs.size(); i++) {
			childs.get(i).setParent(this);
		}
		list.addAll(childs);
	}
	
	public TreeElement getChild(int index){
		return list.get(index);
	}

	public int getChildNum() {
		return list.size();
	}
}
