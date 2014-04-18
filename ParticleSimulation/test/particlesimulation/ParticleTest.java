/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robbykunsan
 */
public class ParticleTest {
	
	public ParticleTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of calculate_distance method, of class Particle.
	 */
	@Test
	public void testCalculate_distance() {
		System.out.println("calculate_distance");
		Particle p1 = new Particle(1.0, 1.0, 1.0);
		Particle p2 = new Particle(0.0, 0.0, 0.0);
		double expResult = Math.sqrt(3.0);
		double result = p1.calculate_distance(p2);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of calculate_power method, of class Particle.
	 */
	@Test
	public void testCalculate_power() {
		System.out.println("calculate_power");
		double distance = 2.0;
		Particle p = new Particle(0.0, 0.0, 0.0);
		double expResult = 0.25;
		double result = p.calculate_power(distance);
		assertEquals(expResult, result, 0.0);
	}
	
}
