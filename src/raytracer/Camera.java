package raytracer;

public class Camera {
	private Point eye;
	private Vector vx;
	private Vector vy;
	private Vector vz;

	private double windowDistance;
	private double windowWidth;
	private double windowHeight;

	private double rows, cols;
//	private double fovy, fovx;

	public Camera(Point eye, Point center, Vector up, double fovy, int cols, int rows) {
		fovy = Math.toRadians(fovy);
		double fovx = fovy * cols / rows;

		Vector at = new Vector(eye, center);
		vz = at.negate().normalize();
		vx = up.cross(vz).normalize();
		vy = vz.cross(vx);

		this.eye = eye;
//		this.center = center;
//		this.up = up;
		this.cols = cols;
		this.rows = rows;

		windowDistance = 1.0;
		windowHeight = Math.sin(fovy / 2.0) * windowDistance * 2.0;
		windowWidth = Math.sin(fovx / 2.0) * windowDistance * 2.0;

		Log.debug("  Viewframe:");
		Log.debug("    Org: " + eye.toString());
		Log.debug("    X:   " + vx.toString());
		Log.debug("    Y:   " + vy.toString());
		Log.debug("    Z:   " + vz.toString());

		Log.debug("    Window width: " + windowWidth);
		Log.debug("          height: " + windowHeight);
	}

	public Ray getRay(int col, int row) {
		return getRay(col, row, 0.5, 0.5);
	}
	public Ray getRay(int col, int row, double pixelAdjustmentX, double pixelAdjustmentY) {
		double x = (((double)col + pixelAdjustmentX) / cols) * windowWidth - (windowWidth / 2.0);
		double y = (((double)row + pixelAdjustmentY) / rows) * windowHeight - (windowHeight / 2.0);

		Vector v = new Vector(eye, convertCoords(new Point(x, y, -windowDistance)));

		Log.debug("  Generating ray:");
		Log.debug("    Window coordinates: (" + x + ", " + y + ")");
		Log.debug("    Passes through window point: " + v);

		Ray ray = new Ray(eye, v);
		Log.debug("    Final ray: " + ray);

		return ray;
	}

	public Point convertCoords(Point p) {
		Vector v = convertCoords(new Vector(p.x, p.y, p.z));
		return new Point(v.x, v.y, v.z);
	}

	public Vector convertCoords(Vector p) {
		Matrix rT = new Matrix(new double[][]{
				{vx.x, vy.x, vz.x, 0},
				{vx.y, vy.y, vz.y, 0},
				{vx.z, vy.z, vz.z, 0},
				{0, 0, 0, 1}
		});
		Matrix tInv = new Matrix(new double[][]{
				{1, 0, 0, eye.x},
				{0, 1, 0, eye.y},
				{0, 0, 1, eye.z},
				{0, 0, 0, 1}
		});
/*
		Matrix rT = new Matrix(new double[][]{
				{vx.x, vx.y, vx.z, 0},
				{vy.x, vy.y, vy.z, 0},
				{vz.x, vz.y, vz.z, 0},
				{0, 0, 0, 1}
		});
		Matrix tInv = new Matrix(new double[][]{
				{1, 0, 0, -eye.x},
				{0, 1, 0, -eye.y},
				{0, 0, 1, -eye.z},
				{0, 0, 0, 1}
		});
*/
		Matrix matrix = tInv.times(rT);
		Vector v = matrix.times(new Vector(p.x, p.y, p.z));
		return v;
	}
}
