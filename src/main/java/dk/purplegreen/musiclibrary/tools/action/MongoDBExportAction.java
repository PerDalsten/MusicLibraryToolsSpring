package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.DAO;

@Service("mongoDBExportAction")
public class MongoDBExportAction extends ExportAction {

	private static final Logger log = LogManager.getLogger(MongoDBExportAction.class);

	private DAO dao;

	public MongoDBExportAction(Unmarshaller unmarshaller, @Qualifier("mongoDBDAO") DAO dao) {
		super(unmarshaller);
		this.dao = dao;
	}

	@Override
	protected void saveAlbums(List<Album> albums) {

		log.debug("Saving {} albums", albums.size());

		for (Album album : albums) {
			dao.saveAlbum(album);
		}
	}
}
