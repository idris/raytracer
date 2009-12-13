package raytracer;

public class Camera {
	public Point eye;
	public Vector vx;
	public Vector vy;
	public Vector vz;

	private double windowDistance;
	private double windowWidth;
	private double windowHeight;

	private double rows, cols;
	private double fovy, fovx;

	public Camera(Point eye, Point center, Vector up, double fovy, int cols, int rows) {
		this.fovy = fovy = Math.toRadians(fovy);
		fovx = fovy * cols / rows;

		Vector at = new Vector(eye, center);
		vz = at.negate().normalize();
		vx = up.cross(vz).normalize();
		vy = vz.cross(vx);

		this.eye = eye;
//		this.center = center;
//		this.up = up;
		this.fovy = fovy;
		this.fovx = fovy * cols / rows;
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
		double x = ((double)col / cols) * windowWidth - (windowWidth / 2.0);
		double y = ((double)row / rows) * windowHeight - (windowHeight / 2.0);

		double dz = 1.0;
		double dy = Math.sin(fovy) * ((y - windowHeight/2.0) / dz);
		double dx = Math.sin(fovx) * ((x - windowWidth/2.0) / dz);

//		Vector v = vz.times(dz).negate().plus(vy.times(dy)).plus(vx.times(dx));
		Vector v = new Vector(eye, new Point(x, y, -windowDistance));

		Log.debug("  Generating ray:");
		Log.debug("    Window coordinates: (" + x + ", " + y + ")");
		Log.debug("    Passes through window point: " + v);

		Ray ray = new Ray(eye, v, col, row);
		Log.debug("    Final ray: " + ray);

		return ray;
	}
}
