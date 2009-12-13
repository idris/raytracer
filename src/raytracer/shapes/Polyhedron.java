package raytracer.shapes;

import raytracer.Ray;
import raytracer.RayHit;

import java.util.List;

public class Polyhedron extends Shape {
	private List<PolyhedronFace> faces;

	public Polyhedron(List<PolyhedronFace> faces) {
		this.faces = faces;
	}

	@Override
	public RayHit intersect(Ray ray) {
		return null;
	}
}
