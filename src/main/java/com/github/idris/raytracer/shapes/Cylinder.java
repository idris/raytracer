package com.github.idris.raytracer.shapes;

import com.github.idris.raytracer.Vector;
import com.github.idris.raytracer.RayHit;
import com.github.idris.raytracer.Ray;
import com.github.idris.raytracer.Log;
import com.github.idris.raytracer.Point;

public class Cylinder extends Shape {
	private Point base;
	private Vector axis;
	private double radius;

	public Cylinder(Point base, Vector axis, double radius) {
		this.base = base;
		this.axis = axis;
		this.radius = radius;

		Log.warn("Cylinder shape is not supported. This shape will be ignored.");
	}

	public RayHit intersect(Ray ray) {
		return null;
	}
}
