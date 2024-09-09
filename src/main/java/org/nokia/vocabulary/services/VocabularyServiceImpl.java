package org.nokia.vocabulary.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nokia.vocabulary.file.FileHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Implementation of the VocabularyService interface for managing vocabulary.
 * Supports adding, updating, searching, removing words, and saving the vocabulary to a JSON file.
 */
@Service
public class VocabularyServiceImpl implements VocabularyService {
	private static final Logger logger = LogManager.getLogger(VocabularyServiceImpl.class);
	private final Map<String, String> vocabularyMap = new TreeMap<>();
	private final FileHandler fileHandler;
	
	/**
	 * Constructs a VocabularyServiceImpl with a FileHandler for loading and saving vocabulary.
	 *
	 * @param fileHandler The file handler used for loading and saving vocabulary data.
	 */
	public VocabularyServiceImpl(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
		try {
			// Load initial data from JSON file
			Map<String, String> initialData = fileHandler.loadVocabulary();
			vocabularyMap.putAll(initialData);
			logger.debug("Loaded vocabulary with {} entries.", vocabularyMap.size());
		} catch (IOException e) {
			logger.error("Error loading vocabulary: {}", e.getMessage());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getAllWords() {
		logger.debug("Listing all words.");
		return vocabularyMap;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String addOrUpdateWord(String word, String meaning) {
		if (vocabularyMap.containsKey(word)) {
			// Word already exists, ask user if they want to replace it
			System.out.println("The word - " + word + " - already exists with the meaning: " + vocabularyMap.get(word));
			System.out.println("Do you want to replace it? (Y)es/(N)o");
			
			Scanner scanner = new Scanner(System.in);
			String response = scanner.nextLine().trim().toLowerCase();
			
			// Allow both "yes", "y", "no", "n" (case-insensitive)
			if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
				vocabularyMap.put(word, meaning);
				logger.debug("Replaced word: {}", word);
				return "Word '" + word + "' has been updated.";
			} else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
				logger.debug("Kept old definition for word: {}", word);
				return "Word '" + word + "' was not updated.";
			} else {
				logger.warn("Invalid input: {}", response);
				return "Invalid input. Please enter 'yes', 'y', 'Y', 'no', or 'n', 'N'.";
			}
		} else {
			// Word does not exist, add it
			vocabularyMap.put(word, meaning);
			logger.debug("Added new word: {}", word);
			return "Word added: " + word;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String searchWord(String word) {
		String meaning = vocabularyMap.get(word);
		if (meaning != null) {
			logger.debug("Found word: {}", word);
		} else {
			logger.warn("Word not found: {}", word);
		}
		return meaning;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeWord(String word) {
		if (vocabularyMap.remove(word) != null) {
			logger.debug("Removed word: {}", word);
			return true;
		} else {
			logger.warn("Attempted to remove non-existing word: {}", word);
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveVocabularyToFile() {
		try {
			fileHandler.saveVocabulary(vocabularyMap);
			logger.debug("Vocabulary saved to file.");
		} catch (IOException e) {
			logger.error("Error saving vocabulary: {}", e.getMessage());
		}
	}
}
