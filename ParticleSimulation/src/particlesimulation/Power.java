package particlesimulation;

public class Power extends DirectionVector{
	public Power(double x, double y, double z){
		super(x, y, z);
	}

	public void add(Power p){
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
}
