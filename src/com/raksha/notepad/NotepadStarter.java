package com.raksha.notepad;

public class NotepadStarter {

	public static void main(String[] args) {
		new Thread(() -> new NotepadFrame()).start();
	}
}
