package dk.purplegreen.musiclibrary.tools.action;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	@Transactional //Here (instead of super) to avoid required JDBC database connection for JMS/MongoDB
	public void execute() throws IOException {
		super.execute();
	}
	
	protected void saveAlbums(List<Album> albums) {

		log.debug("Saving {} albums", albums.size());
		for (Album album : albums) {
			Integer artistId = dao.getArtistID(album.getArtist());

			dao.saveAlbum(album, artistId);
		}
	}
}
