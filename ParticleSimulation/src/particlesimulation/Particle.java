package particlesimulation;

public class Particle {
	private int index;
	private Position position;
	private Velocity velocity;
	private double mass;

	// Unused field for extending particle object
	final public double density = 10.0;
	final public double volume = 10.0;
	final public double pressure = 10.0;
	final public double temperature = 10.0;
	final public Spin spin = new Spin(0.0, 0.0, 0.0);
	final public Strength strength = new Strength(10.0, 10.0, 10.0);

	final public static int NUM = ParticleConfigGenerator.NUM;

	final public static int CYCLE = 6;
	final public static int X_INDEX = 0;
	final public static int Y_INDEX = 1;
	final public static int Z_INDEX = 2;
	final public static int V_X_INDEX = 3;
	final public static int V_Y_INDEX = 4;
	final public static int V_Z_INDEX = 5;
	public static double[] field_list = new double[NUM * CYCLE];
	// Repeat position and velocity field
	// [x-1, y-1, z-1, v_x-1, v_y-1, v_z-1...]

	private static double[] temp_list = new double[6];
	// Save temp data
	// [vec_x, vec_y, vec_z, pow_x, pow_y, pow_z]
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
	
	public static void calculate_vector(int own_index, int partner_index){
		int own_offset = CYCLE * own_index;
		int partner_offset = CYCLE * partner_index;
		Particle.temp_list[VEC_X_INDEX] = 
			Particle.field_list[partner_offset + X_INDEX] - Particle.field_list[own_offset + X_INDEX];
		Particle.temp_list[VEC_Y_INDEX] = 
			Particle.field_list[partner_offset + Y_INDEX] - Particle.field_list[own_offset + Y_INDEX];
		Particle.temp_list[VEC_Z_INDEX] = 
			Particle.field_list[partner_offset + Z_INDEX] - Particle.field_list[own_offset + Z_INDEX];
	}

	private static void force_function(int own_index, int partner_index){
		double power_value;
		calculate_vector(own_index, partner_index);
		double vec_x = Particle.temp_list[VEC_X_INDEX];
		double vec_y = Particle.temp_list[VEC_Y_INDEX];
		double vec_z = Particle.temp_list[VEC_Z_INDEX];

		double vector = (double) Math.sqrt(vec_x * vec_x + vec_y * vec_y + vec_z * vec_z);
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
			power_value = 0.0e0f;
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
		for (int i = 0; i < NUM; i++){
			if (i == index){
				continue;
			}
			force_function(index, i);
		}
	}

	public static void update_position(double mass, int index, double t){
		// calculate position after passing t seconds

		double limit = ParticleSimulation.WIDTH;
		int offset = CYCLE * index;

		Particle.field_list[offset + X_INDEX] = 
			fix_position(Particle.field_list[offset + X_INDEX] + 
				move_equation(field_list[offset + V_X_INDEX], 
					Particle.temp_list[POW_X_INDEX]/mass, t), limit);
	        Particle.field_list[offset + Y_INDEX] = 
			fix_position(Particle.field_list[offset + Y_INDEX] + 
				move_equation(field_list[offset + V_Y_INDEX], 
					Particle.temp_list[POW_Y_INDEX]/mass, t), limit);
	        Particle.field_list[offset + Z_INDEX] = 
			fix_position(Particle.field_list[offset + Z_INDEX] + 
				move_equation(field_list[offset + V_Z_INDEX], 
					Particle.temp_list[POW_Z_INDEX]/mass, t), limit);
	}
}
