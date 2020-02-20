package ueshooting.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ueshooting.script.ScriptLoader;
import ueshooting.script.ScriptSyntaxException;
import ueshooting.stage.EnemyData;
import ueshooting.stage.Script;
import ueshooting.stage.ShotData;
import ueshooting.stage.Stage;
import ueshooting.system.SystemMain;

public class StageLoader {
	private static final byte[] stagefileSignature = {'S','T',(byte) 0x80,(byte) 0x9a};
	private static final int stagefileMinimalHeaderSize = 12;
	@SuppressWarnings("unused")
	private static final int stagefileMinimalStageSectorSize = 0;
	private static final int SPAWN_MANUAL = 999999;
	int pos;
	int stagefile_stagesector_length;
	int stage_time_length;
	String stage_name;
	String stage_bgimage_path;
	int stage_first_bgm;
	List<String> stage_bgm_path_list = new ArrayList<>();
	List<EnemyData> stage_enemy_data = new ArrayList<>();
	List<ShotData> stage_shot_data = new ArrayList<>();
	Script global;
	@SuppressWarnings("unused")
	private int stagefile_header_length;
	private int starting_frame;
	private byte debug_flags;
	
	public List<String> extractStageNames(int length, List<String> string) throws IOException{
		List<String> ret = new ArrayList<>();
		int stage_num = length;
		if(string.size() < stage_num - 1)stage_num = string.size();
		for(int i = 0;i < stage_num;i++){
			ret.add(string.get(i));
		}
		return ret;
	}
	
	public Stage loadStage(byte[] buf) throws UnsupportedStageFileException{
		pos = 0;
		try {
			loadHeader(buf);
			loadStageSector(buf);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ScriptSyntaxException e) {
			System.out.printf(String.valueOf(e.error_position));
			e.printStackTrace();
			return null;
		}
		return getStage();
	}

	private void loadHeader(byte[] buf) throws UnsupportedStageFileException, UnsupportedEncodingException {
		int len;
		byte[] bytes = new byte[4];
		if(buf.length < stagefileMinimalHeaderSize)throw new UnsupportedStageFileException();
		System.arraycopy(buf, pos, bytes, 0, 4);
		pos += 4;
		if(!Arrays.equals(bytes,stagefileSignature))throw new UnsupportedStageFileException();
		len = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		bytes = new byte[len];
		System.arraycopy(buf, pos, bytes, 0, len);
		pos += len;
		stage_name = new String(bytes,"SJIS");
	}
	
	private void loadStageSector(byte[] buf) throws UnsupportedEncodingException, UnsupportedStageFileException, ScriptSyntaxException {
		int len;
		byte[] temp;
		stage_time_length = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		System.out.printf("TimeLength: %d\n",stage_time_length);
		len = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		temp = new byte[len];
		System.arraycopy(buf, pos, temp, 0, len);
		pos += len;
		stage_bgimage_path = new String(temp,"SJIS");
		//len = SystemMain.bytesToInt(buf,pos);
		//pos += 4;
		//temp = new byte[len];
		//System.arraycopy(buf, pos, temp, 0, len);
		//pos += len;
		stage_first_bgm = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		int bgm_num = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		String path;
		for(int i = 0;i < bgm_num;i++){
			len = SystemMain.bytesToInt(buf,pos);
			pos += 4;
			temp = new byte[len];
			System.arraycopy(buf, pos, temp, 0, len);
			pos += len;
			path = new String(temp,"SJIS");
			stage_bgm_path_list.add(path);
		}
		starting_frame = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		debug_flags = buf[pos];
		pos += 1;
		//デバッグ用予約領域(7byte)
		pos += 7;
		int enemy_num = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		for(int i = 0;i < enemy_num;i++){
			stage_enemy_data.add(loadEnemyData(buf));
		}
		int shot_num = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		for(int i = 0;i < shot_num;i++){
			stage_shot_data.add(loadShotData(buf));
		}
		global = new ScriptLoader().generateTree(loadScript(buf));
	}
	
	private EnemyData loadEnemyData(byte[] buf) throws UnsupportedEncodingException, ScriptSyntaxException, UnsupportedStageFileException {
		//String name;
		String image_path;
		int spawn_x,spawn_y;
		int spawn_time;
		byte category;
		int type;
		int hp;
		Script ai;
		byte[] temp;
		int len;
		/*len = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		temp = new byte[len];
		System.arraycopy(buf, pos, temp, 0, len);
		pos += len;
		name = new String(temp,"SJIS");*/
		len = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		temp = new byte[len];
		System.arraycopy(buf, pos, temp, 0, len);
		pos += len;
		image_path = new String(temp,"SJIS");
		spawn_x = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		spawn_y = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		spawn_time = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		if(spawn_time == SPAWN_MANUAL) {
			spawn_time = -1;
		}
		category = buf[pos];
		pos += 1;
		type = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		hp = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		ai = new ScriptLoader().generateTree(loadScript(buf));
		return new EnemyData(image_path,spawn_x,spawn_y,spawn_time,category,type,hp,ai);
	}
	
	private ShotData loadShotData(byte[] buf) throws ScriptSyntaxException, UnsupportedEncodingException, UnsupportedStageFileException {
		int subType;
		Script script;
		subType = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		script = new ScriptLoader().generateTree(loadScript(buf));
		return new ShotData(subType, script);
	}
	
	private String loadScript(byte[] buf) throws UnsupportedStageFileException, UnsupportedEncodingException {
		int script_length = SystemMain.bytesToInt(buf, pos);
		pos += 4;
		//System.out.println((char)buf[pos]);
		if(pos + script_length > buf.length) throw new UnsupportedStageFileException();
		String ret = new String(buf, "SJIS").substring(pos, pos + script_length);
		pos += script_length;
		return ret;
	}

	/*private EnemyAI loadEnemyAI(byte[] buf) {
		EnemyAI ai = new EnemyAI();
		int action_num;
		int loop_length;
		action_num = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		for(int i = 0;i < action_num;i++){
			ai.addAction(loadEnemyAction(buf));
		}
		loop_length = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		ai.setLoopLen(loop_length);
		return ai;
	}

	private EnemyAction loadEnemyAction(byte[] buf) {
		EnemyAction action;
		int type;
		int subtype;
		int prm1;
		int prm2_int;
		//String prm2_str;
		int time;
		type = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		subtype = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		prm1 = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		prm2_int = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		time = SystemMain.bytesToInt(buf,pos);
		pos += 4;
		action = new EnemyAction(type,subtype,prm1,prm2_int,time);
		//action = new EnemyAction(type,subtype,prm1,prm2_str,time);
		return action;
	}*/

	private Stage getStage() {
		Stage stage = new Stage(stage_bgimage_path,stage_first_bgm,stage_bgm_path_list,stage_time_length,starting_frame,debug_flags,stage_enemy_data,stage_shot_data,global);
		return stage;
	}
}
