package view;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import view.tab.CreateProfileContent;
import view.tab.InputMarksContent;
import view.tab.OverviewResultsContent;

public class StudentProfileTabPane extends TabPane {

	private CreateProfileContent studentProfileContent;
	private InputMarksContent inputMarksContent;
	private OverviewResultsContent overviewResultsContent;

	public StudentProfileTabPane() {
		//create the content containers for all 3 tabs
		studentProfileContent = new CreateProfileContent();
		inputMarksContent = new InputMarksContent();
		overviewResultsContent = new OverviewResultsContent();
		addTab("Create Profile", studentProfileContent);
		addTab("Input Marks", inputMarksContent);
		addTab("Overview Results", overviewResultsContent);
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}

	/**
	 * Creates & adds a tab to this TabPane
	 * @param title The title of the Tab
	 * @param content a Pane containing the content of the Tab
	 */
	private void addTab(String title, Pane content) { //convenience method to add a tab
		Tab tab = new Tab(title);
		content.setPadding(new Insets(20));
		tab.setContent(content);
		super.getTabs().add(tab);
	}

	/**
	 * @return The CreateProfileContent object
	 */
	public CreateProfileContent getStudentProfileContent() {
		return studentProfileContent;
	}

	/**
	 * @return The InputMarksContent object
	 */
	public InputMarksContent getInputMarksContent() {
		return inputMarksContent;
	}

	/**
	 * @return The OverviewResultsContent object
	 */
	public OverviewResultsContent getOverviewResultsContent() {
		return overviewResultsContent;
	}
	
	/**
	 * Switches tabs
	 * @param tab The tab index to switch to
	 */
	public void switchTab(int tab) {
		getSelectionModel().select(tab);
		FadeTransition ft = new FadeTransition(Duration.millis(300), this);
		ft.setFromValue(0.3);
		ft.setToValue(1.0);
		ft.setCycleCount(1);
		ft.setAutoReverse(true);
		ft.play();
	}
}
