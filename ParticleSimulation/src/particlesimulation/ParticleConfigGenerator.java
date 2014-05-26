package particlesimulation;
import java.util.Random;
import java.io.*;

public class ParticleConfigGenerator {
	static Random random = new Random();
	public static final int NUM = 1000;
	public static final int WIDTH = 99;
	public static final int MAX_VELOCITY = 5;

	public static void main(String[] args) throws IOException {
		double x, y, z;
		double v_x, v_y, v_z;
		String[] pos;
		String line;
		
		String filepath = new String("../../config/conf.txt");
		File conf_file = new File(filepath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(conf_file));

		try {
			for (int i = 0; i<NUM ; i++) {
				x = random.nextInt(WIDTH) + random.nextDouble();
				y = random.nextInt(WIDTH) + random.nextDouble();
				z = random.nextInt(WIDTH) + random.nextDouble();
				v_x = random.nextInt(MAX_VELOCITY * 2) + random.nextDouble() - MAX_VELOCITY;
				v_y = random.nextInt(MAX_VELOCITY * 2) + random.nextDouble() - MAX_VELOCITY;
				v_z = random.nextInt(MAX_VELOCITY * 2) + random.nextDouble() - MAX_VELOCITY;
				
				pos = new String[]{Double.toString(x), 
						   Double.toString(y), 
						   Double.toString(z),
						   Double.toString(v_x), 
						   Double.toString(v_y), 
						   Double.toString(v_z)};
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < pos.length - 1; j++){
					builder.append(pos[j]).append(" ");
				}
				line = builder.append(pos[pos.length-1]).toString();
				bw.write(line);
				bw.newLine();
			}
		}finally{
			bw.close();
		}
	}
	
}
