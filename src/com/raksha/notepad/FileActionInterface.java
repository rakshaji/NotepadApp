package com.raksha.notepad;

import java.awt.event.ActionEvent;

public interface FileActionInterface {
	
	public abstract int performNewFileAction(ActionEvent e);
	
	public abstract void performNewWindowAction(ActionEvent e);
	
	public abstract void performOpenFileAction(ActionEvent e);
	
	public abstract void performSaveAction(ActionEvent e);
	
	public abstract void performSaveAsAction(ActionEvent e); 
	
	public abstract void performPrintAction(ActionEvent e);
	
	public abstract void performExitAction(ActionEvent e);
}
