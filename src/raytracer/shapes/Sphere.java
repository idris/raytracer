package raytracer.shapes;

import raytracer.*;

public class Sphere extends Shape {
	Point center;
	double radius;

	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	public RayHit intersect(Ray ray) {
/*
		Vector eo = new Vector(ray.origin, center);
		double v = eo.dot(ray.direction);
		double disc = radius*radius - (eo.dot(eo) - v*v);
		if(disc < 0) {
			return null;
		} else {
			double d = Math.sqrt(disc);
			Point p = ray.origin.plus(ray.direction.times(v - d));
			Vector normal;
			if(eo.magnitude() > radius) {
				// outside
				normal = new Vector(center, p);
			} else {
				// inside
				normal = new Vector(p, center);
			}

			return new RayHit(ray, this, normal, p);
		}
*/
// /*
		Point p = ray.origin;
		Vector u = ray.direction;
		Vector v = new Vector(center, p);
		double b = 2 * (v.dot(u));
		double c = v.dot(v) - radius*radius;
		double discriminant = b*b - 4*c;

		if(discriminant < 0) return null;

		double tMinus = (-b - Math.sqrt(discriminant)) / 2;
		double tPlus = (-b + Math.sqrt(discriminant)) / 2;

		if(tMinus < 0 && tPlus < 0) {
			// sphere is behind the ray
			return null;
		}

		double tValue;
		Vector normal;
		Point intersection;
		boolean incoming;
		if(tMinus < 0 && tPlus > 0) {
			// ray origin lies inside the sphere. take tPlus
			tValue = tPlus;
//			return null;
			intersection = ray.getEnd(tValue);
			normal = new Vector(intersection, center);
			incoming = false;
		} else {
			// both roots positive. take tMinus
			tValue = tMinus;
			intersection = ray.getEnd(tValue);
			normal = new Vector(center, intersection);
			incoming = true;
		}

		return new RayHit(ray, this, normal, intersection, incoming);
//*/
	}

	@Override
	public boolean contains(Point p) {
		return new Vector(center, p).magnitude() < radius;
	}

	public String toString() {
		return pigment + " sphere";
	}
}
