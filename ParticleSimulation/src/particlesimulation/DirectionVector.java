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
	float x, y, z;
	private float value;

	public DirectionVector(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float x_vector(){
		return this.x/this.value;
	}

	public float y_vector(){
		return this.y/this.value;
	}
	
	public float z_vector(){
		return this.z/this.value;
	}

	public float get_value(){
		return this.value;
	}
}
