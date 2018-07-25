package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.JDBCDAO;

@Service("jdbcExportAction")
public class JDBCExportAction extends ExportAction {

	private static final Logger log = LogManager.getLogger(JDBCExportAction.class);

	private JDBCDAO dao;

	@Autowired
	public JDBCExportAction(Unmarshaller unmarshaller, JDBCDAO dao) {
		super(unmarshaller);
		this.dao = dao;
	}

	protected void saveAlbums(List<Album> albums) {

		log.debug("Saving {} albums", albums.size());
		for (Album album : albums) {
			Integer artistId = dao.getArtistID(album.getArtist());

			dao.saveAlbum(album, artistId);
		}
	}
}
