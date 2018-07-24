package dk.purplegreen.musiclibrary.tools.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Repository;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.AlbumCollection;
import dk.purplegreen.musiclibrary.tools.model.Song;

@Repository
public class DerbyImportAction implements Action {

	private static final String SELECT_ALBUMS_SQL = "SELECT album.id AS id, album.album_title AS title, album.album_year AS yr, artist.artist_name AS artist "
			+ "FROM album " + "JOIN artist ON artist.id=album.artist_id " + "ORDER BY artist, yr";

	private static final String SELECT_SONGS_SQL = "SELECT album_id, song_title AS title, track, disc FROM song ORDER BY album_id, disc, track";

	private static final Logger log = LogManager.getLogger(DerbyImportAction.class);

	private Marshaller marshaller;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private Environment environment;

	@Autowired
	public DerbyImportAction(Marshaller marshaller, JdbcTemplate jdbcTemplate) {
		this.marshaller = marshaller;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void execute() throws IOException {

		Map<Integer, Album> albumMap = new HashMap<>();

		AlbumCollection albums = new AlbumCollection(jdbcTemplate.query(SELECT_ALBUMS_SQL, new RowMapper<Album>() {
			@Override
			public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
				Album album = new Album(rs.getString("artist"), rs.getString("title"), rs.getInt("yr"));
				albumMap.put(rs.getInt("id"), album);

				log.debug("Adding album: {}", album);
				return album;
			}
		}));

		jdbcTemplate.query(SELECT_SONGS_SQL, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Album album = albumMap.get(rs.getInt("album_id"));
				album.getSongs().add(new Song(rs.getString("title"), rs.getInt("track"), rs.getInt("disc")));
			}
		});

		String fileName = String.join("", environment.getRequiredProperty("albumdir"), "/albums_",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")), ".xml");

		try (OutputStream os = new FileOutputStream(fileName)) {
			marshaller.marshal(albums, new StreamResult(os));
		}
	}
}
