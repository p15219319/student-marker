package view.tab;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;

public class OverviewResultsContent extends BorderPane {

	private TextArea textArea;
	private Button submit;

	public OverviewResultsContent() {
		textArea = new TextArea();
		submit = new Button("Save Overview");
		textArea.setEditable(false);
		BorderPane.setAlignment(textArea, Pos.TOP_CENTER);
		BorderPane.setAlignment(submit, Pos.TOP_CENTER);
		BorderPane.setMargin(textArea, new Insets(10));
		BorderPane.setMargin(submit, new Insets(10));
		setTop(textArea);
		setCenter(submit);
	}

	/**
	 * @param e Adds the EventHandler 'e' to the submit button
	 */
	public void addSubmitHandler(EventHandler<ActionEvent> e) {
		submit.setOnAction(e);
	}

	/**
	 * @return The text in the text area
	 */
	public String getTextAreaContent() {
		return textArea.getText();
	}

	/**
	 * @param content Sets the text in the text area
	 */
	public void setTextAreaContent(String content) {
		textArea.setText(content);
	}

	public void updateStyle(int marks) {
		Region r = (Region) textArea.lookup(".content");
		if (textArea.getText().isEmpty()) {
			r.setStyle("");
		} else if (marks >= 120) {
			textArea.setStyle("-fx-text-fill: white");
			r.setStyle("-fx-background-color: green");
		} else {
			textArea.setStyle("-fx-text-fill: white");
			r.setStyle("-fx-background-color: red");
		}
	}
}
