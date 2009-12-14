package raytracer.shapes;

import raytracer.*;

public class Parallelogram extends Shape {
	private final Point p1, p2, p3, p4;

	public Parallelogram(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p1.plus(new Vector(p1, p2).plus(new Vector(p1, p3)));

		Log.warn("Parallelogram shape is not supported. This shape will be ignored.");
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
