package raytracer.pigments;

import raytracer.*;

import java.awt.Color;

public class GradientPigment implements Pigment {
	private final Point origin;
	private final Vector v;
	private final Color start;
	private final Color end;

	public GradientPigment(Point origin, Vector vector, Color start, Color end) {
		this.origin = origin;
		this.v = vector;
		this.start = start;
		this.end = end;
	}

	public Color getColor(Point p) {
		double d = new Vector(origin, p).dot(v);
		double percent = d / v.getMagnitude();

		return ColorUtil.blend(ColorUtil.intensify(start, (float)percent), ColorUtil.intensify(end, 1.0f - (float)percent));
	}

	public String toString() {
		return "gradiented";
	}
}
