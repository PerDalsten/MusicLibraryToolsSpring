package dk.purplegreen.musiclibrary.tools.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.Song;

@Repository
public class JDBCDAO {

	private static final String SELECT_ARTIST_SQL = "SELECT id FROM artist WHERE artist_name = ?";

	private static final String INSERT_ARTIST_SQL = "INSERT INTO artist (artist_name) VALUES (?)";

	private static final String INSERT_ALBUM_SQL = "INSERT INTO album (artist_id, album_title, album_year) VALUES (?,?,?)";

	private static final String INSERT_SONG_SQL = "INSERT INTO song (album_id, song_title, track, disc) VALUES (?,?,?,?)";

	private static final String SELECT_ALBUMS_SQL = "SELECT album.id AS id, album.album_title AS title, album.album_year AS yr, artist.artist_name AS artist "
			+ "FROM album " + "JOIN artist ON artist.id=album.artist_id " + "ORDER BY artist, yr";

	private static final String SELECT_SONGS_SQL = "SELECT album_id, song_title AS title, track, disc FROM song ORDER BY album_id, disc, track";

	private static final Logger log = LogManager.getLogger(JDBCDAO.class);

	private JdbcTemplate jdbcTemplate;

	public JDBCDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Cacheable("artistid-cache")
	public Integer getArtistID(String artist) {

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

	public void saveAlbum(Album album, Integer artistId) {
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

			args.add(new Object[] { keyHolder.getKey().intValue(), song.getTitle(), song.getTrack(), song.getDisc() });
		}

		jdbcTemplate.batchUpdate(INSERT_SONG_SQL, args);
	}

	public List<Album> getAlbums() {
		Map<Integer, Album> albumMap = new HashMap<>();

		List<Album> albums = jdbcTemplate.query(SELECT_ALBUMS_SQL, new RowMapper<Album>() {
			@Override
			public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
				Album album = new Album(rs.getString("artist"), rs.getString("title"), rs.getInt("yr"));
				albumMap.put(rs.getInt("id"), album);

				log.debug("Adding album: {}", album);
				return album;
			}
		});

		jdbcTemplate.query(SELECT_SONGS_SQL, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Album album = albumMap.get(rs.getInt("album_id"));
				album.getSongs().add(new Song(rs.getString("title"), rs.getInt("track"), rs.getInt("disc")));
			}
		});

		return albums;
	}
}
