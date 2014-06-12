package particlesimulation;

public class Particle {
	public int index;
	public Position position;
	public Velocity velocity;
	public double mass;
	public static int CYCLE = 6;

	// Unused field for extending particle object
	public double density;
	public double volume;
	public double pressure;
	public double temperature;

	final public static int NUM = ParticleConfigGenerator.NUM;

	public static Particle[] obj_list;

	final public static int X_INDEX = 0;
        final public static int Y_INDEX = 1;
        final public static int Z_INDEX = 2;
        final public static int V_X_INDEX = 3;
        final public static int V_Y_INDEX = 4;
        final public static int V_Z_INDEX = 5;
        public static double[] field_list = new double[NUM * CYCLE];
	// Repeat position and velocity field
	// [x-1, y-1, z-1, v_x-1, v_y-1, v_z-1]

	public static double temp_list[] = new double[6];
	final public static int VEC_X_INDEX = 0;
        final public static int VEC_Y_INDEX = 1;
        final public static int VEC_Z_INDEX = 2;
        final public static int POW_X_INDEX = 3;
        final public static int POW_Y_INDEX = 4;
        final public static int POW_Z_INDEX = 5;
	

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
		Particle.temp_list[VEC_X_INDEX] = 
			Particle.field_list[CYCLE * partner_index + X_INDEX] - 
			Particle.field_list[CYCLE * own_index + X_INDEX];
		Particle.temp_list[VEC_Y_INDEX] =
			Particle.field_list[CYCLE * partner_index + Y_INDEX] - 
			Particle.field_list[CYCLE * own_index + Y_INDEX];
		Particle.temp_list[VEC_Z_INDEX] = 
			Particle.field_list[CYCLE * partner_index + Z_INDEX] - 
			Particle.field_list[CYCLE * own_index + Z_INDEX];
	}

	private static void force_function(int own_index, int partner_index){
		double power_value;
		calculate_vector(own_index, partner_index);

		double vector = Math.sqrt( 
			Particle.temp_list[VEC_X_INDEX] *
				Particle.temp_list[VEC_X_INDEX] +
			Particle.temp_list[VEC_Y_INDEX] * 
				Particle.temp_list[VEC_Y_INDEX] + 
			Particle.temp_list[VEC_Z_INDEX] *
				Particle.temp_list[VEC_Z_INDEX]
		);
		if (vector != 0.0) {
			power_value = 
				-(ParticleSimulation.MASS * ParticleSimulation.MASS) / (vector * vector);
			Particle.temp_list[POW_X_INDEX]  +=
				power_value * Particle.temp_list[VEC_X_INDEX] / vector;
			Particle.temp_list[POW_Y_INDEX] +=
				power_value * Particle.temp_list[VEC_Y_INDEX] / vector;
			Particle.temp_list[POW_Z_INDEX] +=
				power_value * Particle.temp_list[VEC_Z_INDEX] / vector;
		} else {
			power_value = 0.0;
		}
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

		Particle.field_list[CYCLE * index + X_INDEX] = 
			fix_position(Particle.field_list[CYCLE * index + X_INDEX] + 
				move_equation(field_list[CYCLE * index + V_X_INDEX], 
					Particle.temp_list[POW_X_INDEX]/mass, t), limit);
	        Particle.field_list[CYCLE * index + Y_INDEX] = 
			fix_position(Particle.field_list[CYCLE * index + Y_INDEX] + 
				move_equation(field_list[CYCLE * index + V_Y_INDEX], 
					Particle.temp_list[POW_Y_INDEX]/mass, t), limit);
	        Particle.field_list[CYCLE * index + Y_INDEX] = 
			fix_position(Particle.field_list[CYCLE * index + Y_INDEX] + 
				move_equation(field_list[CYCLE * index + V_Z_INDEX], 
					Particle.temp_list[POW_Z_INDEX]/mass, t), limit);
	}

	public static void create_field_list(){
		field_list = new double[obj_list.length * CYCLE];
		for(int i = 0; i < obj_list.length; i++){
			obj_list[i].index   = i;
			field_list[CYCLE * i + X_INDEX] = obj_list[i].position.x;
			field_list[CYCLE * i + Y_INDEX] = obj_list[i].position.y;
			field_list[CYCLE * i + Z_INDEX] = obj_list[i].position.z;
			field_list[CYCLE * i + V_X_INDEX] = obj_list[i].velocity.x;
			field_list[CYCLE * i + V_Y_INDEX] = obj_list[i].velocity.y;
			field_list[CYCLE * i + V_Z_INDEX] = obj_list[i].velocity.z;
		}
	}

}
