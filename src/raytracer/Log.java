package raytracer;

public class Log {
	public static void error(String msg) {
		System.err.println("ERROR: " + msg);
	}

	public static void warn(String msg) {
		System.out.println("Warning: " + msg);
	}

	public static void info(String msg) {
		System.out.println(msg);
	}

	public static void debug(String msg) {
		if(Main.DEBUG) System.out.println(msg);
	}
}
