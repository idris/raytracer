package raytracer;

public class Point {
	public double x, y, z;

	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double distanceTo(Point p) {
		return Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y) + (p.z - z)*(p.z - z));
	}

	public Point plus(Vector v) {
		return new Point(x + v.x, y + v.y, z + v.z);
	}

	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
