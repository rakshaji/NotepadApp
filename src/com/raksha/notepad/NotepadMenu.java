package com.raksha.notepad;

import javax.swing.JMenu;

public class NotepadMenu extends JMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menuLabel = "No Label";
	private boolean isEnabled = false;
	private String shortcutKey;

	NotepadMenu() {
		super();
	}
	
	NotepadMenu(String label) {
		super(label);
		this.menuLabel = label;
	}

	public String getMenuLabel() {
		return menuLabel;
	}

	public String getShortcutKey() {
		return shortcutKey;
	}

	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}
	
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
}
