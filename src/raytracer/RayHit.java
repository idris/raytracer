package raytracer;

import raytracer.shapes.Shape;

public class RayHit {
	public final Ray ray;
	public final Shape shape;
	public final double t;
	public final Vector normal;
	public final Point point;
	private final boolean incoming;

	public RayHit(Ray ray, Shape shape, Vector normal, double t, boolean entering) {
		this.ray = ray;
		this.shape = shape;
		this.t = t;
		this.normal = normal.normalize();
		this.point = ray.getEnd(t);
		this.incoming = entering;
	}

	public RayHit(Ray ray, Shape shape, Vector normal, Point intersection, boolean entering) {
		this.ray = ray;
		this.shape = shape;
		this.t = new Vector(ray.origin, intersection).magnitude();
		this.normal = normal.normalize();
		this.point = intersection;
		this.incoming = entering;
	}

	public Ray getReflectionRay() {
		return new Ray(point, ray.direction.minus(normal.times(2.0*ray.direction.dot(normal))));
	}

	public Ray getTransmissionRay() {
		Vector v = ray.direction.negate();
		Vector n = normal;
		double cosi = v.dot(n);
		double nint;
		if(incoming) nint = 1.0 / shape.finish.ior;
		else nint = shape.finish.ior;
		double cost = Math.sqrt(1.0 - nint*nint * (1 - cosi*cosi));

		return new Ray(point, n.times(nint * cosi - cost).minus(v.times(nint)));
	}
}
