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
import java.util.concurrent.atomic.AtomicInteger;

public class RayTracer {
	public static final int MAX_RECURSION_LEVEL = 5;
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
		if(light != null && hit.shape.finish.amb > 0) {
			color = ColorUtil.blend(color, ColorUtil.intensify(hit.shape.getColor(hit.point), light.getColor(hit, null)));
		}

		for(int i = 1;i < lights.size();i++) {
//			Log.debug("Checking light " + i + ":");
			light = lights.get(i);
			Vector lightRayVec = new Vector(hit.point, light.location);
			Ray lightRay = new Ray(hit.point, lightRayVec);
			lightRay.t = lightRayVec.magnitude();

//			Log.debug("  light ray = " + lightRay);
			RayHit obstruction = findHit(lightRay);
			if(obstruction == null) {
				// not in the shadow
				//              add the basic Phong shading for this light
				//                (diffuse, specular components)
//				Log.debug("  Light is visible:");

				Color c = light.getColor(hit, lightRay);
//				Log.debug("  final color   = " + c);
				color = ColorUtil.blend(color, c);
			}
		}

		if(depth <= MAX_RECURSION_LEVEL) {
			if(hit.shape.finish.isReflective()) {
				color = ColorUtil.blend(color, ColorUtil.intensify(trace(hit.getReflectionRay(), depth+1), hit.shape.finish.refl));
			}

			if(hit.shape.finish.isTransmittive()) {
				color = ColorUtil.blend(color, ColorUtil.intensify(trace(hit.getTransmissionRay(), depth+1), hit.shape.finish.trans));
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
//			Log.debug("    Testing object " + shape + ": " + (h == null?"missed":"hit"));
			if(h != null && h.t < ray.t) {
//				Log.debug("      hit at t=" + h.t + ". point=" + h.point);
				hit = h;
				ray.t = h.t;
			}
		}

		return hit;
	}

	private Color trace(Ray ray, int depth) {
//		Log.debug("Tracing ray " + ray);

		RayHit hit = findHit(ray);

		if(hit != null) {
			return shade(hit, depth);
		}

		// missed everything. return background color
		return BACKGROUND_COLOR;
	}


	public void draw(File outFile) throws IOException, InterruptedException {
		final BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

		long start = System.currentTimeMillis();

		if(Main.MULTI_THREAD) {
			final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			final AtomicInteger remaining = new AtomicInteger(rows * cols);
			for(int r = 0;r < rows; r++) {
				for(int c = 0;c < cols; c++) {
					final int cc = c;
					final int rr = r;
					executor.execute(new Runnable() {
						public void run() {
							image.setRGB(cc, rr, getPixelColor(cc, rr).getRGB());
						}
					});
				}
			}

			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.MINUTES);
		} else {
			for(int r = 0;r < rows; r++) {
				if(r % 5 == 0) Log.info((rows - r) + " rows left to trace.");
				for(int c = 0;c < cols; c++) {
					image.setRGB(c, r, getPixelColor(c, r).getRGB());
				}
			}
		}

		Log.info("Finished in: " + (System.currentTimeMillis()-start) + "ms");

		ImageIO.write(image, "bmp", outFile);
	}


	public Color getPixelColor(int col, int row) {
		int bmpRow = rows-1 - row;
//		Log.debug("Tracing ray (col=" + col + ", row=" + row + ")");
//		Log.debug("  [Note: In bmp format this is row " + bmpRow + "]");

		if(Main.ANTI_ALIAS) {
			Ray ray = camera.getRay(col, bmpRow, 0, 0);
			Color c1 = trace(ray, 0);
			ray = camera.getRay(col, bmpRow, .5, 0);
			Color c2 = trace(ray, 0);
			ray = camera.getRay(col, bmpRow, 0, .5);
			Color c3 = trace(ray, 0);
			ray = camera.getRay(col, bmpRow, .5, .5);
			Color c4 = trace(ray, 0);

			return ColorUtil.average(c1, c2, c3, c4);
		} else {
			Ray ray = camera.getRay(col, bmpRow);
			return trace(ray, 0);
		}
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
		if(numLights > 0) lights.add(new AmbientLight(readPoint(scanner), readColor(scanner), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()));
		for(int i=1;i<numLights;i++) {
			lights.add(new Light(readPoint(scanner), readColor(scanner), scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()));
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
				File bmpFile = new File(scanner.next());
				try {
					pigments.add(new TexmapPigment(bmpFile, scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
				} catch(IOException ex) {
					Log.error("Could not locate texmap file '" + bmpFile.getName() + "'.");
					System.exit(1);
				}
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
				ArrayList<Polygon> faces = new ArrayList<Polygon>(numFaces);
				for(int f=0;f<numFaces;f++) {
					faces.add(new Polygon(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
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
		return new Color(ColorUtil.clamp(scanner.nextFloat()), ColorUtil.clamp(scanner.nextFloat()), ColorUtil.clamp(scanner.nextFloat()));
	}
	private static Vector readVector(Scanner scanner) {
		return new Vector(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
	}
	private static Point readPoint(Scanner scanner) {
		return new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
	}
}
