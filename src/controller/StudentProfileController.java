package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import view.StudentProfileMenuBar;
import view.StudentProfileRootPane;
import view.StudentProfileTabPane;
import view.control.NumberOnlyTextField;
import view.tab.CreateProfileContent;
import view.tab.InputMarksContent;

/**
 * Controller class of the MVC for the StudentProfile application
 */
public class StudentProfileController {

	private StudentProfileRootPane view;
	private StudentProfile model;

	public StudentProfileController(StudentProfileRootPane view, StudentProfile model) {
		this.view = view;
		this.model = model;
		initializeView();
		attachEventHandlers();
	}

	/**
	 * Initializes any pre-requirements for the view, such as filling the combo boxs
	 */
	private void initializeView() {
		// populate the combo box values by adding the courses
		view.getTabPane().getStudentProfileContent().populateComboBox(setupAndGetCourses());
	}

	private void attachEventHandlers() {
		StudentProfileMenuBar menubar = view.getMenuBar();
		StudentProfileTabPane tabs = view.getTabPane();

		// Add event handlers to the menu items in the menu bar
		menubar.addLoadHandler(new LoadMenuHandler());
		menubar.addSaveHandler(new SaveMenuHandler());
		menubar.addExitHandler(e -> Platform.exit());

		// make it so when enter is pressed, it moves onto the next field
		CreateProfileContent cpc = tabs.getStudentProfileContent();
		cpc.getpNumber().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				cpc.getFirstName().requestFocus();
			}
		});

		cpc.getFirstName().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				cpc.getLastName().requestFocus();
			}
		});

		cpc.getLastName().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				cpc.getEmail().requestFocus();
			}
		});

		String aboutText = "Version 1.0.1\n"
				+ "Fill in the information in the Create Profile tab and click the Create button to move onto the second tab.\n"
				+ "For more information, hover the Text Fields and see the tooltips\n\n"
				+ "In the second tab, input the students marks for each module, once this is complete\n"
				+ "you can submit the marks.\n\n"
				+ "The final tab will display the results including the average mark and credits passed.\n"
				+ "You may choose to save the overview results";
		menubar.addAboutHandler(e -> alertDialogBuilder("Student Profile Mark Submission Tool", null, aboutText, AlertType.INFORMATION));

		/*
		 * add eventhandler to the create profile button to make it update the model
		 * with the current inputted user data and then switch to the next tab
		 */
		tabs.getStudentProfileContent().addCreateHandler(new CreateProfileHandler());

		// Add event handler to the courses selection combo box in the create profile tab
		tabs.getStudentProfileContent().addComboBoxHandler(new CoursesComboBoxHandler());

   		// Add event handler to the input marks tab
		tabs.getInputMarksContent().addClearHandler(new InputMarksClearHandler());

		// Add event handler to submit button in the input marks tab
		tabs.getInputMarksContent().addSubmitHandler(new InputMarksSubmitHandler());

		// Add event handler to save overview button in the last tab
		tabs.getOverviewResultsContent().addSubmitHandler(new SaveOverviewHandler());

		// use bindings to disable and enable certain nodes on the view
		BooleanBinding profilebinding = tabs.getStudentProfileContent().checkEmpty();
		BooleanBinding marksbinding = tabs.getInputMarksContent().checkEmpty();
		tabs.getStudentProfileContent().bindCreate(profilebinding);
		tabs.getInputMarksContent().bindSubmit(marksbinding);

		tabs.getInputMarksContent().disableProperty().bind(tabs.getStudentProfileContent().needsToCreateProfile().or(profilebinding));

		for (TextField tf : tabs.getInputMarksContent().getExamMarkFields()) {
			tf.focusedProperty().addListener(e -> changeColor(tf));
		}

		for (TextField tf : tabs.getInputMarksContent().getCwkMarkFields()) {
			tf.focusedProperty().addListener(e -> changeColor(tf));
		}
	}

	private class CreateProfileHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			StudentProfileTabPane tabs = view.getTabPane();
			if (!validatePNumber()) {
				alertDialogBuilder("Invalid data entered", null, "Ensure that the p number is 9 characters or less and the email is valid", AlertType.ERROR);
			} else {
				for (TextField tf : tabs.getInputMarksContent().getCwkMarkFields()) {
					tf.clear();
					changeColor(tf);
				}
				for (TextField tf : tabs.getInputMarksContent().getExamMarkFields()) {
					tf.clear();
					changeColor(tf);
				}
				updateModelWithView();
				tabs.getInputMarksContent().updateTitle("Input marks for " + model.getStudentName().getFullName() + "(" + model.getpNumber() + ")");
				tabs.switchTab(1);
			}
		}

	}

	/**
	 * A few validation checks for the pnumber and email
	 * @return true if email and pnumber are valid
	 */
	private boolean validatePNumber() {
		String p = view.getTabPane().getStudentProfileContent().getpNumber().getText();
		if (p.length() != 9 || (!(p.startsWith("P") || p.startsWith("p")))) { // check valid length and starts with p
			return false;
		}
		char[] cc = p.toCharArray();
		for (int i = 0; i < cc.length; i++) {
			if (i == 0) continue; // skip P, already validated
			char c = cc[i];
			if ((c < 48 || c > 57) && (c != 'x' && c != 'X')) {
				return false;
			}
		}
		if (!view.getTabPane().getStudentProfileContent().getEmail().getText().contains("@")
				|| view.getTabPane().getStudentProfileContent().getEmail().getText().startsWith("@")
				|| view.getTabPane().getStudentProfileContent().getEmail().getText().endsWith("@")) {
			return false;
		}
		char tail = cc[cc.length - 1];
		return (tail >= 48 && tail <= 57) || (tail == 'x' || tail == 'X');
	}

	/**
	 * Sets up and returns courses
	 * @return An array containing all courses
	 */
	private Course[] setupAndGetCourses() {
		Module ctec2121 = new Module("CTEC2121", "Organisations, Project Management and Research", true);
		Module ctec2122 = new Module("CTEC2122", "Forensics and Security", false);
		Module ctec2602 = new Module("CTEC2602", "OO Software Design and Development", false);
		Module ctec2701 = new Module("CTEC2701", "Multi - tier Web Applications", true);
		Module ctec2901 = new Module("CTEC2901", "Data Structures and Algorithms", false);
		Module lawg2003 = new Module("LAWG2003", "Issues in Criminal Justice", false);
		Module ctec2903 = new Module("CTEC2903", "System Defence Strategies", true);

		Course compSci = new Course("Computer Science");
		compSci.addModule(ctec2121);
		compSci.addModule(ctec2602);
		compSci.addModule(ctec2701);
		compSci.addModule(ctec2901);

		Course softEng = new Course("Software Engineering");
		softEng.addModule(ctec2121);
		softEng.addModule(ctec2602);
		softEng.addModule(ctec2701);
		softEng.addModule(ctec2901);

		Course compSecu = new Course("Computer Security");
		compSecu.addModule(ctec2121);
		compSecu.addModule(ctec2122);
		compSecu.addModule(ctec2701);
		compSecu.addModule(ctec2903);

		Course forenComp = new Course("Forensic Computing");
		forenComp.addModule(ctec2121);
		forenComp.addModule(ctec2122);
		forenComp.addModule(ctec2701);
		forenComp.addModule(lawg2003);

		return new Course[] { compSci, softEng, compSecu, forenComp };
	}

	/**
	 * this method is used to update the view with data from the from. it is necessary to do so when loading, so the data
	 * in the view is updated with the data loaded into the model
	 */
	public void updateViewWithModel() {
		// load in the first tab data from the model into the view
		CreateProfileContent createTab = view.getTabPane().getStudentProfileContent();
		createTab.getCourses().setValue(model.getCourse());
		createTab.getpNumber().setText(model.getpNumber());
		createTab.getFirstName().setText(model.getStudentName().getFirstName());
		createTab.getLastName().setText(model.getStudentName().getFamilyName());
		createTab.getEmail().setText(model.getEmail());
		createTab.getDate().setValue(model.getDate());

		// load in the second tab data from the model into the view
		InputMarksContent input = view.getTabPane().getInputMarksContent();

		Label[] modulenames = input.getModuleNameLabels();
		NumberOnlyTextField[] exammarks = input.getExamMarkFields();
		List<Module> list = new ArrayList<>(model.getCourse().getModulesOnCourse());
		for (int i = 0; i < list.size(); i++) {
			if (i < modulenames.length) {
				Module module = list.get(i);
				input.getCwkMarkFields()[i].setText(String.valueOf(module.getCwkMark()));
				input.getExamMarkFields()[i].setText(String.valueOf(module.getExamMark()));
				modulenames[i].setText(module.getModuleCode() + " " + module.getModuleName());
				if (module.isCwkOnly()) {
					exammarks[i].setVisible(false);
					exammarks[i].setText("0");
				} else {
					module.setExamMark(Integer.valueOf(exammarks[i].getText()));
					exammarks[i].setVisible(true);
				}
			}
		}

		view.getTabPane().getOverviewResultsContent().setTextAreaContent(generateResults());
		view.getTabPane().getOverviewResultsContent().updateStyle(model.getCourse().creditsPassed());

		for (TextField tf : view.getTabPane().getInputMarksContent().getCwkMarkFields()) {
			changeColor(tf);
		}

		for (TextField tf : exammarks) {
			changeColor(tf);
		}
	}

	private void changeColor(TextField tf) {
		if (!tf.getText().isEmpty()) {
			int mark = Integer.parseInt(tf.getText());
			if (mark < 40) {
				tf.setStyle("-fx-background-color: red; -fx-border-color: skyblue; -fx-border-width: 2px;");
			} else if (mark > 100) {
				tf.setStyle("-fx-background-color: orange; -fx-border-color: skyblue; -fx-border-width: 2px;");
			} else if (mark >= 70) {
				tf.setStyle("-fx-background-color: lightgreen; -fx-border-color: skyblue; -fx-border-width: 2px;");
			} else {
				tf.setStyle("-fx-border-color: skyblue; -fx-border-width: 2px;");
			}
		} else {
			tf.setStyle("-fx-border-color: skyblue; -fx-border-width: 2px;");
		}
	}

	/**
	 * this method is used to update the model with data from the view. it is necessary to do so when saving, so the data
	 * in the model is kept updated
	 */
	public void updateModelWithView() {
		// update the model with the data that the user has inputted into the create profile tab
		CreateProfileContent create = view.getTabPane().getStudentProfileContent();
		model.setCourse(create.getCourses().getSelectionModel().getSelectedItem());
		model.setpNumber(create.getpNumber().getText());
		model.setStudentName(new Name(create.getFirstName().getText(), create.getLastName().getText()));
		model.setEmail(create.getEmail().getText());
		model.setDate(create.getDate().getValue());

		InputMarksContent input = view.getTabPane().getInputMarksContent();

		/*
		 * updates the labels and mark field visibility on the input marks tab
		 * based on the selected course and modules on that course as some are coursework only
		*/
		Label[] modulenames = input.getModuleNameLabels();
		NumberOnlyTextField[] exammarks = input.getExamMarkFields();
		List<Module> list = new ArrayList<>(model.getCourse().getModulesOnCourse());
		for (int i = 0; i < list.size(); i++) {
			if (i < modulenames.length) {
				Module module = list.get(i);
				modulenames[i].setText(module.getModuleCode() + " " + module.getModuleName());
				if (!input.getCwkMarkFields()[i].getText().isEmpty()) {
					module.setCwkMark(Integer.valueOf(input.getCwkMarkFields()[i].getText()));
				}
				if (module.isCwkOnly()) {
					exammarks[i].setVisible(false);
					exammarks[i].setText("0");
				} else {
					if (!exammarks[i].getText().isEmpty()) {
						module.setExamMark(Integer.valueOf(exammarks[i].getText()));
					}
					exammarks[i].setVisible(true);
				}
			}
		}
	}

	private class CoursesComboBoxHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			// switch the combo box colours based on the course that the user has selected
			CreateProfileContent cpc = view.getTabPane().getStudentProfileContent();
			String course = cpc.getCourses().getSelectionModel().getSelectedItem().getCourseName();
			if (course.equals("Computer Science")) {
				cpc.setComboBoxColor(0, 255, 255, 0.6);
			} else if (course.equals("Software Engineering")) {
				cpc.setComboBoxColor(0, 122, 122, 0.6);
			} else if (course.equals("Forensic Computing")) {
				cpc.setComboBoxColor(0, 188, 0, 0.6);
			} else if (course.equals("Computer Security")) {
				cpc.setComboBoxColor(0, 0, 255, 0.6);
			}
		}
	}

	private class InputMarksClearHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			InputMarksContent imc = view.getTabPane().getInputMarksContent();
			for (int i = 0; i < 4; i++) {
				if (imc.getCwkMarkFields()[i].isVisible()) {
					imc.getCwkMarkFields()[i].clear();
				}

				if (imc.getExamMarkFields()[i].isVisible()) {
					imc.getExamMarkFields()[i].clear();
				}
			}

			for (TextField tf : view.getTabPane().getInputMarksContent().getCwkMarkFields()) {
				tf.setStyle("");
			}

			for (TextField tf : view.getTabPane().getInputMarksContent().getExamMarkFields()) {
				tf.setStyle("");
			}
		}
	}

	private class InputMarksSubmitHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			StudentProfileTabPane tabs = view.getTabPane();

			// check if valid marks have been input (values between 0 and 100 inclusive)
			InputMarksContent inputmarks = tabs.getInputMarksContent();
			boolean valid = true;

			for (int cwkm : inputmarks.getCwkMarks()) {
				if (cwkm < 0 || cwkm > 100) {
					valid = false;
				}
			}

			for (int exm : inputmarks.getExamMarks()) {
				if (exm < 0 || exm > 100) {
					valid = false;
				}
			}

			if (!valid) {
				alertDialogBuilder("Inappropriate marks entered!", null, "Please enter marks between 0 and 100!", AlertType.ERROR);
			} else {
				updateModelWithView();
				tabs.switchTab(2);
				tabs.getOverviewResultsContent().setTextAreaContent(generateResults());
				tabs.getOverviewResultsContent().updateStyle(model.getCourse().creditsPassed());
			}
		}
	}

	private String generateResults() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(model.getStudentName().getFullName()).append("\n");
		sb.append("PNo: ").append(model.getpNumber()).append("\n");
		sb.append("Email: ").append(model.getEmail()).append("\n");
		sb.append("Date: ").append(model.getDate().getDayOfMonth()).append("/")
				.append(model.getDate().getMonthValue()).append("/").append(model.getDate().getYear())
				.append("\n");
		sb.append("Course: ").append(model.getCourse().getCourseName()).append("\n");
		sb.append("\n");
		sb.append("2nd year average:").append("\n");
		sb.append("========").append("\n");
		sb.append("Credits passed: ").append(model.getCourse().creditsPassed()).append("\n");
		sb.append("Year average mark: ").append(model.getCourse().yearAverageMark());
		return sb.toString();
	}

	private class SaveOverviewHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			String content = view.getTabPane().getOverviewResultsContent().getTextAreaContent();
			if (content.isEmpty()) {
				alertDialogBuilder("Error", "Empty overview results!", "Please create a profile and input marks before attempting to save", AlertType.ERROR);
			} else {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Student Overview Results");
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Documents", "*.txt"));
				File file = fileChooser.showSaveDialog(null);
				if (file != null) {
					try (PrintWriter pw = new PrintWriter(file)) {
						pw.println(content);
						pw.flush();
						alertDialogBuilder("Information", "Saved successfully", "Results Overview saved",
								AlertType.INFORMATION);
					} catch (IOException ioExcep) {
						ioExcep.printStackTrace();
						alertDialogBuilder("Error", "Save failure", "Failed to save StudentProfile", AlertType.ERROR);
					}
				}
			}
		}
	}

	private class LoadMenuHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Load Student Profile");
			File file = fileChooser.showOpenDialog(null);
			if (file != null) {
				try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
					model = (StudentProfile) ois.readObject();
					updateViewWithModel();

					if (!view.getTabPane().getStudentProfileContent().checkEmpty().get()) {
						view.getTabPane().getStudentProfileContent().setCreatedProfile(true);
					}
					view.getTabPane().getInputMarksContent().updateTitle(
							"Input marks for " + model.getStudentName().getFullName() + "(" + model.getpNumber() + ")");
					alertDialogBuilder("Information", "Load success", "StudentProfile loaded", AlertType.INFORMATION);
				} catch (IOException ioExcep) {
					alertDialogBuilder("Error", "Load failure", "Failed to load StudentProfile: IOException",
							AlertType.ERROR);
				} catch (ClassNotFoundException c) {
					alertDialogBuilder("Error", "Load failure", "Failed to load StudentProfile: ClassNotFoundException",
							AlertType.ERROR);
				}
			}
		}
	}

	private class SaveMenuHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			updateModelWithView();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save Student Profile");
			File file = fileChooser.showSaveDialog(null);
			if (file != null) {
				try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
					oos.writeObject(model);
					oos.flush();
					alertDialogBuilder("Information", "Save successful", "Successfully saved StudentProfile",
							AlertType.INFORMATION);
				} catch (IOException ioExcep) {
					// ioExcep.printStackTrace();
					System.err.println("Error saving");
					alertDialogBuilder("Information", "Save failure", "Failed to save StudentProfile", AlertType.ERROR);
				}
			}
		}
	}

	private void alertDialogBuilder(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
