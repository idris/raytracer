package raytracer;

import java.awt.Color;

public class ColorUtil {
	public static Color blend(Color base, Color mixin) {
		float[] baseC = base.getRGBColorComponents(null);
		float[] mixinC = mixin.getRGBColorComponents(null);
//		float[] factorC = factor.getRGBColorComponents(null);
		float[] factorC = new float[]{1.0f,1.0f,1.0f};

		float red = clamp(baseC[0] + (mixinC[0] * factorC[0]));
		float green = clamp(baseC[1] + (mixinC[1] * factorC[1]));
		float blue = clamp(baseC[2] + (mixinC[2] * factorC[2]));
		return new Color(red, green, blue);
	}

	public static float clamp(float x) {
		return Math.max(0.0f, Math.min(1.0f, x));
	}

	public static Color intensify(Color color, float intensity) {
		// TODO: clamp should not be necessary here
		return intensify(color, new Color(clamp(intensity), clamp(intensity), clamp(intensity)));
	}

	public static Color intensify(Color color, Color intensity) {
		float[] c = color.getRGBColorComponents(null);
		float[] i = intensity.getRGBColorComponents(null);

		return new Color(c[0] * i[0], c[1] * i[1], c[2] * i[2]);
	}
}
