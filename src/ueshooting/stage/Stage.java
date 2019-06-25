package ueshooting.stage;

import java.util.ArrayList;
import java.util.List;

public class Stage {
	//int stage_index;	//面番号
	//String stage_name;
	String bg_path;
	int first_bgm;
	List<String> bgm_path_list;	//List<String>だとエラーになる
	int time_length;	//面の時間の長さ(ボス除く)
	int starting_frame;			//for debug
	boolean debugInvincible = false;	//for debug
	protected List<EnemyData> enemydata = new ArrayList<>();
	protected List<ShotData> shotdata = new ArrayList<>();
	public Script global;
	protected int[] shotSubTypeList;
	public Stage(String p_bg_path, int p_first_bgm, List<String> stage_bgm_path_list, int p_time_length,
			int p_starting_frame, byte debug_flags, List<EnemyData> p_enemydata, List<ShotData> p_shotdata, Script global) {
		bg_path = p_bg_path;
		first_bgm = p_first_bgm;
		bgm_path_list = stage_bgm_path_list;
		time_length = p_time_length;
		starting_frame = p_starting_frame;
		setDebugFlags(debug_flags);
		enemydata = p_enemydata;
		shotdata = p_shotdata;
		this.global = global;
		
		shotSubTypeList = new int[p_shotdata.size()];
		for(int i = 0;i < shotdata.size();i++){
			shotSubTypeList[i] = shotdata.get(i).subType;
		}
	}

	private void setDebugFlags(byte debug_flags) {
		if((debug_flags & 0x00000001) == 1) debugInvincible = true;
	}
	
	public String get_bg_path() {
		return bg_path;
	}
	
	public int get_first_bgm() {
		return first_bgm;
	}
	
	public List<String> get_bgm_path_list() {
		return bgm_path_list;
	}
	
	public EnemyData getEnemyData(int cur_enemy_index) {
		return enemydata.get(cur_enemy_index);
	}
	public int getEnemyNum() {
		return enemydata.size();
	}
	public Script getShotScript(int subType) {
		for(int i = 0;i < shotdata.size();i++){
			if(shotSubTypeList[i] == subType)return shotdata.get(i).getScript();
		}
		return null;
	}
	
	public int getStartingFrame(){
		return starting_frame;
	}
	public boolean getDebugInvincible() {
		return debugInvincible;
	}
}