package raytracer.shapes;

import raytracer.*;

public class Triangle extends Shape {
	private final Point p1, p2, p3;

	public Triangle(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
