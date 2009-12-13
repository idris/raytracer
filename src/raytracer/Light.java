package raytracer;

import java.awt.Color;

public class Light {
	public final Point location;
	public final Color color;
	final double a, b, c;

	public Light(Point location, Color color, double a, double b, double c) {
		this.location = location;
		this.color = color;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 *
	 * @param d - distance
	 * @return attenuation factor at distance d
	 */
	public double getAttenuationFactor(double d) {
		return 1 / (a + b*c + c*(d*d));
	}

	public Color getColor(RayHit hit, Ray lightRay) {
		double distance = lightRay.origin.distanceTo(location);
		Log.debug("  distance      = " + distance);
		double attenuationFactor = getAttenuationFactor(distance);
		Log.debug("  attenuation   = " + attenuationFactor);

		// diffuse
		Log.debug("  normal vector = " + hit.normal);
		Log.debug("  light vector  = " + lightRay.direction);
		double diffuseStrength = hit.shape.finish.diff * Math.max(0.0, hit.normal.dot(lightRay.direction));
		Log.debug("  diff strength = " + diffuseStrength);

		// specular
		Vector halfway = Vector.halfway(hit.ray.direction, lightRay.direction);
		Log.debug("  halfway vector= " + halfway);
		double specularStrength = hit.shape.finish.spec * Math.pow(Math.max(0.0, hit.normal.dot(halfway)), hit.shape.finish.shiny);
		Log.debug("  spec strength = " + specularStrength);

		float[] shapeColor = hit.shape.getColor(hit.point).getRGBColorComponents(new float[3]);
		float[] intensity = color.getRGBColorComponents(new float[3]);
		double red = (0.0 * hit.shape.finish.amb * intensity[0] * shapeColor[0]) + (intensity[0] * attenuationFactor * (shapeColor[0] * diffuseStrength + specularStrength));
		double green = (0.0 * hit.shape.finish.amb * intensity[1] * shapeColor[1]) + (intensity[1] * attenuationFactor * (shapeColor[1] * diffuseStrength + specularStrength));
		double blue = (0.0 * hit.shape.finish.amb * intensity[2] * shapeColor[2]) + (intensity[2] * attenuationFactor * (shapeColor[2] * diffuseStrength + specularStrength));

		Log.debug("  final color   = (" + red + ", " + green + ", " + blue + ")");

		return new Color((float)red, (float)green, (float)blue);
	}
}
