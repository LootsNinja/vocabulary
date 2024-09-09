package org.nokia.vocabulary.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Handles loading and saving of the vocabulary to and from a JSON file.
 */
@Component
public class FileHandler {
	@Value("${output.file.path}")
	private String FILE_PATH;
	
	private static final Logger logger = LogManager.getLogger(FileHandler.class);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Loads the vocabulary from the JSON file.
	 *
	 * @return A map of words and their meanings, or an empty map if the file does not exist.
	 * @throws IOException If an I/O error occurs during file reading.
	 */
	public Map<String, String> loadVocabulary() throws IOException {
		File file = new File(FILE_PATH);
		if (file.exists()) {
			logger.debug("Loading vocabulary from file: " + FILE_PATH);
			return objectMapper.readValue(file, new TypeReference<Map<String, String>>() {
			});
		} else {
			logger.warn("Vocabulary file not found. Starting with empty vocabulary.");
			return Map.of();
		}
	}
	
	/**
	 * Saves the given vocabulary to the JSON file.
	 *
	 * @param vocabularyMap The map containing words and their meanings to save.
	 * @throws IOException If an I/O error occurs during file writing.
	 */
	public void saveVocabulary(Map<String, String> vocabularyMap) throws IOException {
		logger.debug("Saving vocabulary to file: {}", FILE_PATH);
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), vocabularyMap);
		logger.debug("Vocabulary saved successfully.");
	}
}
