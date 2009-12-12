package raytracer.shapes;

import raytracer.pigments.Finish;
import raytracer.pigments.Pigment;

public abstract class Shape {
	Pigment pigment;
	Finish finish;

	public void setMaterial(Pigment pigment, Finish finish) {
		this.pigment = pigment;
		this.finish = finish;
	}
}
