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

	public Ray getReflectionRay() {
		return null;
	}

	public Ray getTransmissionRay() {
		return null;
	}
}
