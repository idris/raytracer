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
		Vector v = ray.direction.negate();
		Vector n = normal;
		double cosi = v.dot(n);
		double nint;
		if(shape.contains(point.plus(v.times(0.001)))) nint = shape.finish.ior;
		else nint = 1.0 / shape.finish.ior;
		double cost = Math.sqrt(1.0 - nint*nint * (1 - cosi*cosi));

		return new Ray(point, n.times(nint * cosi - cost).minus(v.times(nint)));
	}
}
