package particlesimulation;
import java.io.*;
import java.util.*;

public class ParticleSimulation {
	public static final float WIDTH = 0.99e2f;
	public static final float MASS = 0.1e2f;
	public static final float STEP = 0.1e0f;

	public static void main(String[] args) throws IOException, FileNotFoundException {
		Particle.obj_list = read_config();
		for (int count = 0; count < 10; count++){
			update_all_position(ParticleSimulation.STEP);
		}
		TimeProfile tp = new TimeProfile();
		tp.start();
		for (int count = 0; count < 100; count++){
			update_all_position(ParticleSimulation.STEP);
		}
		tp.print_micro_time();
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
						Float.valueOf(parsed_line[0]), 
						Float.valueOf(parsed_line[1]), 
						Float.valueOf(parsed_line[2]),
						MASS,
						Float.valueOf(parsed_line[3]), 
						Float.valueOf(parsed_line[4]), 
						Float.valueOf(parsed_line[5]))
						);
		}
		br.close();
		return particles.toArray(new Particle[particles.size()]);
	}

	public static void update_all_position(float step){
		Power power;

		for (int i = 0; i < Particle.obj_list.length; i++){
			power = Particle.obj_list[i].calculate_power();
			Particle.obj_list[i].update_position(power, step);
		}
	}

}
