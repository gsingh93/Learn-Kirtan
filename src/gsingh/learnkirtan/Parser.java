package gsingh.learnkirtan;

import gsingh.learnkirtan.keys.Key;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Parser {

	public static void parseAndPlay(File file) {

		Key[] keys = Main.keys;
		Scanner scanner = null;
		int key = 0;

		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext("[A-Za-z.']+ *")) {
			String note = scanner.next("[A-Za-z.']+");

			int count = 0;
			for (int i = 0; i < 3; i++) {
				if (note.substring(i, i + 1).matches("[A-Za-z]"))
					break;
				count++;
			}
			String prefix = note.substring(0, count);
			String suffix = "";
			note = note.substring(count);
			note = note.toUpperCase();
			int index = note.indexOf(".");
			if (index == -1)
				index = note.indexOf("'");
			if (index != -1) {
				suffix = note.substring(index);
				note = note.substring(0, index);
			}

			System.out.println(prefix + note + suffix);
			if (note.equals("SA")) {
				key = 10;
			} else if (note.equals("RE")) {
				key = 12;
			} else if (note.equals("GA")) {
				key = 14;
			} else if (note.equals("MA")) {
				key = 15;
			} else if (note.equals("PA")) {
				key = 17;
			} else if (note.equals("DHA")) {
				key = 19;
			} else if (note.equals("NI")) {
				key = 21;
			} else {
				System.out.println("Invalid note.");
				JOptionPane.showMessageDialog(null, "Error: Invalid note.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

			// TODO: Check if notes have valid modifiers
			if (prefix.contains("'")) {
				key--;
			}
			if (prefix.contains(".")) {
				key -= 12;
			}
			if (suffix.contains("'")) {
				key++;
			}
			if (suffix.contains(".")) {
				key += 12;
			}

			if (key > 0 && key < 48)
				keys[key].playOnce(500);
			else
				JOptionPane.showMessageDialog(null, "Error: Invalid note.",
						"Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}
