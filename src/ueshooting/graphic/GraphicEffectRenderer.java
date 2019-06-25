package ueshooting.graphic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import ueshooting.system.SystemMain;

public class GraphicEffectRenderer {
	List<GraphicEffect> effect_list;
	
	public void addEffect(GraphicEffect effect){
		effect_list.add(effect);
	}
	
	public void processCompoundEffect() {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			
			List<GraphicEffect> added_effects = effect.runCompoundEffect();
			effect_list.addAll(added_effects);
			i += added_effects.size();
		}
	}
	
	public void drawBackgroundEffect(Graphics2D g) {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			effect.runBackgroundEffect(g);
		}
	}

	public void drawForegroundEffect(Graphics2D g) {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			effect.runForegroundEffect(g);
		}
	}
	
	public void drawSurfaceEffect(Graphics2D g) {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			effect.runSurfaceEffect(g);
		}
	}
	
	//�F���ω��ȂǁA���C����ʑS�̂Ɍ��ʂ��y�Ԃ���
	public void drawScreenEffect(Graphics2D g) {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			effect.runScreenEffect(g);
		}
	}
	
	public void drawUIEffect(Graphics2D g) {
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			effect.runUIEffect(g);
		}
	}
	
	//�t���[���̏I���ɕK���Ă�
	public void endFrame(){
		for(int i = 0;i < effect_list.size();i++){
			GraphicEffect effect = effect_list.get(i);
			
			if(effect.isDone()){
				//�I�������G�t�F�N�g�͍폜����
				effect_list.remove(i);
				i--;
			}
			
			effect.time++;
		}
	}
	
	public void setEffectList(List<GraphicEffect> e){
		effect_list = e;
	}

}
