package org.nokia.vocabulary.cli;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nokia.vocabulary.services.VocabularyService;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

/**
 * Command-line interface for managing vocabulary through Spring Shell.
 * Provides commands for listing, adding, searching, removing words, and exiting the application.
 */
@ShellComponent
@Slf4j
@RequiredArgsConstructor
public class VocabularyCommands {
	private final VocabularyService vocabularyService;
	
	/**
	 * Lists all stored words with their meanings.
	 *
	 * @return A string containing all words and their meanings.
	 */
	@ShellMethod(value = "List all stored words with their meanings.", key = {"l", "list"})
	public String listAllWords() {
		Map<String, String> vocabularyMap = vocabularyService.getAllWords();
		if (vocabularyMap.isEmpty()) {
			return "No words stored.";
		}
		StringBuilder builder = new StringBuilder();
		vocabularyMap.forEach((word, meaning) -> builder.append(word).append(": ").append(meaning).append("\n"));
		return builder.toString();
	}
	
	/**
	 * Adds a new word with its meaning to the vocabulary.
	 *
	 * @param word    The word to add.
	 * @param meaning The meaning of the word.
	 * @return A confirmation message or error if inputs are invalid.
	 */
	@ShellMethod(value = "Add a new word with its meaning.", key = {"a", "add"})
	public String addWord(@ShellOption(help = "The word to add") String word,
						  @ShellOption(help = "The meaning of the word") String meaning) {
		if (word == null || word.isEmpty() || meaning == null || meaning.isEmpty()) {
			return "Please enter a word and its meaning to add.\na '[word]' '[meaning]'";
		}
		return vocabularyService.addOrUpdateWord(word, meaning);
	}
	
	/**
	 * Searches for a word in the vocabulary and displays its meaning.
	 *
	 * @param word The word to search for.
	 * @return The word and its meaning, or a message if the word is not found.
	 */
	@ShellMethod(value = "Search for a word and display its meaning.", key = {"s", "search"})
	public String searchWord(@ShellOption(help = "The word to search for") String word) {
		if (word == null || word.isEmpty()) {
			return "Please enter a word to search for its meaning.\ns '[word]'";
		}
		String meaning = vocabularyService.searchWord(word);
		if (meaning != null) {
			log.debug("Found word: {}", word);
			return word + ": " + meaning;
		} else {
			log.warn("Word not found: {}", word);
			return "Word not found.";
		}
	}
	
	/**
	 * Removes a word from the vocabulary.
	 *
	 * @param word The word to remove.
	 * @return A confirmation message or an error if the word is not found.
	 */
	@ShellMethod(value = "Remove a word from the vocabulary.", key = {"r", "remove"})
	public String removeWord(@ShellOption(help = "The word to remove") String word) {
		if (word == null || word.isEmpty()) {
			return "Please enter a word to remove.\nr '[word]'";
		}
		boolean result = vocabularyService.removeWord(word);
		if (result) {
			log.debug("Removed word: {}", word);
			return "Word removed: " + word;
		} else {
			log.warn("Word not found: {}", word);
			return "Word not found.";
		}
	}
	
	/**
	 * Saves the vocabulary to a file and quits the application.
	 *
	 * @return An empty string (the application quits before returning).
	 */
	@ShellMethod(value = "Save the vocabulary and quit the application.", key = {"q", "quit"})
	public String quitApplication() {
		System.out.println("Saving vocabulary...");
		vocabularyService.saveVocabularyToFile();
		System.out.println("Exiting...");
		System.exit(0);
		return "";
	}
}
