package gsingh.learnkirtan.installer;


public class SoundBankInstallerFactory {

	public static SoundBankInstaller getInstaller() {
		return new WindowsSoundBankInstaller();
	}
}
