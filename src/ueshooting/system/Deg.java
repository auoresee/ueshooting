package ueshooting.system;

/**
 * trigonometric functions for degree
 *
 */
public class Deg {
	public static double sin(double theta){
		return Math.sin(toRad(theta));
	}
	public static double cos(double theta){
		return Math.cos(toRad(theta));
	}
	public static double tan(double theta){
		return Math.tan(toRad(theta));
	}
	public static double atan(double value){
		return toDeg(Math.atan(value));
	}
	public static double atan2(double y, double x){
		return toDeg(Math.atan2(y,x));
	}
	
	
	public static double toRad(double value) {
		return value / 360 * Math.PI * 2;
	}
	public static double toDeg(double value) {
		return value * 360 / Math.PI / 2;
	}
}
