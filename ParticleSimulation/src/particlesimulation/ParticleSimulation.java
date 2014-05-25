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

	public static void main(String[] args) throws IOException, FileNotFoundException {
		Particle.obj_list = read_config();
		Particle.create_field_list();
		for (int count = 0; count < 10; count++){
			ParticleSimulation.simple_update(ParticleSimulation.STEP);
			for (int i = 0; i < Particle.obj_list.length; i++){
				System.out.println(Particle.pos_list[3 * i]);
				System.out.println(Particle.pos_list[3 * i + 1]);
				System.out.println(Particle.pos_list[3 * i + 2]);
			}
		}
	}

	public static Particle[] read_config() throws IOException, FileNotFoundException{
		ArrayList<Particle> particles = new ArrayList<>();
		File conf_file = new File("../config/conf.txt");
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
						Double.valueOf(parsed_line[2]),
						MASS,
						Double.valueOf(parsed_line[3]), 
						Double.valueOf(parsed_line[4]), 
						Double.valueOf(parsed_line[5]))
						);
		}
		br.close();
		return particles.toArray(new Particle[particles.size()]);
	}

	public static void simple_update(double step){
		Power power;

		for (int i = 0; i < Particle.obj_list.length; i++){
			power = Particle.obj_list[i].calculate_power();
			Particle.obj_list[i].update_position(power, step);
		}
	}

}
