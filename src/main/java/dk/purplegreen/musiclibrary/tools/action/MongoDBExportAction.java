package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.MongoDBDAO;

@Service("mongoDBExportAction")
public class MongoDBExportAction extends ExportAction {

	private static final Logger log = LogManager.getLogger(MongoDBExportAction.class);

	private MongoDBDAO mongoDBDAO;

	public MongoDBExportAction(Unmarshaller unmarshaller, MongoDBDAO mongoDBDAO) {
		super(unmarshaller);
		this.mongoDBDAO = mongoDBDAO;
	}

	@Override
	protected void saveAlbums(List<Album> albums) {

		log.debug("Saving {} albums", albums.size());

		for (Album album : albums) {
			mongoDBDAO.saveAlbum(album);
		}
	}
}
