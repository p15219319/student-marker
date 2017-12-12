package view.tab;

import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.control.NumberOnlyTextField;

public class InputMarksContent extends FlowPane {

	private GridPane container;
	private Label[] modulenames;
	private NumberOnlyTextField[] cwkmarks, exammarks;
	private Button clear, submit;
	private Label title;

	public InputMarksContent() {
		super(Orientation.VERTICAL); // to make this flowpane vertical
		// initialize the gridpane and set the spacing between each column and row
		container = new GridPane();
		container.setHgap(30);
		container.setVgap(20);
		initDefaultValues();

		// create labels for the titles
		Label[] header = { new Label("Module"), new Label("Cwk Mark"), new Label("Exam Mark") };
		for (int col = 0; col < header.length; col++) {
			Label lbl = header[col];
			// make font bold
			lbl.setFont(Font.font(lbl.getFont().getFamily(), FontWeight.BOLD, lbl.getFont().getSize()));
			container.add(lbl, col, 0);
		}

		for (int row = 0; row < 4; row++) {
			container.add(modulenames[row], 0, row + 1);
			modulenames[row].setPadding(new Insets(0, 20, 0, 0));
			container.add(cwkmarks[row], 1, row + 1);
			container.add(exammarks[row], 2, row + 1);
		}

		clear = new Button("Clear");
		submit = new Button("Submit Marks");

		setAlignment(Pos.CENTER);

		title = new Label("Input marks for Student (P#)");
		title.setAlignment(Pos.CENTER);
		title.setFont(Font.font("Verdana", FontWeight.BOLD, 21));
		title.setPadding(new Insets(30));

		HBox hbox = new HBox(clear, submit);
		hbox.setPadding(new Insets(20, 0, 0, 0));
		hbox.setSpacing(20);
		hbox.setAlignment(Pos.CENTER);
		getChildren().addAll(title, container, hbox);
	}

	public void updateTitle(String title) {
		this.title.setText(title);
	}

	public NumberOnlyTextField[] getExamMarkFields() {
		return exammarks;
	}

	public NumberOnlyTextField[] getCwkMarkFields() {
		return cwkmarks;
	}

	public Label[] getModuleNameLabels() {
		return modulenames;
	}

	public int[] getCwkMarks() {
		int[] marks = new int[4];
		for (int i = 0; i < marks.length; i++) {
			marks[i] = Integer.valueOf(cwkmarks[i].getText());
		}
		return marks;
	}

	public int[] getExamMarks() {
		int[] marks = new int[4];
		for (int i = 0; i < marks.length; i++) {
			marks[i] = Integer.valueOf(exammarks[i].getText());
		}
		return marks;
	}

	private void initDefaultValues() {
		modulenames = new Label[] {
				new Label("Profile not created"),
				new Label("Profile not created"),
				new Label("Profile not created"),
				new Label("Profile not created")
	    };

		cwkmarks = new NumberOnlyTextField[] {
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0")
		};

		exammarks = new NumberOnlyTextField[] {
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0"),
				new NumberOnlyTextField("0")
		};

		for (int i = 0; i < cwkmarks.length; i++) {
			cwkmarks[i].setMaxWidth(75);
			exammarks[i].setMaxWidth(75);
		}
	}

	public void addClearHandler(EventHandler<ActionEvent> e) {
		clear.setOnAction(e);
	}

	public void addSubmitHandler(EventHandler<ActionEvent> e) {
		submit.setOnAction(e);
	}

	public void bindSubmit(BooleanBinding binding) {
		submit.disableProperty().bind(binding);
	}

	public BooleanBinding checkEmpty() {
		return cwkmarks[0].textProperty().isEmpty().or(cwkmarks[1].textProperty().isEmpty().or(cwkmarks[2].textProperty().isEmpty()).or(cwkmarks[3].textProperty().isEmpty())
				.or(exammarks[0].textProperty().isEmpty()).or(exammarks[1].textProperty().isEmpty()).or(exammarks[2].textProperty().isEmpty()).or(exammarks[3].textProperty().isEmpty()));
	}
}
