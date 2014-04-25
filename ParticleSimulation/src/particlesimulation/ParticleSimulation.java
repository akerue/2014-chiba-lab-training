/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particlesimulation;
import java.io.*;
import java.util.*;

/**
 *
 * @author Robbykunsan
 */
public class ParticleSimulation {
	public static final double WIDTH = 100.0;
	public static final double MASS = 10.0;
	public static final double STEP = 0.01;

	public static void main(String[] args) {
		Particle[] particles = init();
	
		particles = simple_update(particles, STEP);
	}

	public static Particle[] init(){
		ArrayList<Particle> particles = new ArrayList<Particle>();
		try {
			File conf_file = new File("/Users/Robbykunsan/Workspace/mlab/training/config/conf.txt");
			BufferedReader br = new BufferedReader(new FileReader(conf_file));

			String line;
			String[] parsed_line;
			while((line = br.readLine()) != null){
				if (line.length() <= 1) {
					// final line
					break;
				}
				parsed_line = line.replaceAll("¥n", "").split(" ");
				particles.add(new Particle(
							Double.valueOf(parsed_line[0]), 
							Double.valueOf(parsed_line[1]), 
							Double.valueOf(parsed_line[2]))
							);
			}
		} catch(FileNotFoundException e){
			System.out.println(e);
			System.exit(1);
		}catch(IOException e){
			System.out.println(e);
			System.exit(1);
		}
		return particles.toArray(new Particle[particles.size()]);
	}

	public static Particle[] simple_update(Particle[] particles, double step){
		Power power;

		for (int i = 0; i < particles.length; i++){
			power = particles[i].calculate_power(particles, i);
			particles[i].update_position(power, step);
		}
		return particles;
	}
}
