package gsingh.learnkirtan.ui.shabadeditor;

import gsingh.learnkirtan.shabad.Shabad;
import gsingh.learnkirtan.shabad.ShabadMetaData;
import gsingh.learnkirtan.shabad.ShabadNotes;

public interface ShabadEditor {

	public Shabad getShabad();

	public void setShabad(Shabad shabad);

	public ShabadNotes getNotes();

	public ShabadMetaData getMetaData();

	public String getWords();

	public boolean isModified();

	public boolean isValidShabad();

}
