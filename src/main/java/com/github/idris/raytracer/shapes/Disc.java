package com.github.idris.raytracer.shapes;

import com.github.idris.raytracer.Vector;
import com.github.idris.raytracer.RayHit;
import com.github.idris.raytracer.Ray;
import com.github.idris.raytracer.Log;
import com.github.idris.raytracer.Point;

public class Disc extends Shape {
	private Point center;
	private Vector normal;
	private double radius;

	public Disc(Point center, Vector normal, double radius) {
		this.center = center;
		this.normal = normal;
		this.radius = radius;

		Log.warn("Disc shape is not supported. This shape will be ignored.");
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
