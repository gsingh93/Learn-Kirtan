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
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNext("[A-Za-z.]+ *")) {
			String note = scanner.next("[A-Za-z.]+ *");

			System.out.println(note);
			note = note.trim();
			if (note.equals("Sa")) {
				keys[10].doClick();
				keys[10].playOnce();
			} else if (note.equals("Re")) {
				keys[12].doClick();
				keys[12].playOnce();
			} else if (note.equals("Ga")) {
				keys[14].doClick();
				keys[14].playOnce();
			} else if (note.equals("Ma")) {
				keys[15].doClick();
				keys[15].playOnce();
			} else if (note.equals("Pa")) {
				keys[17].doClick();
				keys[17].playOnce();
			} else if (note.equals("Dha")) {
				keys[19].doClick();
				keys[19].playOnce();
			} else if (note.equals("Ni")) {
				keys[21].doClick();
				keys[21].playOnce();
			} else if (note.equals("Sa.")) {
				keys[22].doClick();
				keys[22].playOnce();
			}
		}

	}
}
