package raytracer.pigments;

import raytracer.Point;

import java.awt.Color;


public interface Pigment {
	public Color getColor(Point p);
}
