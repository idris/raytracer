package raytracer;

import java.awt.Color;

public class AmbientLight extends Light {
	public AmbientLight(Point location, Color color, float a, float b, float c) {
		super(location, color, a, b, c);
	}

	@Override
	public Color getColor(RayHit hit, Ray lightRay) {
		return ColorUtil.intensify(color, hit.shape.finish.amb);
	}
}
