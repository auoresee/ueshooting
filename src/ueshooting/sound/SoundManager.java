package ueshooting.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager implements LineListener {
	//public SoundManager instance = new SoundManager();	in SystemMain
	public List<AudioInputStream> BGMList = new ArrayList<>();
	public HashMap<String,List<List<Clip>>> clipMap = new HashMap<>();

	public void registerClip(List<Clip> clips, String category) {
		List<List<Clip>> list = clipMap.get(category);
		if(list == null) {
			list = new ArrayList<List<Clip>>();
			clipMap.put(category, list);
		}
		list.add(clips);
	}
	
	//音量はパーセントで指定(100=最大)
	public boolean playClip(String category, int id, double volume) {
		int age = Integer.MIN_VALUE;
		int oldest = -1;
		if(clipMap.isEmpty()) return true;
		for(int i = 0;i < clipMap.get(category).get(id).size();i++){
			List<Clip> list = clipMap.get(category).get(id);
			if(list == null) {
				return false;
			}
			if(list.get(i).isRunning()){
				if(list.get(i).getFramePosition() > age){
					oldest = i;
					age = list.get(i).getFramePosition();
				}
				continue;
			}
			else {
				Clip clip = list.get(i);
				FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
				controlByLinearScalar(control, volume / 100);
				clip.start();
				return true;
			}
		}
		if(oldest != -1){
			Clip clip = clipMap.get(category).get(id).get(oldest);
			FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			controlByLinearScalar(control, volume / 100);
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
		return true;
	}
	
	private void controlByLinearScalar(FloatControl control, double linearScalar) {
		control.setValue((float)Math.log10(linearScalar) * 20);
	}
	
	public void registerBGM(AudioInputStream stream) {
		BGMList.add(stream);
	}
	
	public boolean playBGM(int id) {
		if(BGMList.isEmpty()) return false;
		AudioInputStream stream = BGMList.get(id);
		try {
			// オーディオ入力ストリームからデータを読む
			byte [] data = new byte [stream.available()];
			stream.read(data);
			stream.close();
			// ファイルのフォーマットを調べる
			AudioFormat af = stream.getFormat();
			// 再生する
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
			SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(af);
			setLineVolume(line, 50);
			line.start();
			line.write(data, 0, data.length);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//Lineの音量を変更(linearScalar: 0-100)
	public void setLineVolume(SourceDataLine line,double linearScalar){
    	try{
    		FloatControl control = 
    				(FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
    		control.setValue((float)Math.log10(linearScalar / 100) * 20);
    	}catch(IllegalArgumentException e){
    		e.printStackTrace();
    	}
	}

	@Override
	public void update(LineEvent event) {
		if(event.getType() == LineEvent.Type.STOP){
			if(event.getLine() instanceof Clip){
				//((Clip)event.getLine()).stop();
				((Clip)event.getLine()).setFramePosition(0);
			}
			else {
				event.getLine().close();
			}
		}
	}

	public void loadClip(String string, String category, int bufferCount) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		List<Clip> bufferList = new ArrayList<>();
		for(int i = 0;i < bufferCount;i++){
			// オーディオストリームを開く
			AudioInputStream stream = AudioSystem.getAudioInputStream(
					new File(string));

			// オーディオ形式を取得
			AudioFormat format = stream.getFormat();
			// ULAW/ALAW形式の場合はPCM形式に変更
			if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
				AudioFormat newFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(),
						format.getFrameSize() * 2, format.getFrameRate(), true);
				stream = AudioSystem.getAudioInputStream(newFormat, stream);
				format = newFormat;
			}

			// ライン情報を取得
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			// サポートされてる形式かチェック
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("エラー: " + string + "はサポートされていない形式です");
			}

			// 空のクリップを作成
			Clip clip = (Clip) AudioSystem.getLine(info);
			// クリップのイベントを監視
			clip.addLineListener(this);
			// オーディオストリームをクリップとして開く
			clip.open(stream);
			// クリップを登録
			bufferList.add(clip);
			// ストリームを閉じる
			stream.close();
		}
		registerClip(bufferList, category);
	}

}
