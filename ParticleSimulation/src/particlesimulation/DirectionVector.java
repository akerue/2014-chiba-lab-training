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
	double value;

	public DirectionVector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = Math.sqrt(x * x + y * y + z * z);
	}
}
