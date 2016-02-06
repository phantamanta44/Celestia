package io.github.phantamanta44.celestia.util;

public class MathUtils {

	public static int clamp(int n, int l, int u) {
		 return Math.max(Math.min(n, u), l);
	}
	
	public static float clamp(float n, float l, float u) {
		 return Math.max(Math.min(n, u), l);
	}
	
	public static double clamp(double n, double l, double u) {
		 return Math.max(Math.min(n, u), l);
	}
	
}