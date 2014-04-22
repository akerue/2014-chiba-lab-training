/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulation;

/**
 *
 * @author Robbykunsan
 */
public class Particle {
	public Position position;
	public Velocity velocity;
	public double mass;

	public Particle(double x, double y, double z, double mass, Velocity initial_velocity){
		this.position = new Position(x, y, z);
		this.mass = mass;
		this.velocity = initial_velocity;
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

	private double attractive_force_function(double own_mass, double partner_mass, double distance){
		// this function should be redefined
		return (own_mass * partner_mass)/(distance * distance);
	}
	
	public Power calculate_power(Particle p){
		double power_x, power_y, power_z;
		DirectionVector v = calculate_vector(p);
		// use function which user difines
		power_x = attractive_force_function(this.mass, p.mass, v.x);
		power_y = attractive_force_function(this.mass, p.mass, v.y);
		power_z = attractive_force_function(this.mass, p.mass, v.z);
		return new Power(power_x, power_y, power_z);
	}

}
