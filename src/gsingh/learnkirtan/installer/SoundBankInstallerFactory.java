package gsingh.learnkirtan.installer;


public class SoundBankInstallerFactory {

	public static SoundBankInstaller getInstaller() {
		if (isWindowsVistaOr7())
			return new WindowsSoundBankInstaller();
		if (isWindowsXP())
			return new WindowsXPSoundBankInstaller();
		if (isMacOSX())
			return new MacOSXSoundBankInstaller();
		if (isDebianLinux())
			return new DebianLinuxSoundBankInstaller();
		else
			return null; // TODO
	}

	private static boolean isDebianLinux() {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isMacOSX() {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isWindowsXP() {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isWindowsVistaOr7() {
		// TODO Auto-generated method stub
		return true;
	}
}
