package raytracer;

import raytracer.pigments.*;
import raytracer.shapes.*;
import raytracer.shapes.Shape;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class RayTracer {
	public static final int MAX_RECURSION_LEVEL = 3;

	private Camera camera;
	private ArrayList<Light> lights = new ArrayList<Light>();
	private ArrayList<Pigment> pigments = new ArrayList<Pigment>();
	private ArrayList<Finish> finishes = new ArrayList<Finish>();
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private int cols, rows;

	public RayTracer(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}


	private Color trace(Ray ray, int depth) {
		return Color.RED;
	}


	public void readScene(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);

		// read view
		Point eye = readPoint(scanner);
		Point center = readPoint(scanner);
		Vector up = readVector(scanner);
		double fovy = scanner.nextDouble();
		camera = new Camera(eye, center, up, fovy, cols, rows);

		// read lights
		int numLights = scanner.nextInt();
		for(int i=0;i<numLights;i++) {
			Point location = readPoint(scanner);
			Color color = readColor(scanner);
			double a = scanner.nextDouble();
			double b = scanner.nextDouble();
			double c = scanner.nextDouble();
			lights.add(new Light(location, color, a, b, c));
		}

		// read pigments
		int numPigments = scanner.nextInt();
		for(int i=0;i<numPigments;i++) {
			String name = scanner.next();
			if("solid".equals(name)) {
				pigments.add(new SolidPigment(readColor(scanner)));
			} else if("checker".equals(name)) {
				pigments.add(new CheckerPigment(readColor(scanner), readColor(scanner), scanner.nextDouble()));
			} else {
				throw new UnsupportedOperationException("Unrecognized pigment: '" + name + "'.");
			}
		}

		// read surface finishes
		int numFins = scanner.nextInt();
		for(int i=0;i<numFins;i++) {
			finishes.add(new Finish(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
		}

		// read shapes
		int numShapes = scanner.nextInt();
		for(int i=0;i<numShapes;i++) {
			int pigNum = scanner.nextInt();
			int finishNum = scanner.nextInt();
			String name = scanner.next();
			Shape shape;
			if("sphere".equals(name)) {
				shape = new Sphere(readPoint(scanner), scanner.nextDouble());
			} else if("plane".equals(name)) {
				shape = new Plane(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
			} else if("cylinder".equals(name)) {
				shape = new Cylinder(readPoint(scanner), readVector(scanner), scanner.nextDouble());
			} else if("cone".equals(name)) {
				shape = new Cone(readPoint(scanner), readVector(scanner), scanner.nextDouble());
			} else {
				throw new UnsupportedOperationException("Unrecognized shape: '" + name + "'.");
			}

			shape.setMaterial(pigments.get(pigNum), finishes.get(finishNum));
			shapes.add(shape);
		}
	}

	public void draw(File outFile) throws IOException {
		BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

		for(int r = 0;r < rows; r++) {
			for(int c = 0;c < cols; c++) {
				Ray ray = new Ray();
				Color color = trace(ray, 0);
				image.setRGB(c, r, color.getRGB());
			}
		}

		ImageIO.write(image, "bmp", outFile);
	}


	private static Color readColor(Scanner scanner) {
		return new Color(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat());
	}
	private static Vector readVector(Scanner scanner) {
		return new Vector(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
	}
	private static Point readPoint(Scanner scanner) {
		return new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
	}
}
