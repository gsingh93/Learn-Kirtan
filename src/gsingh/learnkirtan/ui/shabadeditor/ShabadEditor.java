package gsingh.learnkirtan.ui.shabadeditor;

import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadMetaData;
import gsingh.learnkirtan.shabad.ShabadNotes;

import java.util.List;

public interface ShabadEditor {

	public Shabad getShabad();

	public void setShabad(Shabad shabad);

	public ShabadNotes getNotes();

	public ShabadMetaData getMetaData();

	public List<String> getWords();

	public boolean isModified();

	public boolean isValidShabad();

	public void setRepeating(boolean bool);

	public boolean getRepeating();

	void setMetaData(ShabadMetaData metaData);
}
