package com.raksha.notepad;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import static com.raksha.notepad.AppConstants.*;

public class FileMenu extends NotepadMenu implements FileActionInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String FILE_LABEL = "File";
	private JMenuItem newFileMenuItem = new JMenuItem(NEW_MENU_ITEM);
	private JMenuItem newWindowMenuItem = new JMenuItem(NEW_WINDOW_MENU_ITEM);
	private JMenuItem openFileMenuItem = new JMenuItem(OPEN_MENU_ITEM);
	private JMenuItem saveFileMenuItem = new JMenuItem(SAVE_MENU_ITEM);
	private JMenuItem saveAsFileMenuItem = new JMenuItem(SAVE_AS_MENU_ITEM);
	private JMenuItem printFileMenuItem = new JMenuItem(PRINT_MENU_ITEM);
	private JMenuItem exitWindowMenuItem = new JMenuItem(EXIT_MENU_ITEM);
	private JTextArea textArea;
	private JFrame frame;
	private String filename;
	private String filePath;

	FileMenu() {
		super(FILE_LABEL);

		// add sub menus
		this.add(newFileMenuItem);
		this.add(newWindowMenuItem);
		this.add(openFileMenuItem);
		this.add(saveFileMenuItem);
		this.add(saveAsFileMenuItem);
		this.addSeparator();
		this.add(printFileMenuItem);
		this.addSeparator();
		this.add(exitWindowMenuItem);
		this.setEnabled(true);
		
		newFileMenuItem.addActionListener((e) -> performNewFileAction(e));
		newWindowMenuItem.addActionListener((e) -> performNewWindowAction(e));
		openFileMenuItem.addActionListener((e) -> performOpenFileAction(e));
		saveFileMenuItem.addActionListener((e) -> performSaveAction(e));
		saveAsFileMenuItem.addActionListener((e) -> performSaveAsAction(e));
		printFileMenuItem.addActionListener((e) -> performPrintAction(e));
		exitWindowMenuItem.addActionListener((e) -> performExitAction(e));
	}

	FileMenu(JTextArea textArea) {
		this();
		this.setTextArea(textArea);
	}

	FileMenu(NotepadFrame notepadFrame, JTextArea textArea, String filename) {
		this();
		this.setTextArea(textArea);
		this.setFilename(filename);
		this.setFrame(notepadFrame);
	}

	private JTextArea getTextArea() {
		return textArea;
	}

	private void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
		this.textArea.addKeyListener(new KeyListener() {
			
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(EMPTY_STRING.equals(getTextinTextArea())) {
					removeUnsavedChangesMarkFromFileName();
				} else {
					addUnsavedChangesMarkInFileName();	
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	private String getFilename() {
		return filename;
	}

	private void setFilename(String filepath) {
		this.filename = filepath;
		if (getFrame() != null) {
			getFrame().setTitle(filename);
		}
	}

	private JFrame getFrame() {
		return frame;
	}

	private void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private void setTextinTextArea(String text) {
		getTextArea().setText(text);
	}

	private String getTextinTextArea() {
		return this.textArea.getText();
	}

	private class PrintSetup implements Printable {
		
		public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
			if (page > 0) { /* We have only one page, and 'page' is zero-based */
				return NO_SUCH_PAGE;
			}
			
			/* User (0,0) is typically outside the imageable area, so we must
			* translate by the X and Y values in the PageFormat to avoid clipping
			*/
			Graphics2D g2d = (Graphics2D)g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			
			/* Now we perform our rendering */
			g.drawString("Test the print dialog!", 100, 100);
			
			/* tell the caller that this page is part of the printed document */
			return PAGE_EXISTS;
		}
		
	} 
	
	private void saveFile(String filePath, String text) {
		try {
			FileWriter fileWriter = new FileWriter(filePath);
			fileWriter.write(text);
			fileWriter.close();
		} catch (IOException e) {
			System.err.println("Oops! there is problem saving this file - " + e);
		}
		removeUnsavedChangesMarkFromFileName();
	}
	
	private void removeUnsavedChangesMarkFromFileName() {
		String title = getFrame().getTitle();
		if(title.startsWith(UNSAVED_CHANGES_IDENTIFIER)) {
			title = title.substring(1);
			getFrame().setTitle(title);
		}
	}
	
	private void addUnsavedChangesMarkInFileName() {
		String title = getFrame().getTitle();
		if(!title.startsWith(UNSAVED_CHANGES_IDENTIFIER)) {
			getFrame().setTitle(UNSAVED_CHANGES_IDENTIFIER + title);
		}
	}
	
	private void showOverwriteDialog(String filePath, String text) {
		Object[] options1 = { YES_BTN
								, NO_BTN };
		int m = JOptionPane.showOptionDialog(this.getParent()
				, getFilename() + " already exists. Do you want to overwrite?"
				, "Overwrite?"
				, JOptionPane.YES_NO_OPTION
				, JOptionPane.QUESTION_MESSAGE
				, null
				, options1,
				options1[1]);
		if (m == 0) { // yes overwrite
			saveFile(filePath, text);
		}
	}
	
	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public int performNewFileAction(ActionEvent e) {
		String txt = getTextinTextArea();

		if (EMPTY_STRING.equals(txt)) {
			return -1;
		}

		// Custom button text
		Object[] options = { SAVE_BTN, DONT_SAVE_BTN, CANCEL_BTN };
		//JComponent jc = (JComponent) ae.getSource();
		int n = JOptionPane.showOptionDialog(this, "Do you want to save changes to " + getFilename() + "?", SAVE_BTN,
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

		if (n == 0) {// save clicked
			JFileChooser jf = new JFileChooser() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void approveSelection() {
					// setting the label as the path of the selected file
					String filePath = this.getSelectedFile().getAbsolutePath();
					File file = new File(filePath);
					if (file.exists()) {
						setFilename(file.getName());
						showOverwriteDialog(filePath, txt);
					} else {
						// not saved already then save with new filename
						saveFile(filePath, txt);
					}
					super.approveSelection();
					setTextinTextArea(EMPTY_STRING);
				}
			}; // default constructor JFileChooser is called.
			jf.showSaveDialog(null);
		} else if (n == 1) {
			// Don't save button clicked
			setFilename(DEFAULT_FILENAME);
			setTextinTextArea(EMPTY_STRING);// clear the text
		} 
		
		return n;
	}

	@Override
	public void performNewWindowAction(ActionEvent e) {
		new Thread(() -> {
			JDialog dialog = new JDialog(new NotepadFrame());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}).start();
	}

	@Override
	public void performOpenFileAction(ActionEvent e) {
		JFileChooser jf = new JFileChooser() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void approveSelection() {
				// setting the label as the path of the selected file
				String filePath = this.getSelectedFile().getAbsolutePath();
				setFilePath(filePath);
				
				File file = new File(filePath);
				setFilename(file.getName());
				String text = EMPTY_STRING;
				try {
					FileReader fileReader = new FileReader(filePath);
					while (fileReader.ready()) {
						text += String.valueOf((char) fileReader.read());
					}
					setTextinTextArea(text);
					fileReader.close();
				} catch (IOException e) {
					System.err.println("Oops! there is some problem reading this file - " + e);
				}
				super.approveSelection();
			}

		}; // default constructor JFileChooser is called.
		jf.showOpenDialog(null);
	}

	@Override
	public void performSaveAction(ActionEvent e) {
		// if file already saved then simple save the changes.
		if(!DEFAULT_FILENAME.equals(getFilename())) {
			saveFile(filePath, getTextinTextArea());
			return;
		} 
		
		// else save as new file
		JFileChooser jf = new JFileChooser() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void approveSelection() {
				// setting the label as the path of the selected file
				String filePath = this.getSelectedFile().getAbsolutePath();
				File file = new File(filePath);
				if (file.exists()) {
					showOverwriteDialog(filePath, getTextinTextArea());
				} else {
					// not saved already then save with new filename
					saveFile(filePath, getTextinTextArea());
				}
				super.approveSelection();
			}
		}; // default constructor JFileChooser is called.
		jf.showSaveDialog(null);
		
	}

	@Override
	public void performSaveAsAction(ActionEvent e) {
		JFileChooser jf = new JFileChooser() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void approveSelection() {
				// setting the label as the path of the selected file
				String filePath = this.getSelectedFile().getAbsolutePath();
				File file = new File(filePath);
				if (file.exists()) {
					showOverwriteDialog(filePath, getTextinTextArea());
				} else {
					// not saved already then save with new filename
					saveFile(filePath, getTextinTextArea());
				}
				super.approveSelection();
			}
		}; // default constructor JFileChooser is called.
		jf.showSaveDialog(null);
	}

	@Override
	public void performPrintAction(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PageFormat pf = job.pageDialog(aset);
        job.setPrintable(new PrintSetup(), pf);
        boolean ok = job.printDialog(aset);
        if (ok) {
            try {
                 job.print(aset);
            } catch (PrinterException ex) {
             /* The job did not successfully complete */
            }
        }
		
	}

	@Override
	public void performExitAction(ActionEvent e) {
		getFrame().dispose();
	}
}
