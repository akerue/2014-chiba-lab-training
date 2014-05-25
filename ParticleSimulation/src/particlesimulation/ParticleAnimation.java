package particlesimulation;


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
	double radius = 1.0;
	int TIMES = 500;
	
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
		Particle.obj_list = ParticleSimulation.read_config();
		Sphere[] spheres = new Sphere[Particle.obj_list.length];
		ParallelTransition[] parallelTransitions = new ParallelTransition[TIMES];

		for (int i = 0; i < TIMES; i++){
			parallelTransitions[i] = new ParallelTransition();
		}

		for (int i = 0; i < Particle.obj_list.length; i++){
			spheres[i] = new Sphere(radius);
			spheres[i].setTranslateX(Particle.obj_list[i].position.x);
			spheres[i].setTranslateY(Particle.obj_list[i].position.y);
			spheres[i].setTranslateZ(Particle.obj_list[i].position.z);
			root.getChildren().add(spheres[i]);	
		}
		
		Particle[] last_particles;
		TranslateTransition[] translateTransitions = 
			new TranslateTransition[Particle.obj_list.length];
		for (int i = 0; i < Particle.obj_list.length; i++){
			translateTransitions[i] = new TranslateTransition();
		}
		
		for (int count = 0; count < TIMES; count++){
			last_particles = Particle.obj_list;
			ParticleSimulation.simple_update(ParticleSimulation.STEP);
			for (int i = 0; i < Particle.obj_list.length; i++){
				translateTransitions[i] = new TranslateTransition(
						Duration.millis(
							ParticleSimulation.STEP * 1000),
						spheres[i]);
				translateTransitions[i].setFromX(last_particles[i].position.x);
				translateTransitions[i].setFromY(last_particles[i].position.y);
				translateTransitions[i].setFromZ(last_particles[i].position.z);
				translateTransitions[i].setToX(Particle.obj_list[i].position.x);
				translateTransitions[i].setToY(Particle.obj_list[i].position.y);
				translateTransitions[i].setToZ(Particle.obj_list[i].position.z);
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

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}
