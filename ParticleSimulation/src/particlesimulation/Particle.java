package particlesimulation;

public class Particle {
	public int index;
	public Position position;
	public Velocity velocity;
	public double mass;
	public static int CYCLE = 12;

	// Unused field for extending particle object
	public double density;
	public double volume;
	public double pressure;
	public double temperature;

	public static Particle[] obj_list;

	public static double[] field_list;
	// Repeat position and velocity field
	// [x-1, y-1, z-1, v_x-1, v_y-1, v_z-1, x-2, y-2, z-2, vec_x, vec_y, vec_z, p_x, p_y, p_z...]

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
	
	public static void calculate_vector(int own_index, int partner_index){
		Particle.field_list[CYCLE * own_index + 6] = 
			Particle.field_list[CYCLE * partner_index]     - Particle.field_list[CYCLE * own_index];
		Particle.field_list[CYCLE * own_index + 7] = 
			Particle.field_list[CYCLE * partner_index + 1] - Particle.field_list[CYCLE * own_index + 1];
		Particle.field_list[CYCLE * own_index + 8] = 
			Particle.field_list[CYCLE * partner_index + 2] - Particle.field_list[CYCLE * own_index + 2];
	}

	private static void force_function(int own_index, int partner_index){
		double power_value;
		calculate_vector(own_index, partner_index);
		double vector = Math.sqrt(
				Math.pow(Particle.field_list[CYCLE * own_index + 6], 2.0) +
				Math.pow(Particle.field_list[CYCLE * own_index + 7], 2.0) + 
				Math.pow(Particle.field_list[CYCLE * own_index + 8], 2.0)
		);
		if (vector != 0.0) {
			power_value = Math.pow(ParticleSimulation.MASS, 2.0)/Math.pow(vector, 2.0);
		} else {
			power_value = 0.0;
		}
		Particle.field_list[CYCLE * own_index + 9]  +=
			power_value * Particle.field_list[CYCLE * own_index + 6] / vector;
		Particle.field_list[CYCLE * own_index + 10] +=
			power_value * Particle.field_list[CYCLE * own_index + 7] / vector;
		Particle.field_list[CYCLE * own_index + 11] +=
			power_value * Particle.field_list[CYCLE * own_index + 8] / vector;
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

	public static void calculate_power(int index){
		for (int i = 0; i < Particle.obj_list.length; i++){
			if (i == index){
				continue;
			}
			force_function(index, i);
		}
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

	public static void update_position(double mass, int index, double t){
		// calculate position after passing t seconds

		double limit = ParticleSimulation.WIDTH;

		Particle.field_list[CYCLE * index] = 
			fix_position(Particle.field_list[CYCLE * index] + 
				move_equation(field_list[CYCLE * index + 3], 
					Particle.field_list[CYCLE * index + 9]/mass, t), limit);
	        Particle.field_list[CYCLE * index + 1] = 
			fix_position(Particle.field_list[CYCLE * index + 1] + 
				move_equation(field_list[CYCLE * index + 4], 
					Particle.field_list[CYCLE * index + 10]/mass, t), limit);
	        Particle.field_list[CYCLE * index + 2] = 
			fix_position(Particle.field_list[CYCLE * index + 2] + 
				move_equation(field_list[CYCLE * index + 5], 
					Particle.field_list[CYCLE * index + 11]/mass, t), limit);
	}

	public static void create_field_list(){
		field_list = new double[obj_list.length * CYCLE];
		for(int i = 0; i < obj_list.length; i++){
			obj_list[i].index   = i;
			field_list[CYCLE * i]     = obj_list[i].position.x;
			field_list[CYCLE * i + 1] = obj_list[i].position.y;
			field_list[CYCLE * i + 2] = obj_list[i].position.z;
			field_list[CYCLE * i + 3] = obj_list[i].velocity.x;
			field_list[CYCLE * i + 4] = obj_list[i].velocity.y;
			field_list[CYCLE * i + 5] = obj_list[i].velocity.z;
			field_list[CYCLE * i + 6] = 0.0;
			field_list[CYCLE * i + 7] = 0.0;
			field_list[CYCLE * i + 8] = 0.0;
			field_list[CYCLE * i + 9] = 0.0;
			field_list[CYCLE * i + 10] = 0.0;
			field_list[CYCLE * i + 11] = 0.0;
			
		}
	}

}
