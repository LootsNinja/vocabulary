package org.nokia.vocabulary.services;

import java.util.Map;

/**
 * Service interface for managing vocabulary operations.
 * Provides methods for adding, updating, searching, removing words, and saving the vocabulary to a file.
 */
public interface VocabularyService {
	/**
	 * Retrieves all stored words and their meanings.
	 *
	 * @return A map of words and their meanings.
	 */
	Map<String, String> getAllWords();
	
	/**
	 * Adds a new word or updates an existing word in the vocabulary.
	 *
	 * @param word    The word to add or update.
	 * @param meaning The meaning of the word.
	 * @return A confirmation message indicating whether the word was added or updated.
	 */
	String addOrUpdateWord(String word, String meaning);
	
	/**
	 * Searches for a word in the vocabulary.
	 *
	 * @param word The word to search for.
	 * @return The meaning of the word, or null if the word is not found.
	 */
	String searchWord(String word);
	
	/**
	 * Removes a word from the vocabulary.
	 *
	 * @param word The word to remove.
	 * @return True if the word was removed, false if the word was not found.
	 */
	boolean removeWord(String word);
	
	/**
	 * Saves the vocabulary to a JSON file.
	 */
	void saveVocabularyToFile();
}
