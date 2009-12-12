package raytracer.pigments;

public class Finish {
	double amb, diff, spec, shiny, refl, trans, ior;

	public Finish(double amb, double diff, double spec, double shiny, double refl, double trans, double ior) {
		this.amb = amb;
		this.diff = diff;
		this.spec = spec;
		this.shiny = shiny;
		this.refl = refl;
		this.trans = trans;
		this.ior = ior;
	}
}
