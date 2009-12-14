package raytracer.shapes;

import raytracer.*;
import raytracer.pigments.Finish;
import raytracer.pigments.Pigment;

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
