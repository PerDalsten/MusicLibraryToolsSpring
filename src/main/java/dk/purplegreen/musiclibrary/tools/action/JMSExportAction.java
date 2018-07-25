package dk.purplegreen.musiclibrary.tools.action;

import java.util.List;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.purplegreen.musiclibrary.tools.model.Album;
import dk.purplegreen.musiclibrary.tools.model.SongInfo;

@Repository("jmsExportAction")
public class JMSExportAction extends ExportAction {

	private JmsTemplate jmsTemplate;

	public JMSExportAction(Unmarshaller unmarshaller, JmsTemplate jmsTemplate) {
		super(unmarshaller);
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	protected void saveAlbums(List<Album> albums) {
		for (Album album : albums) {
			album.getSongs().stream().forEach(song -> jmsTemplate.send(session -> {
				try {
					return session.createTextMessage(
							new ObjectMapper().writeValueAsString(new SongInfo(album.getArtist(), album.getTitle(),
									album.getYear(), song.getTitle(), song.getTrack(), song.getDisc())));
				} catch (JsonProcessingException e) {
					throw new RuntimeException("JSON error", e);
				}
			}));
		}
	}
}
