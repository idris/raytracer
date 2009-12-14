package raytracer.shapes;

import raytracer.*;

public class Plane extends Shape {
	private final double a, b, c, d;
	private final Vector normal;

	public Plane(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.normal = new Vector(a, b, c).normalize();
	}

	public RayHit intersect(Ray ray) {
		// from http://www.tar.hu/gamealgorithms/ch22lev1sec2.html
		double denominator = (a * ray.direction.x + b * ray.direction.y + c * ray.direction.z);
		if(denominator == 0.0) return null;

		double t = - (a * ray.origin.x + b * ray.origin.y + c * ray.origin.z + d) / denominator;

		if(t < 0) return null;

		return new RayHit(ray, this, normal, t, true);
	}
}
