package main;

import controller.StudentProfileController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.StudentProfile;
import view.StudentProfileRootPane;

public class ApplicationLoader extends Application {

	private StudentProfileRootPane view;

	/**
	 * Initialises the MVC objects (Model, View and Controller)
	 */
	public void init() {
		view = new StudentProfileRootPane();
		StudentProfile model = new StudentProfile();
		new StudentProfileController(view, model);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Student Profile Mark Submission Tool");
		Scene scene = new Scene(view, 800, 600);
		scene.getStylesheets().add("view/stylesheet.css");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
