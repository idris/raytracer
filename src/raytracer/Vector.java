package raytracer;

public class Vector {
	public double x, y, z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(Point p) {
		this(p.x, p.y, p.z);
	}

	/**
	 * Create a new vector from point1 to point2.
	 * @param from
	 * @param to
	 */
	public Vector(Point from, Point to) {
		this(to.x - from.x, to.y - from.y, to.z - from.z);
	}

	public Vector normalize() {
		double magnitude = magnitude();
		double divisor;
		if(magnitude == 0) {
			Log.error("Trying to normalize a Vector with magnitude 0.");
			divisor = Double.POSITIVE_INFINITY;
		}
		else divisor = 1 / magnitude;

		return this.times(divisor);
	}

	public double magnitude() {
		return Math.sqrt(this.dot(this));
	}

	public Vector plus(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}

	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}

	public Vector negate() {
		return times(-1);
	}

	public Vector times(double scalar) {
		return new Vector(x * scalar, y * scalar, z * scalar);
	}

	public Vector cross(Vector v) {
		return new Vector(((y * v.z) - (z * v.y)),
						  ((z * v.x) - (x * v.z)),
						  ((x * v.y) - (y * v.x)));
	}

	public double dot(Vector v) {
		return (x * v.x) + (y * v.y) + (z * v.z);
	}

	public static Vector halfway(Vector v1, Vector v2) {
		return v1.plus(v2).normalize();
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
