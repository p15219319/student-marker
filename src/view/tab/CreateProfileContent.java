package view.tab;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import model.Course;
import view.control.ForceUpperCaseInitialTextField;

public class CreateProfileContent extends VBox {

	private ComboBox<Course> courses; //ComboBox containing courses
	private TextField pNumber, firstName, lastName, email; //TextFields for the user to input data
	private DatePicker date; // DatePicker object
	private Button create; //the create Button object
	private SimpleBooleanProperty createdProfile; // tells if a profile has previously been created

	public CreateProfileContent() {
		createdProfile = new SimpleBooleanProperty(false);
		courses = new ComboBox<>();
		setComboBoxColor(0, 255, 255, 0.6);

		pNumber = new TextField("P");
		firstName = new ForceUpperCaseInitialTextField();
		lastName = new ForceUpperCaseInitialTextField();
		email = new TextField();
		date = new DatePicker(LocalDate.now());

		create = new Button("Create Profile");
		create.setOnMouseClicked(e -> createdProfile.set(true));

		setAlignment(Pos.CENTER);

		Label title = new Label("Create a Profile");
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 21));
		title.setPadding(new Insets(30));
		getChildren().addAll(
				title,
				createSection("Select course:", courses, "Input the students course", e -> courses.getSelectionModel().select(0)),
				createSection("Input P number:", pNumber, "Input the students P number, maximum 9 characters", e -> pNumber.setText("P")),
				createSection("Input first name:", firstName, "Input the students first name, no numbers allowed", e -> firstName.clear()),
				createSection("Input surname:", lastName, "Input the students last name, no numbers allowed", e -> lastName.clear()),
				createSection("Input email:", email, "Input the students email", e -> email.clear()),
				createSection("Input date:", date, "Use the date picker to select a date", e -> date.setValue(LocalDate.now())),
				create
		);
	}

	/**
	 * Utility method to create a section consisting of 2 elements, a Label & Control
	 * @param text The text to create a Label for
	 * @param value The Control object
	 * @param info The tooltip for the info label
	 * @param clearHandler The EventHandler for the clear button
	 * @return A FlowPane which adds and positions a Label & Control
	 */
	private FlowPane createSection(String text, Control value, String info, EventHandler<ActionEvent> clearHandler) {
		FlowPane section = new FlowPane();
		section.setVgap(8);
		section.setHgap(4);
		section.setPrefWrapLength(300);
		section.setPadding(new Insets(10));
		section.setAlignment(Pos.CENTER);
		HBox label = new HBox();
		label.setMinWidth(100);
		label.setPrefWidth(100);

		value.setMinWidth(160);
		value.setPrefWidth(160);

		label.getChildren().add(new Label(text));

		Tooltip tooltip = new Tooltip(info);
		value.setTooltip(tooltip);

		Button clear = new Button("X");
		clear.setOnAction(clearHandler);
		clear.setTextFill(Color.RED);
		section.getChildren().addAll(label, value, clear);
		return section;
	}

	/**
	 * Populates the ComboBox with the given Courses
	 * @param courses The courses to populate the ComboBox with
	 */
	public void populateComboBox(Course[] courses) {
		this.courses.getItems().addAll(courses);
		this.courses.getSelectionModel().select(0);
	}

	/**
	 * @return The courses ComboBox
	 */
	public ComboBox<Course> getCourses() {
		return courses;
	}

	/**
	 * @return The email TextField
	 */
	public TextField getEmail() {
		return email;
	}

	/**
	 * @return The date DatePicker
	 */
	public DatePicker getDate() {
		return date;
	}

	/**
	 * @return The lastName TextField
	 */
	public TextField getLastName() {
		return lastName;
	}

	/**
	 * @return The firstName TextField
	 */
	public TextField getFirstName() {
		return firstName;
	}

	/**
	 * @return The pNumber TextField
	 */
	public TextField getpNumber() {
		return pNumber;
	}

	/**
	 * @param e Adds the EventHandler 'e' to the create button
	 */
	public void addCreateHandler(EventHandler<ActionEvent> e) {
		create.setOnAction(e);
		createdProfile.set(true);
	}

	/**
	 * @param e Adds the EventHandler 'e' to the combo box
	 */
	public void addComboBoxHandler(EventHandler<ActionEvent> e) {
		courses.setOnAction(e);
	}

	/**
	 * Changes the combo box background color to the given color
	 * @param r red
	 * @param g green
	 * @param b blue
	 * @param a alpha
	 */
	public void setComboBoxColor(int r, int g, int b, double a) {
		courses.setStyle("-fx-background-color: rgba(" + r + ", " + g + ", " + b + ", " + a + ");");
	}

	/**
	 * @param binding Binds the create buttons disabled property to the given binding
	 */
	public void bindCreate(BooleanBinding binding) {
		create.disableProperty().bind(binding);
	}

	/**
	 * @return A binding which checks if any inputted data is null, empty or invalid
	 */
	public BooleanBinding checkEmpty() {
		return courses.itemsProperty().isNull().or(firstName.textProperty().isEmpty()).or(lastName.textProperty().isEmpty()).or(email.textProperty().isEmpty()).or(date.valueProperty().isNull());
	}

	/**
	 * @return A BooleanBinding which checks if a profile has previously been created (or loaded from file) in the current session
	 */
	public BooleanBinding needsToCreateProfile() {
		return createdProfile.not();
	}

	public void setCreatedProfile(boolean created) {
		createdProfile.set(created);
	}
}
