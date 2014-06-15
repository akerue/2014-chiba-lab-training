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
public class Power extends DirectionVector{
	public Power(double x, double y, double z){
		super(x, y, z);
	}

	public void add(Power p){
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
}
