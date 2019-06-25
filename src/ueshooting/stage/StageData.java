package ueshooting.stage;

public class StageData {
	protected int stage_num;
	protected Stage stages[];
	public StageData(int p1){
		stage_num = p1;
		stages = new Stage[stage_num];
	}
	
	public void setStage(Stage stage,int index){
		stages[index] = stage;
	}
	public int getStageNum(){
		return stage_num;
	}

	public Stage getStage(int i) {
		return stages[i];
	}
}
