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
public class DirectionVector {
	double x, y, z;
	private double value;

	public DirectionVector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = (double) Math.sqrt(x * x + y * y + z * z);
	}

	public double x_vector(){
		return this.x/this.value;
	}

	public double y_vector(){
		return this.y/this.value;
	}
	
	public double z_vector(){
		return this.z/this.value;
	}

	public double get_value(){
		return this.value;
	}
}
