package dk.purplegreen.musiclibrary.tools.model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("album")
@XmlType(propOrder = { "artist", "title", "year", "songs" })
@Document(collection = "albums")
public class Album {
	@Id
	@XStreamOmitField
	/**
	 * Id field for auto-generated MongoDB document id or artistId for JDBC.
	 */
	private String id;
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

	public String getId() {
		return id;
	}

	@XmlTransient
	public void setId(String id) {
		this.id = id;
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
