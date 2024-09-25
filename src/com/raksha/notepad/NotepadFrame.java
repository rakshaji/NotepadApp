package com.raksha.notepad;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

public class NotepadFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 500;
	private NotepadTextArea notepadTextArea = new NotepadTextArea();
	private NotepadMenuBar notepadMenuBar = new NotepadMenuBar(this, notepadTextArea, AppConstants.DEFAULT_FILENAME);

	NotepadFrame() {
		super();
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setJMenuBar(notepadMenuBar);
		setContentPane(notepadTextArea);
		setVisible(true);
		setTitle(AppConstants.DEFAULT_FILENAME);
		setLocationRelativeTo(null);// to place UI to the center of the screen
		//favicon
		File imageFile = new File(AppConstants.FAVICON);
		if(imageFile.exists()) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(AppConstants.FAVICON));
		} else {
			setIconImage(Toolkit.getDefaultToolkit().getImage(AppConstants.FAVICON.replace("resources/", "../../")));
		}
		
	}
	
	@Override
	public void dispose() {
		int actionNum = notepadMenuBar.getFileMenu().performNewFileAction(null);
		if(actionNum < 2 && actionNum >= -1) {
			super.dispose();
		} else {
			return;
		}
	}
}
