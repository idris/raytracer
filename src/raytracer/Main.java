package raytracer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class Main {
	private static final String USAGE = "Usage:\n"+
			"        raytracer infile bmpfile width height [-options]\n"+
			"\n"+
			"    where:\n"+
			"        infile    - input file name\n"+
			"        bmpfile   - bmp output file name\n"+
			"        width     - image width (in pixels)\n"+
			"        height    - image hreight (in pixels)\n"+
			"        -test     - run in test mode (see below)\n"+
			"        -noshadow - don't compute shadows\n"+
			"        -noreflec - don't do reflections\n"+
			"        -notrans  - don't do transparency\n"+
			"        -nocap    - cylinders and cones are infinite";

	public static boolean DEBUG = false;


	private static void printUsage() {
		System.out.println(USAGE);
	}

	public static void main(String[] args) throws IOException {
		if(args.length < 5) {
			printUsage();
			System.exit(0);
		}

		// required arguments
		File inFile = new File(args[1]);
		File outFile = new File(args[2]);
		int cols = Integer.parseInt(args[3]);
		int rows = Integer.parseInt(args[4]);

		// optional arguments
		for(String arg: args) {
			if("-test".equals(arg)) {
				DEBUG = true;
			} else {
				System.out.print("Unrecognized option: '" + arg + "' ignored.");
			}
		}

		RayTracer rayTracer = new RayTracer(cols, rows);
		rayTracer.readScene(inFile);
		rayTracer.draw(outFile);
	}
}
