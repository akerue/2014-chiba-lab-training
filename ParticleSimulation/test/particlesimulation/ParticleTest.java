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
	private Velocity v;
	private Particle p1, p2;
	
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
		v = new Velocity(1.0, 1.0, 1.0);
		p1 = new Particle(1.0, 1.0, 1.0, 1.0, v);
		p2 = new Particle(0.0, 0.0, 0.0, 1.0, v);
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
		Power result = p1.calculate_power(p2);
		assertEquals(result.x, 1.0, 0.0);
		assertEquals(result.y, 1.0, 0.0);
		assertEquals(result.z, 1.0, 0.0);
	}
	
}
