package dk.purplegreen.musiclibrary.tools.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.oxm.Marshaller;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.AlbumCollection;

public abstract class ImportAction implements Action {

	@Autowired
	private Environment environment;

	private Marshaller marshaller;

	protected ImportAction(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	@Override
	public void execute() throws IOException {
		AlbumCollection albums = new AlbumCollection(getAlbums());

		String fileName = String.join("", environment.getRequiredProperty("albumdir"), "/albums_",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")), ".xml");

		try (OutputStream os = new FileOutputStream(fileName)) {
			marshaller.marshal(albums, new StreamResult(os));
		}
	}

	protected abstract List<Album> getAlbums();

}
