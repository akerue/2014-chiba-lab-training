package particlesimulation;
import java.io.*;
import java.util.*;

public class ParticleSimulation {
	public static final double WIDTH = 99.0;
	public static final double MASS = 10.0;
	public static final double STEP = 0.1;

	public static void main(String[] args) throws IOException, FileNotFoundException {
		Particle.obj_list = read_config();
		// for JIT optimization
		for (int count = 0; count < 50; count++){
			ParticleSimulation.update_all_position(ParticleSimulation.STEP);
		}
		TimeProfile tp = new TimeProfile();
		tp.start();
		Particle.create_field_list();
		for (int count = 0; count < 500; count++){
			ParticleSimulation.update_all_position(ParticleSimulation.STEP);
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

	public static void update_all_position(double step){
		Power power;

		for (int i = 0; i < Particle.obj_list.length; i++){
			power = Particle.calculate_power(i);
			Particle.update_position(power, MASS, i, step);
		}
	}

}
