package gsingh.learnkirtan;

import gsingh.learnkirtan.ui.View;

import java.beans.PropertyChangeListener;

import javax.swing.event.SwingPropertyChangeSupport;

public class StateModel {

	public enum FileState {
		OPEN, SAVE, CREATE;
	}

	public enum PlayState {
		PLAY, PAUSE, STOP;
	}

	public enum LabelState {
		CHANGE_SA, SHOW_KEY_LABELS, SHOW_SARGAM_LABELS, CLEAR_KEY_LABELS, CLEAR_SARGAM_LABELS;
	}

	private FileState fileState;
	private PlayState playState;
	private LabelState labelState;
	private SwingPropertyChangeSupport pc;

	public StateModel() {
		pc = new SwingPropertyChangeSupport(this);
	}

	public void setFileState(FileState state) {
		pc.firePropertyChange(state.toString(), this.fileState, state);
		this.fileState = state;
	}

	public void setPlayState(PlayState state) {
		pc.firePropertyChange(state.toString(), this.playState, state);
		this.playState = state;
	}

	public void setLabelState(LabelState state) {
		pc.firePropertyChange(state.toString(), this.labelState, state);
		this.labelState = state;
	}

	public void addListener(PropertyChangeListener listener) {
		pc.addPropertyChangeListener(listener);
	}

	public void registerView(View view) {
		pc.addPropertyChangeListener(view.getPropertyChangeListener());
	}
}
