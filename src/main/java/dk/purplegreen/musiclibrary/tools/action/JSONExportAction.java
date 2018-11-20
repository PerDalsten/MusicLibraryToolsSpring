package dk.purplegreen.musiclibrary.tools.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.Song;

@Service("jsonExportAction")
public class JSONExportAction extends ExportAction {

	@Autowired
	private Environment environment;

	@Autowired
	private ObjectMapper mapper;

	protected JSONExportAction(Unmarshaller unmarshaller) {
		super(unmarshaller);
	}

	@Override
	protected void saveAlbums(List<Album> albums) {

		Map<String, JSONArtist> artists = new HashMap<>();
		List<JSONAlbum> jAlbums = new ArrayList<>();

		long jArtistId = 0;
		long jAlbumId = 0;
		long jSongId = 0;

		for (Album album : albums) {

			JSONArtist jArtist = artists.get(album.getArtist());
			if (jArtist == null) {
				jArtist = new JSONArtist(++jArtistId, album.getArtist());
				artists.put(jArtist.name, jArtist);
			}

			List<JSONSong> jSongs = new ArrayList<>();
			for (Song song : album.getSongs()) {

				JSONSong jSong = new JSONSong(++jSongId, song.getTitle(), song.getTrack(), song.getDisc());
				jSongs.add(jSong);
			}

			JSONAlbum jAlbum = new JSONAlbum(++jAlbumId, jArtist, album.getTitle(), album.getYear(), jSongs);

			jAlbums.add(jAlbum);
		}

		try {
			File albumFile = new File(new File(environment.getRequiredProperty("albumdir")), "albums.js");
			
			mapper.writeValue(albumFile, jAlbums);

		} catch (IOException e) {
			throw new RuntimeException("JSON save error", e);
		}
	}

	public static class JSONArtist {
		public long id;
		public String name;

		JSONArtist(long id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	public static class JSONAlbum {
		public long id;
		public JSONArtist artist;
		public String title;
		public int year;
		public List<JSONSong> songs;

		JSONAlbum(long id, JSONArtist artist, String title, int year, List<JSONSong> songs) {
			this.id = id;
			this.artist = artist;
			this.title = title;
			this.year = year;
			this.songs = songs;
		}
	}

	public static class JSONSong {
		public long id;
		public String title;
		public int track;
		public int disc;

		JSONSong(long id, String title, int track, int disc) {
			this.id = id;
			this.title = title;
			this.track = track;
			this.disc = disc;
		}
	}

}
