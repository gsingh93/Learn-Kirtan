package gsingh.learnkirtan.installer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class SoundBankInstaller {

	/**
	 * Installs the soundbank file in the JRE lib/audio folder
	 * 
	 * @return - true if file is present or was installed correctly. False if
	 *         there was an error.
	 */
	public boolean installSoundBank() {
		// Determine where the JRE is installed
		File file = new File("C:\\Program Files (x86)\\Java\\jre6");
		if (!file.exists()) {
			// LOGGER.warning("C:\\Program Files (x86)\\Java\\jre6 does not exist.");
			file = new File("C:\\Program Files\\Java\\jre6");
			if (!file.exists()) {
				// LOGGER.severe("C:\\Program Files\\Java\\jre6 does not exist.");
				return false;
			}
		}

		// If the JRE is properly installed, check if the SoundBank is already
		// installed
		file = new File(file.getAbsolutePath()
				+ "\\lib\\audio\\soundbank-min.gm");
		if (!file.exists()) {
			// LOGGER.warning("soundbank-min.gm does not exist.");
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("soundbank-min.gm");
			OutputStream os = null;
			try {
				os = new FileOutputStream(file.getAbsolutePath());
				IOUtils.copy(is, os);
			} catch (IOException e) {
				// LOGGER.severe("An IOException was thrown when installing the soundbank.");
				e.printStackTrace();
				return false;
			}
		} else {
			// LOGGER.info("Soundbank found.");
		}

		// LOGGER.info("Soundbank installation successful.");
		return true;
	}
}
