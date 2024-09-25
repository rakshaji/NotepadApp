package com.raksha.notepad;

import javax.swing.JMenuBar;
import javax.swing.JTextArea;

public class NotepadMenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FileMenu fileMenu;

	NotepadMenuBar() {
		super();
		fileMenu = new FileMenu();
		this.add(fileMenu); // add to the menubar
	}
	
	NotepadMenuBar(JTextArea textArea) {
		super();
		
		fileMenu = new FileMenu(textArea );
		this.add(fileMenu); // add to the menubar
	}
	
	NotepadMenuBar(NotepadFrame notepadFrame, JTextArea textArea, String filename) {
		super();
		
		fileMenu = new FileMenu(notepadFrame, textArea, filename);
		this.add(fileMenu); // add to the menubar
	}
	
	public FileMenu getFileMenu() {
		return fileMenu;
	}
}
