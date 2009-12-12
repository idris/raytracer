package raytracer;

import java.awt.*;

public class Light {
	Point location;
	Color color;
	double a, b, c;

	public Light(Point location, Color color, double a, double b, double c) {
		this.location = location;
		this.color = color;
		this.a = a;
		this.b = b;
		this.c = c;
	}
}
