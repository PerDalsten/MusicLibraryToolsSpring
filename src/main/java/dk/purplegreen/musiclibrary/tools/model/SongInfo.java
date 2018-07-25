package dk.purplegreen.musiclibrary.tools.model;

public class SongInfo {
	private String artist;
	private String album;
	private Integer year;
	private String title;
	private Integer track;
	private Integer disc;

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public Integer getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	public Integer getTrack() {
		return track;
	}

	public Integer getDisc() {
		return disc;
	}

	public SongInfo(String artist, String album, Integer year, String title, Integer track, Integer disc) {
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.title = title;
		this.track = track;
		if (disc == null)
			this.disc = 1;
		else
			this.disc = disc;
	}
}
