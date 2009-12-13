package raytracer;

import raytracer.shapes.Shape;

public class RayHit {
	public final Ray ray;
	public final Shape shape;
	public final double t;
	public final Vector normal;
	public final Point point;

	public RayHit(Ray ray, Shape shape, Vector normal, double t) {
		this.ray = ray;
		this.shape = shape;
		this.t = t;
		this.normal = normal.normalize();
		this.point = ray.getEnd(t);
	}

	public RayHit(Ray ray, Shape shape, Vector normal, Point intersection) {
		this.ray = ray;
		this.shape = shape;
		this.t = new Vector(ray.origin, intersection).getMagnitude();
		this.normal = normal.normalize();
		this.point = intersection;
	}

	public Ray getReflectionRay() {
		return new Ray(point, ray.direction.minus(normal.times(2.0*ray.direction.dot(normal))));
	}

	public Ray getTransmissionRay() {
		return null;
	}
}
