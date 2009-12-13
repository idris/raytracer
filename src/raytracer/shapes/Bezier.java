package raytracer.shapes;

import raytracer.*;

import java.util.List;

public class Bezier extends Shape {
	private List<Point> points;

	public Bezier(List<Point> points) {
		this.points = points;
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
