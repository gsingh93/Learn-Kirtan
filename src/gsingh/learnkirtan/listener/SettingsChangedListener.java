package gsingh.learnkirtan.listener;

public interface SettingsChangedListener {
	public void onSettingsChanged(String settingsPath, String oldValue,
			String newValue);
}
