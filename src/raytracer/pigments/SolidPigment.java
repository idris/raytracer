package raytracer.pigments;

import java.awt.*;

public class SolidPigment implements Pigment {
	private Color color;

	public SolidPigment(Color color) {
		this.color = color;
	}
}
