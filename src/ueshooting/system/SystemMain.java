package ueshooting.system;

import java.nio.ByteBuffer;

import ueshooting.sound.SoundManager;

public class SystemMain {
	public static String version = "0.01b";
	public static SoundManager soundManager;
	public static int game_time = 0;
	public static final int tile_size = 32;
	public static final int window_clientarea_xsize = 640;
	public static final int window_clientarea_ysize = 480;
	public static final int main_screen_xsize = 384;
	public static final int main_screen_ysize = 448;
	public static final int main_screen_xoffset = 32;
	public static final int main_screen_yoffset = 16;
	public static final int infobar_xsize = 224;
	public static final int infobar_ysize = 480;
	public static final int screen_tile_xnum = 15;
	public static final int screen_tile_ynum = 15;
	public static final byte KEY_OFF = 0;
	public static final byte KEY_ON = 1;
	public static final byte KEY_PRESSED = 2;
	public static final int game_limit_time = 6000;
	public static final int game_clear_score = 100;
	public static final int screen_edge_size = 100;
	
	public static int get_tile_x(int x){
		return x / tile_size;
	}
	public static int get_tile_y(int y){
		return y / tile_size;
	}
	public static void printf(String format, Object... args) {
		System.out.printf(format,args);
	}
	public static double get_distance(double x1,double y1,double x2,double y2){
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	public static double toRad(double value) {
		return value / 360 * Math.PI * 2;
	}
	public static double toDeg(double value) {
		return value * 360 / Math.PI / 2;
	}
	public static byte[] intToBytes(int value){
		byte[] ret = new byte[4];
		ret[0] = (byte) (value & 0xff000000);
		ret[1] = (byte) (value & 0x00ff0000);
		ret[2] = (byte) (value & 0x0000ff00);
		ret[3] = (byte) (value & 0x000000ff);
		return ret;
	}
	public static int bytesToInt(byte[] value){
		int ret = 0;
		ret += value[0] << 24;
		ret += value[1] << 16;
		ret += value[2] << 8;
		ret += value[3];
		return ret;
	}
	public static int bytesToInt(byte[] value,int start_pos){
		byte[] data = new byte[]{value[start_pos + 0],value[start_pos + 1],
			value[start_pos + 2],value[start_pos + 3]};
		int ret = ByteBuffer.wrap(data).getInt();
		return ret;
	}
	public static int bytesToInt(byte value1,byte value2,byte value3,byte value4){
		int ret = 0;
		ret += value1 * 0x01000000;
		ret += value2 * 0x00010000;
		ret += value3 * 0x00000100;
		ret += value4 * 0x00000001;
		return ret;
	}
	public static void debugDialog(String string) {
		// TODO Auto-generated method stub
		
	}
	public static void abort() {
		System.exit(1);
	}
	public static void pause() {
		// TODO Auto-generated method stub
		
	}
}
