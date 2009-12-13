package raytracer;

import raytracer.pigments.*;
import raytracer.shapes.*;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class RayTracer {
	public static final int MAX_RECURSION_LEVEL = 3;
	public static final Color BACKGROUND_COLOR = Color.GRAY;

	private Camera camera;
	private final ArrayList<Light> lights = new ArrayList<Light>();
	private final ArrayList<Pigment> pigments = new ArrayList<Pigment>();
	private final ArrayList<Finish> finishes = new ArrayList<Finish>();
	private final ArrayList<Shape> shapes = new ArrayList<Shape>();
	private final int cols, rows;

	public RayTracer(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
	}


	private Color shade(RayHit hit, int depth) {
		Color color = Color.BLACK;

		// ambient light source
		Light light = lights.get(0);
		if(light != null) {
			color = ColorUtil.blend(color, ColorUtil.intensify(hit.shape.getColor(hit.point), light.color));
		}

		for(int i = 1;i < lights.size();i++) {
			Log.debug("Checking light " + i + ":");
			light = lights.get(i);
			Vector lightRayVec = new Vector(hit.point, light.location);
			Ray lightRay = new Ray(hit.point, lightRayVec);
			lightRay.t = lightRayVec.getMagnitude();

			Log.debug("  light ray = " + lightRay);
			RayHit obstruction = findHit(lightRay);
			if(obstruction == null) {
				// not in the shadow
				//              add the basic Phong shading for this light
				//                (diffuse, specular components)
				Log.debug("  Light is visible:");

				Color c = light.getColor(hit, lightRay);
//				Log.debug("  final color   = " + c);
				color = ColorUtil.blend(color, c);
			}
		}

		if(depth <= MAX_RECURSION_LEVEL) {
			if(hit.shape.finish.isReflective()) {
				ColorUtil.blend(color, ColorUtil.intensify(trace(hit.getReflectionRay(), depth++), hit.shape.finish.refl));
			}

			if(hit.shape.finish.isTransmittive()) {
				ColorUtil.blend(color, ColorUtil.intensify(trace(hit.getTransmissionRay(), depth++), hit.shape.finish.trans));
			}
		}

		return color;

		// Possible Outline:
		//  Get the normal vector from hit
		//  Get the contact point as R's endpoint
		//  Get the pigment and surface finish from hitObj
		//  Initialize accumulated color to Black (0,0,0)
		//  for each (light source i)
		//      if (i is the ambient source) add ambient shading to accumulated color
		//      else
		//          Ray LightRay = ray from contact point to light
		//          call Hit(LightRay) to determine whether in shadow
		//          if (not in shadow) skip the next step
		//              add the basic Phong shading for this light
		//                (diffuse, specular components)
		//  if (reflective)
		//      increment recursion level: lev++
		//      shoot reflection ray and add its contribution
		//  if (transmittive)
		//      increment recursion level: lev++
		//      shoot refraction ray and add its contribution
		//  return the final accumulated color
	}

	private RayHit findHit(Ray ray) {
		RayHit hit = null;

		for(Shape shape: shapes) {
			RayHit h = shape.intersect(ray);
			Log.debug("    Testing object " + shape + ": " + (h == null?"missed":"hit"));
			if(h != null && h.t < ray.t + 0.001) {
				Log.debug("      hit at t=" + h.t + ". point=" + h.point);
				hit = h;
				ray.t = h.t;
			}
		}

		return hit;
	}

	private Color trace(Ray ray, int depth) {
		Log.debug("Tracing ray " + ray);

		RayHit hit = findHit(ray);

		if(hit != null) {
			return shade(hit, depth);
		}

		// missed everything. return background color
		return BACKGROUND_COLOR;

		// Possible Outline:
		//  if (lev exceeds MAX_LEVEL) return the default color
		//  int hitObj = -1
		//  for i = each of the nObjs do
		//      RayHit h = theShapes[i]->intersect(R)
		//      if (h.status != MISS && h.tValue < R.tValue)
		//          hitObj = i
		//          hit = h
		//          set R.tValue to h.tValue
		//  if (hitObj  < 0)
		//      missed everything, return the default color (e.g., gray)
		//  else
		//      return Shade(hitObj, R, hit, lev)
	}


	public void draw(File outFile) throws IOException, InterruptedException {
		BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

		for(int r = 0;r < rows; r++) {
			if(r % 5 == 0) Log.info((rows - r) + " rows left to trace.");
			for(int c = 0;c < cols; c++) {
				image.setRGB(c, r, getPixelColor(c, r).getRGB());
			}
		}

		ImageIO.write(image, "bmp", outFile);
	}


	public Color getPixelColor(int col, int row) throws IOException {
		int bmpRow = rows-1 - row;
		Log.debug("Tracing ray (col=" + col + ", row=" + row + ")");
		Log.debug("  [Note: In bmp format this is row " + bmpRow + "]");
		Ray ray = camera.getRay(col, bmpRow);
		Color color = trace(ray, 0);
		return color;
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
			float a = scanner.nextFloat();
			float b = scanner.nextFloat();
			float c = scanner.nextFloat();
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
			} else if("gradient".equals(name)) {
				pigments.add(new GradientPigment(readPoint(scanner), readVector(scanner), readColor(scanner), readColor(scanner)));
			} else if("texmap".equals(name)) {
				pigments.add(new TexmapPigment(new File(scanner.next()), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
			} else {
				throw new UnsupportedOperationException("Unrecognized pigment: '" + name + "'.");
			}
		}

		// read surface finishes
		int numFins = scanner.nextInt();
		for(int i=0;i<numFins;i++) {
			finishes.add(new Finish(scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()));
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
			} else if("disc".equals(name)) {
				shape = new Disc(readPoint(scanner), readVector(scanner), scanner.nextDouble());
			} else if("polyhedron".equals(name)) {
				int numFaces = scanner.nextInt();
				ArrayList<PolyhedronFace> faces = new ArrayList<PolyhedronFace>(numFaces);
				for(int f=0;f<numFaces;f++) {
					faces.add(new PolyhedronFace(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
				}
				shape = new Polyhedron(faces);
			} else if("triangle".equals(name)) {
				shape = new Triangle(readPoint(scanner), readPoint(scanner), readPoint(scanner));
			} else if("parallelogram".equals(name)) {
				shape = new Parallelogram(readPoint(scanner), readPoint(scanner), readPoint(scanner));
			} else if("bezier".equals(name)) {
				ArrayList<Point> points = new ArrayList<Point>(16);
				for(int s=0;s<16;s++) {
					points.add(readPoint(scanner));
				}
				shape = new Bezier(points);
			} else {
				throw new UnsupportedOperationException("Unrecognized shape: '" + name + "'.");
			}

			shape.setMaterial(pigments.get(pigNum), finishes.get(finishNum));
			shapes.add(shape);
		}
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
