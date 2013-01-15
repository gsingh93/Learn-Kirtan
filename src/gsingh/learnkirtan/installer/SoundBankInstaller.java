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
		// Get the JRE directory
		String jreDir = System.getProperty("java.home");

		// Check if the SoundBank is already installed
		File soundBank = new File(String.format(jreDir
				+ "%1$slib%1$saudio%1$ssoundbank.gm", File.separator));
		File soundBankMin = new File(String.format(jreDir
				+ "%1$slib%1$saudio%1$ssoundbank-min.gm", File.separator));

		// If neither version of soundbank exists, install the min version
		if (!soundBank.exists() && !soundBankMin.exists()) {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("soundbank-min.gm");
			OutputStream os = null;
			try {
				os = new FileOutputStream(soundBank.getAbsolutePath());
				IOUtils.copy(is, os);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}
}
