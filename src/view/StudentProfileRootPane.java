package view;

import javafx.scene.layout.VBox;

/**
 * Root View class of the MVC for the StudentProfile application
 */
public class StudentProfileRootPane extends VBox {

	private StudentProfileMenuBar menuBar;
	private StudentProfileTabPane tabPane;

	public StudentProfileRootPane() {
		menuBar = new StudentProfileMenuBar();
		tabPane = new StudentProfileTabPane();
		this.getChildren().addAll(menuBar, tabPane);
	}

	/**
	 * @return The StudentProfileMenuBar
	 */
	public StudentProfileMenuBar getMenuBar() {
		return menuBar;
	}

	/**
	 * @return The StudentProfileTabPane
	 */
	public StudentProfileTabPane getTabPane() {
		return tabPane;
	}
}