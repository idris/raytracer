package raytracer.pigments;

import raytracer.Point;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public class TexmapPigment implements Pigment {
	private BufferedImage image;
	private int rows, cols;
	private double sa, sb, sc, sd, ta, tb, tc, td;

	public TexmapPigment(File bmpFile, double sa, double sb, double sc, double sd, double ta, double tb, double tc, double td) {
		try {
			image = ImageIO.read(bmpFile);
		} catch(Exception ex) {
			throw new RuntimeException("Error loading texmap file " + bmpFile.getName());
		}

		this.sa = sa;
		this.sb = sb;
		this.sc = sc;
		this.sd = sd;
		this.ta = ta;
		this.tb = tb;
		this.tc = tc;
		this.td = td;

		this.cols = image.getWidth();
		this.rows = image.getHeight();
	}

	public Color getColor(Point p) {
		double s = sa*p.x + sb*p.y + sc*p.z + sd;
		double t = ta*p.x + tb*p.y + tc*p.z + td;

		return new Color(image.getRGB((int)Math.round(s * cols), (int)Math.round(t * rows)));
	}
}
