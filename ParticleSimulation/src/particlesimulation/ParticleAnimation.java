package particlesimulation;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// imported modules of javafx
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.PerspectiveCamera;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;

public class ParticleAnimation extends Application {
	final double radius = 1.0;
	final int TIMES = 500;
	final int NUM = ParticleConfigGenerator.NUM;
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		Group root = new Group();
		Scene scene = new Scene(root, 500, 500);
		scene.setFill(Color.WHITE);
		PerspectiveCamera camera = new PerspectiveCamera(true);
		
		camera.setTranslateZ(-50.0);
		camera.setTranslateX(50.0);
		camera.setTranslateY(50.0);
		camera.setFieldOfView(70.0);
		scene.setCamera(camera);

		// generate particles
		ParticleSimulation.read_config();
		Sphere[] spheres = new Sphere[NUM];
		ParallelTransition[] parallelTransitions = new ParallelTransition[TIMES];

		for (int i = 0; i < TIMES; i++){
			parallelTransitions[i] = new ParallelTransition();
		}

		for (int i = 0; i < NUM; i++){
			spheres[i] = new Sphere(radius);
			spheres[i].setTranslateX(Particle.field_list[3 * i]);
			spheres[i].setTranslateY(Particle.field_list[3 * i + 1]);
			spheres[i].setTranslateZ(Particle.field_list[3 * i + 2]);
			root.getChildren().add(spheres[i]);	
		}
		
		double[] last_particles;
		TranslateTransition[] translateTransitions = 
			new TranslateTransition[NUM];
		for (int i = 0; i < NUM; i++){
			translateTransitions[i] = new TranslateTransition();
		}
		
		for (int count = 0; count < TIMES; count++){
			last_particles = Particle.field_list;
			ParticleSimulation.update_all_position(ParticleSimulation.STEP);
			for (int i = 0; i < NUM; i++){
				translateTransitions[i] = new TranslateTransition(
						Duration.millis(
							ParticleSimulation.STEP * 1000),
						spheres[i]);
				translateTransitions[i].setFromX(last_particles[6 * i]);
				translateTransitions[i].setFromY(last_particles[6 * i + 1]);
				translateTransitions[i].setFromZ(last_particles[6 * i + 2]);
				translateTransitions[i].setToX(Particle.field_list[6 * i]);
				translateTransitions[i].setToY(Particle.field_list[6 * i + 1]);
				translateTransitions[i].setToZ(Particle.field_list[6 * i + 2]);
				parallelTransitions[count].getChildren().add(translateTransitions[i]);
			}
		}
		SequentialTransition sequentialTransition = new SequentialTransition();
		sequentialTransition.getChildren().addAll(parallelTransitions);
		sequentialTransition.play();

		primaryStage.setTitle("Particle Animation");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
