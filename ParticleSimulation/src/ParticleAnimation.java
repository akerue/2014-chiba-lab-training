/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Vector;

// imported modules of javafx
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.PerspectiveCamera;
import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.shape.Path;

// imported modules of particlesimulation
import particlesimulation.*;

/**
 *
 * @author Robbykunsan
 */
public class ParticleAnimation extends Application {
	double radius = 1.0;
	int TIMES = 500;
	
	@Override
	public void start(Stage primaryStage) {
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
		Particle[] particles;
		particles = ParticleSimulation.init();

		Sphere[] spheres = new Sphere[particles.length];
		ParallelTransition[] parallelTransitions = new ParallelTransition[TIMES];

		for (int i = 0; i < TIMES; i++){
			parallelTransitions[i] = new ParallelTransition();
		}

		for (int i = 0; i < particles.length; i++){
			spheres[i] = new Sphere(radius);
			spheres[i].setTranslateX(particles[i].position.x);
			spheres[i].setTranslateY(particles[i].position.y);
			spheres[i].setTranslateZ(particles[i].position.z);
			root.getChildren().add(spheres[i]);	
		}
		
		Particle[] next_particles;
		TranslateTransition[] translateTransitions = 
			new TranslateTransition[particles.length];
		for (int i = 0; i < particles.length; i++){
			translateTransitions[i] = new TranslateTransition();
		}
		
		for (int count = 0; count < TIMES; count++){
			next_particles = 
				ParticleSimulation.simple_update(particles, ParticleSimulation.STEP);
			for (int i = 0; i < particles.length; i++){
				translateTransitions[i] = new TranslateTransition(
						Duration.millis(
							ParticleSimulation.STEP * 1000),
						spheres[i]);
				translateTransitions[i].setFromX(particles[i].position.x);
				translateTransitions[i].setToX(next_particles[i].position.x);
				translateTransitions[i].setFromY(particles[i].position.y);
				translateTransitions[i].setToY(next_particles[i].position.y);
				translateTransitions[i].setFromZ(particles[i].position.z);
				translateTransitions[i].setToZ(next_particles[i].position.z);
				parallelTransitions[count].getChildren().add(translateTransitions[i]);
			}
			particles = next_particles;
		}
		SequentialTransition sequentialTransition = new SequentialTransition();
		sequentialTransition.getChildren().addAll(parallelTransitions);
		sequentialTransition.play();

		primaryStage.setTitle("Particle Animation");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
