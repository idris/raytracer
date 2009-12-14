package raytracer.shapes;

import raytracer.*;

public class Polygon {
	final double a, b, c, d;
	final Vector normal;
	final Plane plane;

	public Polygon(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;

		this.normal = new Vector(a, b, c).normalize();
		this.plane = new Plane(a, b, c, d);
	}
}
