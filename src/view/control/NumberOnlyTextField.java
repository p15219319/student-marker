package view.control;

import javafx.scene.control.TextField;

/**
 * A TextField implementation which only allows numbers to be entered
 */
public class NumberOnlyTextField extends TextField {

	public NumberOnlyTextField(String prompt) {
		super.setPromptText(prompt);
	}

	@Override
	public void replaceText(int s, int e, String t) {
		if (validate(t)) {
			super.replaceText(s, e, t);
		}
	}

	@Override
	public void replaceSelection(String t) {
		if (validate(t)) {
			super.replaceSelection(t);
		}
	}

	private boolean validate(String t) {
		//System.out.println(t);
		return t.length() == 0 || t.matches("[0-9]*");
	}
}
