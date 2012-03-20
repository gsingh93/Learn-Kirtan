package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.Key;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

	public static void parseAndPlay() {

		Key[] keys = Main.keys;
		File file = new File("shabad.txt");
		Scanner scanner = null;
		int key = 0;

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext("[A-Za-z.']+ *")) {
			String note = scanner.next("[A-Za-z.']+");

			String modifiers = "";
			int index = note.indexOf(".");
			if (index == -1)
				index = note.indexOf("'");
			if (index != -1) {
				modifiers = note.substring(index);
				note = note.substring(0, index);
			}

			System.out.println(note);
			System.out.println(modifiers);
			if (note.equals("Sa")) {
				key = 10;
			} else if (note.equals("Re")) {
				key = 12;
			} else if (note.equals("Ga")) {
				key = 14;
			} else if (note.equals("Ma")) {
				key = 15;
			} else if (note.equals("Pa")) {
				key = 17;
			} else if (note.equals("Dha")) {
				key = 19;
			} else if (note.equals("Ni")) {
				key = 21;
			} else {
				System.out.println("Invalid note.");
			}

			if (modifiers.contains("'")) {
				key++;
			}
			if (modifiers.contains(".")) {
				key += 12;
			}

			keys[key].playOnce();
		}

	}
}
