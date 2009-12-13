package raytracer;

public class Ray {
	public final Point origin;
	public final Vector direction;
	public double t;

	public Ray(Point origin, Vector direction) {
		this(origin, direction, true);
	}

	public Ray(Point origin, Vector direction, boolean adjustForError) {
		this.t = Double.POSITIVE_INFINITY;

		this.direction = direction.normalize();

		if(adjustForError) origin = origin.plus(this.direction.times(0.001));

		this.origin = origin;
	}

	public Double intersects(Point p) {
		double t = Double.POSITIVE_INFINITY;
		return t;
	}

	public Point getEnd(double t) {
		return origin.plus(direction.times(t));
	}

	public Point getEnd() {
		return getEnd(this.t);
	}

	public String toString() {
		return "Org:" + origin + " Dir:" + direction + " t:" + t;
	}
}
