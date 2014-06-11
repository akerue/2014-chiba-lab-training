package particlesimulation;
import java.io.*;
import java.util.*;

public class ParticleSimulation {
	public static final float WIDTH = 0.99e2f;
	public static final float MASS = 0.1e2f;
	public static final float STEP = 0.1e0f;
	public static int NUM = ParticleConfigGenerator.NUM;

	public static void main(String[] args) throws IOException, FileNotFoundException {
		read_config();
		// for JIT optimization
		for (int count = 0; count < 50; count++){
			ParticleSimulation.update_all_position(ParticleSimulation.STEP);
		}
		TimeProfile tp = new TimeProfile();
		tp.start();
		for (int count = 0; count < 500; count++){
			ParticleSimulation.update_all_position(ParticleSimulation.STEP);
		}
		tp.print_micro_time();
	}

	public static void read_config() throws IOException, FileNotFoundException{
		File conf_file = new File("../config/conf.txt");
		BufferedReader br = new BufferedReader(new FileReader(conf_file));
		String line;
		String[] parsed_line;
		int i = 0;
		while((line = br.readLine()) != null){
			if (line.length() <= 1) {
				// final line
				break;
			}
			parsed_line = line.replaceAll("¥n", "").split(" ");
			Particle.field_list[i * Particle.CYCLE + Particle.X_INDEX] = 
				Float.valueOf(parsed_line[0]);
			Particle.field_list[i * Particle.CYCLE + Particle.Y_INDEX] = 
				Float.valueOf(parsed_line[1]);
			Particle.field_list[i * Particle.CYCLE + Particle.Z_INDEX] = 
				Float.valueOf(parsed_line[2]);
			Particle.field_list[i * Particle.CYCLE + Particle.V_X_INDEX] = 
				Float.valueOf(parsed_line[3]);
			Particle.field_list[i * Particle.CYCLE + Particle.V_Y_INDEX] = 
				Float.valueOf(parsed_line[4]);
			Particle.field_list[i * Particle.CYCLE + Particle.V_Z_INDEX] = 
				Float.valueOf(parsed_line[5]);
			i++;
		}
		br.close();
	}

	public static void update_all_position(float step){
		for (int i = 0; i < NUM; i++){
			Particle.calculate_power(i);
			Particle.update_position(MASS, i, step);
		}
	}

}
