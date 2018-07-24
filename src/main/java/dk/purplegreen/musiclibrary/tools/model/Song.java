package dk.purplegreen.musiclibrary.tools.model;

import javax.xml.bind.annotation.XmlType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("song")
@XmlType(propOrder = { "track", "title", "disc" })
public class Song {
	private String title;
	private int track;
	private int disc = 1;

	public Song() {
	}

	public Song(String title, int track) {
		this.title = title;
		this.track = track;
	}

	public Song(String title, int track, int disc) {
		this.title = title;
		this.track = track;
		this.disc = disc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTrack() {
		return track;
	}

	public void setTrack(int track) {
		this.track = track;
	}

	public int getDisc() {
		return disc;
	}

	public void setDisc(int disc) {
		this.disc = disc;
	}
	
	@Override
	public String toString() {
		return String.join(". ", Integer.toString(track), title);
	}
}
