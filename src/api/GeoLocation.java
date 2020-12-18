package api;

public class GeoLocation implements geo_location{

	double x;
	double y;
	double z;

	public GeoLocation ()
	{
		x=0;
		y=0;
		z=0;
	}
	public GeoLocation (double x,double y,double z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}

	public GeoLocation(String pos) {

		int a = pos.indexOf(',');
		String A = pos.substring(0, a);
		x = Double.parseDouble(A);

		int b = pos.indexOf(',', a+1);
		String B = pos.substring(a+1, b);
		y = Double.parseDouble(B);

		String C = pos.substring(b+1);
		z = Double.parseDouble(C);

	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	public double distance(geo_location g) {
		double a = x-g.x();
		double b = y-g.y();
		double c = z-g.z();
		double dist = Math.sqrt(a*a+b*b+c*c);
		return dist;
	}

}