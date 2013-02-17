package gsingh.learnkirtan.shabad;

import java.io.Serializable;

public class ShabadMetaData implements Serializable {

	private static final long serialVersionUID = -774359963608200387L;

	private String name;
	private String taal;
	private String raag;
	private String ang;
	private String notes;

	public ShabadMetaData(String name, String taal, String raag, String ang,
			String notes) {
		this.name = name;
		this.taal = taal;
		this.raag = raag;
		this.ang = ang;
		this.notes = notes;
	}

	public String getName() {
		return name;
	}

	public String getTaal() {
		return taal;
	}

	public String getRaag() {
		return raag;
	}

	public String getAng() {
		return ang;
	}

	public String getNotes() {
		return notes;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ShabadMetaData) {
			ShabadMetaData data = (ShabadMetaData) o;
			if (name.equals(data.name) && taal.equals(data.taal)
					&& raag.equals(data.raag) && ang.equals(data.ang)
					&& notes.equals(data.notes)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
