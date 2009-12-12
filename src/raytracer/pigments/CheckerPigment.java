package raytracer.pigments;

import java.awt.*;

public class CheckerPigment implements Pigment {
	private Color color1;
	private Color color2;
	private double scale;

	public CheckerPigment(Color color1, Color color2, double scale) {
		this.color1 = color1;
		this.color2 = color2;
		this.scale = scale;
	}
}
