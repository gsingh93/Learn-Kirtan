package gsingh.learnkirtan.ui.shabadeditor;

import gsingh.learnkirtan.shabad.Shabad;

public interface ShabadEditor {

	public Shabad getShabad();

	public boolean isModified();

	public void setText(String text);

	public String getText();

	public void setWords(String text);
}
