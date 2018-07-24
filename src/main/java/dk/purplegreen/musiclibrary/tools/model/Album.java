package dk.purplegreen.musiclibrary.tools.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("album")
@XmlType(propOrder = { "artist", "title", "year", "songs" })
public class Album {
	private String title;
	private String artist;
	private int year;

	public Album() {
	}

	public Album(String artist, String title, int year) {
		this.artist = artist;
		this.title = title;
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	private List<Song> songs = new ArrayList<>();

	@XmlElementWrapper(name = "songs")
	@XmlElement(name = "song")
	public List<Song> getSongs() {
		return songs;
	}

	@Override
	public String toString() {

		StringJoiner songList = new StringJoiner(", ", "[", "]");
		songs.forEach(song -> songList.add(song.toString()));

		return String.join(", ", title, artist, Integer.toString(year), songList.toString());
	}
}
