package dk.purplegreen.musiclibrary.tools;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import dk.purplegreen.musiclibrary.tools.action.Action;
import dk.purplegreen.musiclibrary.tools.action.JDBCImportAction;
import dk.purplegreen.musiclibrary.tools.action.MongoDBImportAction;

public class MusicLibraryToolsSpring {

	private static final Logger log = LogManager.getLogger(MusicLibraryToolsSpring.class);

	public static void main(String[] args) {

		final Options options = configureOptions();

		try {

			@SuppressWarnings("resource")
			AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
			ctx.registerShutdownHook();

			if (log.isDebugEnabled()) {
				log.debug("Beans in context:");
				for (String beanDefinition : ctx.getBeanDefinitionNames()) {
					log.debug(beanDefinition);
				}
			}

			final CommandLineParser commandLineParser = new DefaultParser();

			CommandLine commandLine = commandLineParser.parse(options, args);

			if (args.length == 0 || commandLine.hasOption("h")) {
				showHelp(options);
			} else {
				Optional<Action> action = Optional.empty();

				if (commandLine.hasOption("i")) {

					if ("mongodb".equals(commandLine.getOptionValue("i"))) {
						action = Optional.of(ctx.getBean(MongoDBImportAction.class));
					} else {
						action = Optional.of(ctx.getBean(JDBCImportAction.class));
					}
				} else if (commandLine.hasOption("e")) {
					if ("mongodb".equals(commandLine.getOptionValue("e"))) {
						action = Optional.of(ctx.getBean("mongoDBExportAction", Action.class));
					} else if ("activemq".equals(commandLine.getOptionValue("e"))) {
						action = Optional.of(ctx.getBean("jmsExportAction", Action.class));
					} else {
						action = Optional.of(ctx.getBean("jdbcExportAction", Action.class));
					}
				}

				if (action.isPresent()) {
					action.get().execute();
				}
			}

		} catch (UnrecognizedOptionException | MissingArgumentException e) {
			log.error("Exception caught parsing commandline", e);
			showHelp(options);
		}
		catch (Exception e) {
			log.error("Exception caught in main", e);
			System.out.println("An error occurred - please consult the log for details.");
		}
	}

	private static Options configureOptions() {
		Options options = new Options();

		options.addOption(Option.builder("h").longOpt("help").desc("Print help").build());

		options.addOption(Option.builder("e").longOpt("export")
				.desc("Export to external system [jdbc,mongodb,activemq] from XML").hasArg().argName("target").build());

		options.addOption(Option.builder("i").longOpt("import")
				.desc("Import to XML from external system [jdbc,mongodb]").hasArg().argName("source").build());

		return options;
	}

	private static void showHelp(Options options) {
		final HelpFormatter helpFormatter = new HelpFormatter();

		helpFormatter.printHelp(MusicLibraryToolsSpring.class.getName(), options);
	}
}
