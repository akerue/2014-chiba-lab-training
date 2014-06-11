package particlesimulation;

public class Particle {

	final public double density = 10.0;
	private Position position;
	final public double volume = 10.0;
	final public Spin spin = new Spin(0.0, 0.0, 0.0);
	final public double pressure = 10.0;
	private Velocity velocity;
	final public double temperature = 10.0;
	final public Strength strength = new Strength(10.0, 10.0, 10.0);

	private float mass;

	public static Particle[] obj_list;

	public Particle(float x, float y, float z, float mass, float v_x, float v_y, float v_z){
		this.position = new Position(x, y, z);
		this.mass = mass;
		this.velocity = new Velocity(v_x, v_y, v_z);
	}

	public float calculate_distance(Particle p){
		float delta_x = position.x - p.position.x;
		float delta_y = position.y - p.position.y;
		float delta_z = position.z - p.position.z;

		return (float) Math.sqrt(Math.pow(delta_x, 2.0) + 
				 Math.pow(delta_y, 2.0) + 
				 Math.pow(delta_z, 2.0));
	}

	public DirectionVector calculate_vector(Particle p){
		return new DirectionVector(p.position.x - this.position.x, 
					   p.position.y - this.position.y, 
					   p.position.z - this.position.z);
	}
	
	private Power force_function(Particle p){
		float power_value;
		DirectionVector v = calculate_vector(p);
		if (v.get_value() != 0.0) {
			power_value = - (p.mass * this.mass)/(v.get_value() * v.get_value());
			return new Power(power_value*v.x_vector(), 
					  power_value*v.y_vector(),
					  power_value*v.z_vector());
		} else {
			return new Power(0, 0, 0);
		}
	}

	private float kinetic_equation(float v, float a, float t) {
		return v + a * t;
	}
	
	public Velocity calculate_velocity(Power power, float t){
		// calculate v after passing t seconds
		
		return new Velocity(kinetic_equation(this.velocity.x, power.x/this.mass, t), 
				    kinetic_equation(this.velocity.y, power.y/this.mass, t),
				    kinetic_equation(this.velocity.z, power.z/this.mass, t));
	}

	private float move_equation(float v, float a, float t){
		return (v * t + a * t * t / 2);
	}

	private float fix_position(float pos, float limit){
		if (pos > limit){
			return fix_position(pos % limit, limit);
		} else if (pos < 0.0) {
			return fix_position(pos % limit + limit, limit);
		} else {
			return pos;
		}
	}

	public Power calculate_power(){
		Power total_power = new Power(0, 0, 0);
		for (int i = 0; i < Particle.obj_list.length; i++) {
			if (Particle.obj_list[i] == this){
				continue;
			}
			total_power.add(force_function(Particle.obj_list[i]));
		}
		return total_power;
	}

	public Position calculate_position(Power power, float t){
		// calculate position after passing t seconds

		float next_x, next_y, next_z;
		float limit = ParticleSimulation.WIDTH;

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

	public void update_position(Power power, float t){
		// calculate position after passing t seconds

		float limit = ParticleSimulation.WIDTH;
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

	public float get_x() {
		return this.position.x;
	}

	public float get_y() {
		return this.position.y;
	}

	public float get_z() {
		return this.position.z;
	}

}
