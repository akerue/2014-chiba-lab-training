/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulation;

import org.openjdk.jol.info.ClassLayout;

/**
 *
 * @author Robbykunsan
 */
public class DumpClassLayout {

	public static void main(String[] args) {
		System.out.println(ClassLayout.parseClass(Particle.class).toPrintable());
		System.out.println(ClassLayout.parseClass(Position.class).toPrintable());
		System.out.println(ClassLayout.parseClass(Velocity.class).toPrintable());
	}
	
}
