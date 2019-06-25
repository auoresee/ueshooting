package ueshooting.sprite;

import ueshooting.map.Map;
import ueshooting.system.Deg;
import ueshooting.system.SystemMain;

//スペルカード1枚目の画面端の魔法陣(0=左上(1回目),1=右下(1回目),2=左上→左下(2回目),3=右下→右上(2回目))
//0,1と2,3のループの終わりが一致しないのは仕様
//0,1 -> 反時計回りに1周する
public class DelegatedAI1_2 extends DelegatedAI {
	private int type;
	public DelegatedAI1_2(Sprite sprite, Map p_map, int prm) {
		super(sprite, p_map, prm);
		this.type = prm;
	}
	
	@Override
	public void timeAction(){
		switch(type){
		case 0: 
			switch(iTime){
			case 30:
				moveToIn(0, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 41:
				shootTo(9,0,2.0,192,224);
				break;
			case 52:
				shootTo(9,0,2.0,192,224);
				break;
			case 63:
				shootTo(9,0,2.0,192,224);
				break;
			case 74:
				shootTo(9,0,2.0,192,224);
				break;
			case 85:
				shootTo(9,0,2.0,192,224);
				break;
			case 96:
				shootTo(9,0,2.0,192,224);
				break;
			case 107:
				shootTo(9,0,2.0,192,224);
				break;
			case 118:
				shootTo(9,0,2.0,192,224);
				break;
			case 129:
				shootTo(9,0,2.0,192,224);
				break;
			case 140:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
			
			case 210:
				moveToIn(383, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 221:
				shootTo(9,0,2.0,192,224);
				break;
			case 232:
				shootTo(9,0,2.0,192,224);
				break;
			case 243:
				shootTo(9,0,2.0,192,224);
				break;
			case 254:
				shootTo(9,0,2.0,192,224);
				break;
			case 265:
				shootTo(9,0,2.0,192,224);
				break;
			case 276:
				shootTo(9,0,2.0,192,224);
				break;
			case 287:
				shootTo(9,0,2.0,192,224);
				break;
			case 298:
				shootTo(9,0,2.0,192,224);
				break;
			case 309:
				shootTo(9,0,2.0,192,224);
				break;
			case 320:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 460:
				moveToIn(383, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 471:
				shootTo(9,0,2.0,192,224);
				break;
			case 482:
				shootTo(9,0,2.0,192,224);
				break;
			case 493:
				shootTo(9,0,2.0,192,224);
				break;
			case 504:
				shootTo(9,0,2.0,192,224);
				break;
			case 515:
				shootTo(9,0,2.0,192,224);
				break;
			case 526:
				shootTo(9,0,2.0,192,224);
				break;
			case 537:
				shootTo(9,0,2.0,192,224);
				break;
			case 548:
				shootTo(9,0,2.0,192,224);
				break;
			case 559:
				shootTo(9,0,2.0,192,224);
				break;
			case 570:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 640:
				moveToIn(0, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 651:
				shootTo(9,0,2.0,192,224);
				break;
			case 662:
				shootTo(9,0,2.0,192,224);
				break;
			case 673:
				shootTo(9,0,2.0,192,224);
				break;
			case 684:
				shootTo(9,0,2.0,192,224);
				break;
			case 695:
				shootTo(9,0,2.0,192,224);
				break;
			case 706:
				shootTo(9,0,2.0,192,224);
				break;
			case 717:
				shootTo(9,0,2.0,192,224);
				break;
			case 728:
				shootTo(9,0,2.0,192,224);
				break;
			case 739:
				shootTo(9,0,2.0,192,224);
				break;
			case 750:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 890:
				moveToIn(0, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 901:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 912:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 923:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 934:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 945:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 956:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 967:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 978:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 989:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1000:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1140:
				moveToIn(0, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 1151:
				shootTo(9,0,2.0,192,224);
				break;
			case 1162:
				shootTo(9,0,2.0,192,224);
				break;
			case 1173:
				shootTo(9,0,2.0,192,224);
				break;
			case 1184:
				shootTo(9,0,2.0,192,224);
				break;
			case 1195:
				shootTo(9,0,2.0,192,224);
				break;
			case 1206:
				shootTo(9,0,2.0,192,224);
				break;
			case 1217:
				shootTo(9,0,2.0,192,224);
				break;
			case 1228:
				shootTo(9,0,2.0,192,224);
				break;
			case 1239:
				shootTo(9,0,2.0,192,224);
				break;
			case 1250:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 1360:
				iTime = 0;
				break;
			}
			
			
			break;
			
		case 1: 
			switch(iTime){
			case 30:
				moveToIn(383, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 41:
				shootTo(9,0,2.0,192,224);
				break;
			case 52:
				shootTo(9,0,2.0,192,224);
				break;
			case 63:
				shootTo(9,0,2.0,192,224);
				break;
			case 74:
				shootTo(9,0,2.0,192,224);
				break;
			case 85:
				shootTo(9,0,2.0,192,224);
				break;
			case 96:
				shootTo(9,0,2.0,192,224);
				break;
			case 107:
				shootTo(9,0,2.0,192,224);
				break;
			case 118:
				shootTo(9,0,2.0,192,224);
				break;
			case 129:
				shootTo(9,0,2.0,192,224);
				break;
			case 140:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
			
			case 210:
				moveToIn(0, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 221:
				shootTo(9,0,2.0,192,224);
				break;
			case 232:
				shootTo(9,0,2.0,192,224);
				break;
			case 243:
				shootTo(9,0,2.0,192,224);
				break;
			case 254:
				shootTo(9,0,2.0,192,224);
				break;
			case 265:
				shootTo(9,0,2.0,192,224);
				break;
			case 276:
				shootTo(9,0,2.0,192,224);
				break;
			case 287:
				shootTo(9,0,2.0,192,224);
				break;
			case 298:
				shootTo(9,0,2.0,192,224);
				break;
			case 309:
				shootTo(9,0,2.0,192,224);
				break;
			case 320:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 460:
				moveToIn(0, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 471:
				shootTo(9,0,2.0,192,224);
				break;
			case 482:
				shootTo(9,0,2.0,192,224);
				break;
			case 493:
				shootTo(9,0,2.0,192,224);
				break;
			case 504:
				shootTo(9,0,2.0,192,224);
				break;
			case 515:
				shootTo(9,0,2.0,192,224);
				break;
			case 526:
				shootTo(9,0,2.0,192,224);
				break;
			case 537:
				shootTo(9,0,2.0,192,224);
				break;
			case 548:
				shootTo(9,0,2.0,192,224);
				break;
			case 559:
				shootTo(9,0,2.0,192,224);
				break;
			case 570:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 640:
				moveToIn(383, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 651:
				shootTo(9,0,2.0,192,224);
				break;
			case 662:
				shootTo(9,0,2.0,192,224);
				break;
			case 673:
				shootTo(9,0,2.0,192,224);
				break;
			case 684:
				shootTo(9,0,2.0,192,224);
				break;
			case 695:
				shootTo(9,0,2.0,192,224);
				break;
			case 706:
				shootTo(9,0,2.0,192,224);
				break;
			case 717:
				shootTo(9,0,2.0,192,224);
				break;
			case 728:
				shootTo(9,0,2.0,192,224);
				break;
			case 739:
				shootTo(9,0,2.0,192,224);
				break;
			case 750:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 890:
				moveToIn(383, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 901:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 912:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 923:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 934:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 945:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 956:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 967:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 978:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 989:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1000:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1140:
				moveToIn(383, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 1151:
				shootTo(9,0,2.0,192,224);
				break;
			case 1162:
				shootTo(9,0,2.0,192,224);
				break;
			case 1173:
				shootTo(9,0,2.0,192,224);
				break;
			case 1184:
				shootTo(9,0,2.0,192,224);
				break;
			case 1195:
				shootTo(9,0,2.0,192,224);
				break;
			case 1206:
				shootTo(9,0,2.0,192,224);
				break;
			case 1217:
				shootTo(9,0,2.0,192,224);
				break;
			case 1228:
				shootTo(9,0,2.0,192,224);
				break;
			case 1239:
				shootTo(9,0,2.0,192,224);
				break;
			case 1250:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 1360:
				iTime = 0;
				break;
			}
			break;
			
		case 2: 
			//2,3のiTime = 0,1のiTime + 140
			switch(iTime){
			case 110:		//250
				self.y = 447;
				moveToIn(0, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 121:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 132:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 143:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 154:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 165:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 176:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 187:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 198:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 209:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 220:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 280:		//420
				moveToIn(383, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 291:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 302:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 313:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 324:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 335:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 346:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 357:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 368:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 379:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 390:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 750:		//890
				moveToIn(383, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 761:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 772:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 783:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 794:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 805:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 816:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 827:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 838:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 849:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 860:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1000:		//1140
				moveToIn(383, 0, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 1011:
				shootTo(9,0,2.0,192,224);
				break;
			case 1022:
				shootTo(9,0,2.0,192,224);
				break;
			case 1033:
				shootTo(9,0,2.0,192,224);
				break;
			case 1044:
				shootTo(9,0,2.0,192,224);
				break;
			case 1055:
				shootTo(9,0,2.0,192,224);
				break;
			case 1066:
				shootTo(9,0,2.0,192,224);
				break;
			case 1077:
				shootTo(9,0,2.0,192,224);
				break;
			case 1088:
				shootTo(9,0,2.0,192,224);
				break;
			case 1099:
				shootTo(9,0,2.0,192,224);
				break;
			case 1110:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 1180:		//1320
				moveToIn(0, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 1191:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1202:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1213:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1224:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1235:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1246:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1257:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1268:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1279:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1290:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1320:
				moveToIn(0, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 1331:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1342:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1353:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1364:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1375:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1386:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1397:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1408:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1419:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1430:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1460:
				iTime = 100;
			}
			break;

		case 3: 
			//2,3のiTime = 0,1のiTime + 140
			switch(iTime){
			case 110:		//250
				self.y = 0;
				moveToIn(383, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 121:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 132:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 143:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 154:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 165:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 176:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 187:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 198:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 209:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 220:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 280:		//420
				moveToIn(0, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 291:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 302:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 313:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 324:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 335:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 346:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 357:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 368:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 379:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 390:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 750:		//890
				moveToIn(0, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 761:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 772:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 783:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 794:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 805:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 816:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 827:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 838:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 849:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 860:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1000:		//1140
				moveToIn(0, 447, 110);
				shootTo(9,0,2.0,192,224);		//11回発射する
				break;
			case 1011:
				shootTo(9,0,2.0,192,224);
				break;
			case 1022:
				shootTo(9,0,2.0,192,224);
				break;
			case 1033:
				shootTo(9,0,2.0,192,224);
				break;
			case 1044:
				shootTo(9,0,2.0,192,224);
				break;
			case 1055:
				shootTo(9,0,2.0,192,224);
				break;
			case 1066:
				shootTo(9,0,2.0,192,224);
				break;
			case 1077:
				shootTo(9,0,2.0,192,224);
				break;
			case 1088:
				shootTo(9,0,2.0,192,224);
				break;
			case 1099:
				shootTo(9,0,2.0,192,224);
				break;
			case 1110:
				stopMoving();
				shootTo(9,0,2.0,192,224);
				break;
				
			case 1180:		//1320
				moveToIn(383, 447, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 1191:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1202:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1213:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1224:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1235:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1246:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1257:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1268:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1279:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1290:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1320:		//100
				moveToIn(383, 0, 110);
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());		//11回発射する
				break;
			case 1331:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1342:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1353:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1364:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1375:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1386:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1397:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1408:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1419:
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
			case 1430:
				stopMoving();
				shootTo(19,0,2.0,map.getPlayer().getX_double(),map.getPlayer().getY_double());
				break;
				
			case 1460:
				iTime = 100;
			}
			break;
		}
	}
	
	@Override
	public void update(){
		if(map.getStageState() > 2) self.exist = false;
		super.update();
	}
	
	private Shot shootTo(int p_type, int p_subType, double speed, double dest_x, double dest_y){
		Shot shot = new Shot(p_type, p_subType, self.x, self.y, 1);
		double angle = Deg.atan2(dest_y - self.y, dest_x - self.x);
		double x_speed = Deg.cos(angle) * speed;
		double y_speed = Deg.sin(angle) * speed;
		shot.shoot_xy(x_speed, y_speed);
		map.setSprite(shot);
		SystemMain.soundManager.playClip("shot", 4, 10);
		return shot;
	}
}