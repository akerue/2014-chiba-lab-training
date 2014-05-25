package particlesimulation;

public class Particle {
	public Position position;
	public Velocity velocity;
	public double mass;

	public static Particle[] obj_list;

	public Particle(double x, double y, double z, double mass, double v_x, double v_y, double v_z){
		this.position = new Position(x, y, z);
		this.mass = mass;
		this.velocity = new Velocity(v_x, v_y, v_z);
	}
	
	public double calculate_distance(Particle p){
		double delta_x = position.x - p.position.x;
		double delta_y = position.y - p.position.y;
		double delta_z = position.z - p.position.z;

		return Math.sqrt(Math.pow(delta_x, 2.0) + 
				 Math.pow(delta_y, 2.0) + 
				 Math.pow(delta_z, 2.0));
	}

	public DirectionVector calculate_vector(Particle p){
		return new DirectionVector(p.position.x - this.position.x, 
					   p.position.y - this.position.y, 
					   p.position.z - this.position.z);
	}
	
	private Power force_function(Particle p){
		double power_value;
		DirectionVector v = calculate_vector(p);
		if (v.get_value() != 0.0) {
			power_value = - (p.mass * this.mass)/Math.pow(v.get_value(), 2.0);
		} else {
			power_value = 0.0;
		}
		return new Power(power_value*v.x_vector(), 
				  power_value*v.y_vector(),
				  power_value*v.z_vector());
	}

	private double kinetic_equation(double v, double a, double t) {
		return v + a * t;
	}
	
	public Velocity calculate_velocity(Power power, double t){
		// calculate v after passing t seconds
		
		return new Velocity(kinetic_equation(this.velocity.x, power.x/this.mass, t), 
				    kinetic_equation(this.velocity.y, power.y/this.mass, t),
				    kinetic_equation(this.velocity.z, power.z/this.mass, t));
	}

	private double move_equation(double v, double a, double t){
		return (v * t + a * t * t / 2);
	}

	private double fix_position(double pos, double limit){
		if (pos > limit){
			return fix_position(pos % limit, limit);
		} else if (pos < 0.0) {
			return fix_position(pos % limit + limit, limit);
		} else {
			return pos;
		}
	}

	public Power calculate_power(){
		Power total_power = new Power(0.0, 0.0, 0.0);
		for (int i = 0; i < Particle.obj_list.length; i++) {
			if (Particle.obj_list[i] == this){
				continue;
			}
			total_power.add(force_function(Particle.obj_list[i]));
		}
		return total_power;
	}

	public Position calculate_position(Power power, double t){
		// calculate position after passing t seconds

		double next_x, next_y, next_z;
		double limit = ParticleSimulation.WIDTH;

		next_x = fix_position(
			this.position.x + move_equation(this.velocity.x, power.x/this.mass, t), 
			limit);
	        next_y = fix_position(
			this.position.y + move_equation(this.velocity.y, power.y/this.mass, t), 
			limit);
	        next_z = fix_position(
			this.position.z + move_equation(this.velocity.z, power.z/this.mass, t), 
			limit);
		return new Position(next_x, next_y, next_z);
	}

	public void update_position(Power power, double t){
		// calculate position after passing t seconds

		double limit = ParticleSimulation.WIDTH;
		this.position.x = 
			fix_position(this.position.x + 
				move_equation(this.velocity.x, power.x/this.mass, t), limit);
		this.position.y = 
			fix_position(this.position.y + 
				move_equation(this.velocity.y, power.y/this.mass, t), limit);
		this.position.z = 
			fix_position(this.position.z + 
				move_equation(this.velocity.z, power.z/this.mass, t), limit);
	}

}
