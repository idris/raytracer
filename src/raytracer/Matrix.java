package raytracer;

public class Matrix {
	private double m[][];

	public Matrix(double m[][]) {
		if(m.length < 4 || m[0].length < 4) throw new IllegalArgumentException("Matrix must be a 4x4 array");
		this.m = m;
	}

/*
	public Matrix inverse() {
		double i[][] = new double[4][4];

		return i;
	}
*/

	public Matrix transpose() {
		double t[][] = new double[4][4];
		t[0][0] = m[0][0];
		t[1][0] = m[0][1];
		t[2][0] = m[0][2];
		t[3][0] = m[0][3];
		t[0][1] = m[1][0];
		t[1][1] = m[1][1];
		t[2][1] = m[1][2];
		t[3][1] = m[1][3];
		t[0][2] = m[2][0];
		t[1][2] = m[2][1];
		t[2][2] = m[2][2];
		t[3][2] = m[2][3];
		t[0][3] = m[3][0];
		t[1][3] = m[3][1];
		t[2][3] = m[3][2];
		t[3][3] = m[3][3];

		return new Matrix(t);
	}

	public Matrix times(Matrix matrix) {
		double m2[][] = matrix.m;
		double r[][] = new double[4][4];

		for(int row=0;row<4;row++) {
			for(int col=0;col<4;col++) {
				r[row][col] = m[row][0] * m2[0][col] + m[row][1] * m2[1][col] + m[row][2] * m2[2][col] + m[row][3] * m2[3][col];
			}
		}

		return new Matrix(r);
	}

	public Vector times(Vector v) {
		double x, y, z;

		x = m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3] * 1.0;
		y = m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3] * 1.0;
		z = m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3] * 1.0;

		// fourth coordinate
		double mag = m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3] * 1.0;

		x /= mag;
		y /= mag;
		z /= mag;

		return new Vector(x, y, z);
	}
}
