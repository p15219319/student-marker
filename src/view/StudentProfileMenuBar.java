package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

/**
 * MenuBar class of the StudentProfile View
 */
public class StudentProfileMenuBar extends MenuBar {

	private MenuItem load, save, exit, about;

	public StudentProfileMenuBar() {
		// construct menu objects
		Menu file = new Menu("_File");
		Menu help = new Menu("_Help");

		// construct menu item objects
		load = new MenuItem("_Load Student Data");
		save = new MenuItem("_Save Student Data");
		exit = new MenuItem("E_xit");
		about = new MenuItem("_About");

		// set key shortcuts
		load.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
		save.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
		exit.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		about.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));

		// add menu items to menus
		file.getItems().addAll(load, save, new SeparatorMenuItem(), exit);
		help.getItems().add(about);

		// add menus to this menu bar
		this.getMenus().addAll(file, help);
	}

	/**
	 * @param handler The EventHandler to attach to the load MenuItem
	 */
	public void addLoadHandler(EventHandler<ActionEvent> handler) {
		load.setOnAction(handler);
	}

	/**
	 * @param handler The EventHandler to attach to the save MenuItem
	 */
	public void addSaveHandler(EventHandler<ActionEvent> handler) {
		save.setOnAction(handler);
	}

	/**
	 * @param handler The EventHandler to attach to the exit MenuItem
	 */
	public void addExitHandler(EventHandler<ActionEvent> handler) {
		exit.setOnAction(handler);
	}

	/**
	 * @param handler The EventHandler to attach to the about MenuItem
	 */
	public void addAboutHandler(EventHandler<ActionEvent> handler) {
		about.setOnAction(handler);
	}

}
