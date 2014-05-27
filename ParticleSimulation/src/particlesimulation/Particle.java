package particlesimulation;

public class Particle {
	public int index;
	public Position position;
	public Velocity velocity;
	public double mass;

	// Unused field for extending particle object
	public double density;
	public double volume;
	public double pressure;
	public double temperature;

	public static Particle[] obj_list;

	public static double[] pos_list;
	// Repeat x, y and z field
	// [x-1, y-1, z-1, x-2, y-2, z-2...]

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
	
	public static DirectionVector calculate_vector(int own_index, int partner_index){
		return new DirectionVector(
			Particle.pos_list[3 * partner_index]     - Particle.pos_list[3 * own_index], 
			Particle.pos_list[3 * partner_index + 1] - Particle.pos_list[3 * own_index + 1], 
			Particle.pos_list[3 * partner_index + 2] - Particle.pos_list[3 * own_index + 2]);
	}

	private static Power force_function(int own_index, int partner_index){
		double power_value;
		DirectionVector v = calculate_vector(own_index, partner_index);
		if (v.get_value() != 0.0) {
			power_value = Math.pow(ParticleSimulation.MASS, 2.0)/Math.pow(v.get_value(), 2.0);
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

	private static double move_equation(double v, double a, double t){
		return (v * t + a * t * t / 2);
	}

	private static double fix_position(double pos, double limit){
		if (pos > limit){
			return fix_position(pos % limit, limit);
		} else if (pos < 0.0) {
			return fix_position(pos % limit + limit, limit);
		} else {
			return pos;
		}
	}

	public static Power calculate_power(int index){
		Power total_power = new Power(0.0, 0.0, 0.0);
		for (int i = 0; i < Particle.obj_list.length; i++){
			if (i == index){
				continue;
			}
			total_power.add(force_function(index, i));
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

	public static void update_position(Power power, Velocity velocity, double mass, int index, double t){
		// calculate position after passing t seconds

		double limit = ParticleSimulation.WIDTH;

		Particle.pos_list[3 * index] = 
			fix_position(Particle.pos_list[3 * index] + 
				move_equation(velocity.x, power.x/mass, t), limit);
	        Particle.pos_list[3 * index + 1] = 
			fix_position(Particle.pos_list[3 * index + 1] + 
				move_equation(velocity.y, power.y/mass, t), limit);
	        Particle.pos_list[3 * index + 2] = 
			fix_position(Particle.pos_list[3 * index + 2] + 
				move_equation(velocity.z, power.z/mass, t), limit);
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
