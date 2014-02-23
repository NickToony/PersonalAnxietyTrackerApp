package com.team5.graph;

import java.util.ArrayList;
import java.util.List;

public class Line {
	private List<Point> points = new ArrayList<Point>();
	private int colour;

	public Line(int colour) {
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}

	public void addPoint(float x, float y, String thought) {
		Point point = new Point(x, y, thought);
		points.add(point);
	}

	public float getX(int i) {
		return points.get(i).getX();
	}

	public float getY(int i) {
		return points.get(i).getY();
	}

	public int getTotal() {
		return points.size();
	}

	public String getThought(int i) {
		return points.get(i).getInformation();
	}
}