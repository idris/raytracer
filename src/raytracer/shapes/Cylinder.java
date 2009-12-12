package raytracer.shapes;

import raytracer.Point;
import raytracer.Vector;

public class Cylinder extends Shape {
	private Point base;
	private Vector axis;
	private double radius;

	public Cylinder(Point base, Vector axis, double radius) {
		this.base = base;
		this.axis = axis;
		this.radius = radius;
	}
}
