package com.github.idris.raytracer.shapes;

import com.github.idris.raytracer.RayHit;
import com.github.idris.raytracer.Ray;
import com.github.idris.raytracer.Point;
import com.github.idris.raytracer.pigments.Finish;
import com.github.idris.raytracer.pigments.Pigment;

import java.awt.Color;


public abstract class Shape {
	public Pigment pigment;
	public Finish finish;

	public final void setMaterial(Pigment pigment, Finish finish) {
		this.pigment = pigment;
		this.finish = finish;
	}

	public abstract RayHit intersect(Ray ray);

	public boolean contains(Point p) {
		return false;
	}

	public final Color getColor(Point p) {
		return pigment.getColor(p);
	}
}
