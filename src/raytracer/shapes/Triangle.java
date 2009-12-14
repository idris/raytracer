package raytracer.shapes;

import raytracer.*;

public class Triangle extends Shape {
	private final Point p1, p2, p3;
	private final Vector u, v;
	private final Plane plane;
	private final Vector normal;

	public Triangle(Point p1, Point p2, Point p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		this.u = new Vector(p1, p2);
		this.v = new Vector(p1, p3);
		this.normal = u.cross(v).normalize();

		double a = normal.x;
		double b = normal.y;
		double c = normal.z;
		double d = p1.x * normal.x + p1.y * normal.y + p1.z * normal.z;

		this.plane = new Plane(a, b, c, -d);
	}

	@Override
	public RayHit intersect(Ray ray) {
		RayHit planeHit = plane.intersect(ray);
		if(planeHit == null) return null;

		double uu, uv, vv, wu, wv, D;
		uu = u.dot(u);
		uv = u.dot(v);
		vv = v.dot(v);
		Vector w = new Vector(planeHit.point.plus(new Vector(p1).negate()));

		wu = w.dot(u);
		wv = w.dot(v);
		D = uv * uv  - uu * vv;

		double s, t;
		s = (uv * wv - vv * wu) / D;
		if(s < 0 || s > 1) return null;
		t = (uv * wu - uu * wv) / D;
		if(t < 0 || (s + t) > 1) return null;

		return new RayHit(planeHit.ray, this, planeHit.normal, planeHit.point, true);
	}
}
