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
		Point p = ray.origin;
		Vector u = ray.direction;
		Vector v = new Vector(center, p);
		double b = 2 * (v.dot(u));
		double c = v.dot(v) - radius*radius;
		double discriminant = b*b - 4*c;

		if(discriminant < 0) return null;

		double tMinus = (-b - Math.sqrt(discriminant)) / 2;
		double tPlus = (-b + Math.sqrt(discriminant)) / 2;
		double tValue;

		if(tMinus < 0 && tPlus < 0) {
			// sphere is behind the ray
			return null;
		} else if(tMinus < 0 && tPlus > 0) {
			// ray origin lies inside the sphere. take tPlus
			tValue = tPlus;
//			return null;
		} else {
			// both roots positive. take tMinus
			tValue = tMinus;
		}

		Vector normal = new Vector(center, ray.getEnd(tValue));

		return new RayHit(ray, this, normal, tValue);
	}
}
