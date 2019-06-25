package ueshooting.stage;

public class ShotData {
	public ShotData(int p_subType, Script p_ai) {
		subType = p_subType;
		script = p_ai;
	}

	int subType;
	Script script;
	public int getSubType() {
		return subType;
	}
	public Script getScript() {
		return script;
	}
}