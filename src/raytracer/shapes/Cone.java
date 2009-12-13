package raytracer.shapes;

import raytracer.*;

public class Cone extends Shape {
	private Point apex;
	private Vector axis;
	private double radius;

	public Cone(Point apex, Vector axis, double radius) {
		this.apex = apex;
		this.axis = axis;
		this.radius = radius;
	}

	public RayHit intersect(Ray ray) {
		return null;
	}
}
