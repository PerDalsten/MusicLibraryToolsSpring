package dk.purplegreen.musiclibrary.tools.persistence;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import dk.purplegreen.musiclibrary.tools.model.Album;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class JDBCDAOTest {

	@Autowired
	private JDBCDAO dao;

	@Test
	public void testSaveAlbum() {

		Album album = new Album("The Beatles", "Revolver", 1965);
		album.setId("1");

		dao.saveAlbum(album);

		List<Album> albums = dao.getAlbums();

		assertEquals("Wrong number of albums", 4, albums.size());
		assertEquals("Wrong sorting of albums", "Revolver", albums.get(2).getTitle());

	}

	@Test
	public void testGetArtistId() {

		assertEquals("Wrong artist id", new Integer(2), dao.getArtistID("Royal Hunt"));
	}

	@Test
	public void testGetAlbums() {

		List<Album> albums = dao.getAlbums();

		assertEquals("Wrong number of albums", 3, albums.size());
		assertEquals("Wrong sorting of albums", "AC/DC", albums.get(0).getArtist());

	}

}
