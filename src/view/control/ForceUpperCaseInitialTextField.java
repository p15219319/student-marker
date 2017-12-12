package view.control;

import javafx.scene.control.TextField;

/**
 * This class is a subclass of TextField which ensures that the first character in a TextField 
 * is upper case, it is used for the first name and last name TextField's in the view
 */
public class ForceUpperCaseInitialTextField extends TextField {

	@Override
	public void replaceText(int s, int e, String t) {
		if (!t.matches("[0-9]*") || t.length() == 0) { //regex to disallow numbers, and length check to allow backspaces
			if (s == 0) { // if on first character
				t = t.toUpperCase(); // set to upper case
			}
			super.replaceText(s, e, t);
		}
	}
}
