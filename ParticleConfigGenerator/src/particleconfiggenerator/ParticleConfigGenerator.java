/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package particleconfiggenerator;
import java.util.Random;
import java.io.*;

/**
 *
 * @author Robbykunsan
 */
public class ParticleConfigGenerator {
	static Random random = new Random();
	public static final int NUM = 100;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		double x, y, z;
		String[] pos;
		String line;
		
		String filepath = new String("/Users/Robbykunsan/Workspace/mlab/training/config/conf.txt");

		try {
			File conf_file = new File(filepath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(conf_file));
			
			for (int i = 0; i<NUM ; i++) {
				x = random.nextInt(100) + random.nextDouble();
				y = random.nextInt(100) + random.nextDouble();
				z = random.nextInt(100) + random.nextDouble();
				pos = new String[]{Double.toString(x), Double.toString(y), Double.toString(z)};
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < pos.length - 1; j++){
					builder.append(pos[j]).append(" ");
				}
				line = builder.append(pos[pos.length-1]).toString();
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
	
}
