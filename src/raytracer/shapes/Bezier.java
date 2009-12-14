package raytracer.shapes;

import raytracer.*;

import java.util.List;

public class Bezier extends Shape {
	private List<Point> points;

	public Bezier(List<Point> points) {
		this.points = points;

		Log.warn("Bezier shape is not supported. This shape will be ignored.");
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
