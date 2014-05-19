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
	public static final double WIDTH = 99.0;
	public static final double MASS = 10.0;
	public static final double STEP = 0.1;
	public static final int LEVEL = 3;
	public static final double SIDE = WIDTH/LEVEL;

	public static ArrayList<Particle>[][][] registered_particles;

	public static void main(String[] args) {
		Particle.obj_list = init();
		
	
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

	public static void simple_update(double step){
		Power power;

		for (int i = 0; i < Particle.obj_list.length; i++){
			power = Particle.obj_list[i].calculate_power(Particle.obj_list, i);
			Particle.obj_list[i].update_position(power, step);
		}
	}

	public static void update_grouping(Particle particle){
		particle.update_registered_space();
		registered_particles[particle.x_index][particle.y_index][particle.z_index]
			.add(particle);
	}

	public static void array_field_update(double step){
		
	}
}
