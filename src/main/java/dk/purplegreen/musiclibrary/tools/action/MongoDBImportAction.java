package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Service;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.persistence.MongoDBDAO;

@Service("mongoDBImportAction")
public class MongoDBImportAction extends ImportAction {

	private static final Logger log = LogManager.getLogger(MongoDBImportAction.class);

	private MongoDBDAO mongoDBDAO;

	public MongoDBImportAction(Marshaller marshaller, MongoDBDAO mongoDBDAO) {
		super(marshaller);
		this.mongoDBDAO = mongoDBDAO;
	}

	@Override
	protected List<Album> getAlbums() {
		return mongoDBDAO.getAllAlbums();
	}

}
