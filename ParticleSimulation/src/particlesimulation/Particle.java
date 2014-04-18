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
	public double x;
	public double y;
	public double z;

	public Particle(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double calculate_distance(Particle p){
		double delta_x = x - p.x;
		double delta_y = x - p.y;
		double delta_z = x - p.z;

		return Math.sqrt(Math.pow(delta_x, 2.0) + 
				 Math.pow(delta_y, 2.0) + 
				 Math.pow(delta_z, 2.0));
	}
	
	public double calculate_power(double distance){
		double power;
		// use function which user difines
		power = 1/(distance * distance);
		return power;
	}
}
