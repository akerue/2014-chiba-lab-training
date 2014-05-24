package particlesimulation;

public class Particle {
	public int index;
	public Position position;
	public Velocity velocity;
	public double mass;

	public static Particle[] obj_list;

	public static double[] pos_list;
	// Repeat x, y and z field
	// [x-1, y-1, z-1, x-2, y-2, z-2...]

	public Particle(double x, double y, double z, double mass, Velocity initial_velocity){
		this.position = new Position(x, y, z);
		this.mass = mass;
		this.velocity = initial_velocity;
	}
	
	public Particle(double x, double y, double z){
		this.position = new Position(x, y, z);
		this.mass = ParticleSimulation.MASS;
		this.velocity = new Velocity(0, 0, 0);
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
	
	public DirectionVector calculate_vector(int index){
		return new DirectionVector(
			Particle.pos_list[3 * index]     - Particle.pos_list[3 * this.index], 
			Particle.pos_list[3 * index + 1] - Particle.pos_list[3 * this.index + 1], 
			Particle.pos_list[3 * index + 2] - Particle.pos_list[3 * this.index + 2]);
	}

	private Power force_function(Particle p){
		double power_value;
		DirectionVector v = calculate_vector(p);
		if (v.value != 0.0) {
			power_value = (p.mass * this.mass)/Math.pow(v.value, 2.0);
		} else {
			power_value = 0.0;
		}
		return new Power(power_value*v.x_vector(), 
				  power_value*v.y_vector(),
				  power_value*v.z_vector());
	}

	private Power force_function(int index){
		double power_value;
		DirectionVector v = calculate_vector(index);
		if (v.value != 0.0) {
			power_value = Math.pow(ParticleSimulation.MASS, 2.0)/Math.pow(v.value, 2.0);
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
		return (v * t + a * t * t / 2) * 100;
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

	public Power calculate_power(int own_index){
		Power total_power = new Power(0.0, 0.0, 0.0);
		for (int i = 0; i < Particle.obj_list.length; i++) {
			if (i == own_index){
				continue;
			}
			total_power.add(force_function(i));
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

		Particle.pos_list[3 * this.index] = 
			fix_position(Particle.pos_list[3 * this.index] + 
				move_equation(this.velocity.x, power.x/this.mass, t), limit);
	        Particle.pos_list[3 * this.index + 1] = 
			fix_position(Particle.pos_list[3 * this.index + 1] + 
				move_equation(this.velocity.y, power.y/this.mass, t), limit);
	        Particle.pos_list[3 * this.index + 2] = 
			fix_position(Particle.pos_list[3 * this.index + 2] + 
				move_equation(this.velocity.z, power.z/this.mass, t), limit);
	}

	public static void create_field_list(){
		pos_list = new double[obj_list.length * 3];
		for(int i = 0; i < obj_list.length; i++){
			obj_list[i].index   = i;
			pos_list[3 * i]     = obj_list[i].position.x;
			pos_list[3 * i + 1] = obj_list[i].position.y;
			pos_list[3 * i + 2] = obj_list[i].position.z;
		}
	}

}
