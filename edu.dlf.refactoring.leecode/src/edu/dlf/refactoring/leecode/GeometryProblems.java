package edu.dlf.refactoring.leecode;

public class GeometryProblems {

	private static class Point {
		int x;
		int y;
	}
	
	private static boolean ifRectanglesOverlap(Point p1, Point p2, Point p3, 
		Point p4) {
		return !(p2.y < p3.y || p1.y > p4.y || p4.x < p1.x || p2.x < p3.x) ;
	}
	
	private static double calculatePie() {
		int width = 10000;
		double fanSpace = 0;
		for(int i = 1; i < width; i ++) {
			double s = width * width - i * i;
			fanSpace += Math.pow(s, 0.5);
		}
		return 4 * fanSpace / (width * width);
	}
	
	public static void main(String[] args) {
		System.out.println(calculatePie());
	}
}
