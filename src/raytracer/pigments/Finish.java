package raytracer.pigments;

public class Finish {
	public float amb, diff, spec, shiny, refl, trans, ior;

	public Finish(float amb, float diff, float spec, float shiny, float refl, float trans, float ior) {
		this.amb = amb;
		this.diff = diff;
		this.spec = spec;
		this.shiny = shiny;
		this.refl = refl;
		this.trans = trans;
		this.ior = ior;
	}

	public boolean isReflective() {
		return refl > 0;
	}

	public boolean isTransmittive() {
		return trans > 0;
	}
}
