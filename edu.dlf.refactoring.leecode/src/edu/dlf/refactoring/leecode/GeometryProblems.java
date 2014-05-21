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
}
