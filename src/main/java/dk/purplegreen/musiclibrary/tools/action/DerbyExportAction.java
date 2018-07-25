package dk.purplegreen.musiclibrary.tools.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.cache.annotation.CacheResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.AlbumCollection;
import dk.purplegreen.musiclibrary.tools.model.Song;

@Repository
public class DerbyExportAction implements Action {

	private static final String SELECT_ARTIST_SQL = "SELECT id FROM artist WHERE artist_name = ?";

	private static final String INSERT_ARTIST_SQL = "INSERT INTO artist (artist_name) VALUES (?)";

	private static final String INSERT_ALBUM_SQL = "INSERT INTO album (artist_id, album_title, album_year) VALUES (?,?,?)";

	private static final String INSERT_SONG_SQL = "INSERT INTO song (album_id, song_title, track, disc) VALUES (?,?,?,?)";

	private static final Logger log = LogManager.getLogger(DerbyExportAction.class);

	private Unmarshaller unmarshaller;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment environment;

	@Autowired
	public DerbyExportAction(Unmarshaller unmarshaller, JdbcTemplate jdbcTemplate) {
		this.unmarshaller = unmarshaller;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public void execute() throws IOException {

		File albumDir = new File(environment.getRequiredProperty("albumdir"));

		File[] albumCollections = albumDir.listFiles(file -> !file.isDirectory() && file.getName().endsWith(".xml"));
		for (File ac : albumCollections) {

			AlbumCollection albums;
			try (InputStream is = new FileInputStream(ac)) {
				albums = (AlbumCollection) unmarshaller.unmarshal(new StreamSource(is));

				saveAlbums(albums.getAlbums());
			}
		}
	}

	private void saveAlbums(List<Album> albums) {

		for (Album album : albums) {

			Integer artistId = getArtistID(album.getArtist());
/*
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(conn -> {
				PreparedStatement ps = conn.prepareStatement(INSERT_ALBUM_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, artistId);
				ps.setString(2, album.getTitle());
				ps.setInt(3, album.getYear());
				return ps;
			}, keyHolder);

			List<Object[]> args = new ArrayList<>();
			for (Song song : album.getSongs()) {

				args.add(new Object[] { keyHolder.getKey().intValue(), song.getTitle(), song.getTrack(),
						song.getDisc() });
			}

			jdbcTemplate.batchUpdate(INSERT_SONG_SQL, args);
			*/
		}
	}

	@Cacheable("cache")
	public Integer getArtistID(String artist) {
		System.out.println("Artist: "+artist);
		log.debug("Lookup artist: {}", artist);
		return 42;
	}
	
	@Cacheable("cache")
	//@Cacheable
	//@CacheResult(cacheName="cache")
	public Integer getArtistIDZZZ(String artist) {
		
		log.debug("Lookup artist: {}", artist);

		Integer artistId = jdbcTemplate.query(SELECT_ARTIST_SQL, rs -> rs.next() ? rs.getInt("id") : null, artist);
		if (artistId == null) {
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(conn -> {
				PreparedStatement ps = conn.prepareStatement(INSERT_ARTIST_SQL, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, artist);
				return ps;
			}, keyHolder);

			artistId = keyHolder.getKey().intValue();
			log.debug("Created artistId: {}", artistId);
		} else {
			log.debug("Retrieved artistId: {}", artistId);
		}

		log.debug("Returning artistId: {}", artistId);

		return artistId;
	}

}
