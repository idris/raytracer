package raytracer.shapes;

import raytracer.Point;

public class Sphere extends Shape {
	Point center;
	double radius;

	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
}
