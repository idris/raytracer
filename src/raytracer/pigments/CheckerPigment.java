package raytracer.pigments;

import raytracer.Point;

import java.awt.Color;


public class CheckerPigment implements Pigment {
	private Color color1;
	private Color color2;
	private double scale;

	public CheckerPigment(Color color1, Color color2, double scale) {
		this.color1 = color1;
		this.color2 = color2;
		this.scale = scale;
	}

	public Color getColor(Point p) {
		int which = (floor(p.x/scale) + floor(p.y/scale) + floor(p.z/scale)) % 2;
		if(which == 0) return color1;
		return color2;
	}

	private int floor(double d) {
		return (int)Math.abs(Math.floor(d));
	}

	public String toString() {
		return "checkered";
	}
}
