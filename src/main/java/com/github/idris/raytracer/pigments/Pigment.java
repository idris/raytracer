package com.github.idris.raytracer.pigments;

import com.github.idris.raytracer.Point;

import java.awt.Color;


public interface Pigment {
	public Color getColor(Point p);
}
