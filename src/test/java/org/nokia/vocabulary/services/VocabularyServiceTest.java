package org.nokia.vocabulary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nokia.vocabulary.file.FileHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VocabularyServiceTest {
	
	@Mock
	private FileHandler fileHandler;
	
	@InjectMocks
	private VocabularyServiceImpl vocabularyService;
	
	private Map<String, String> vocabularyMap;
	
	@BeforeEach
	void setUp() throws IOException {
		MockitoAnnotations.openMocks(this);
		vocabularyMap = new TreeMap<>();
		vocabularyMap.put("apple", "A fruit");
		when(fileHandler.loadVocabulary()).thenReturn(vocabularyMap);
		vocabularyService = new VocabularyServiceImpl(fileHandler);
	}
	
	@Test
	void testGetAllWords() {
		Map<String, String> words = vocabularyService.getAllWords();
		assertEquals(1, words.size());
		assertTrue(words.containsKey("apple"));
		assertEquals("A fruit", words.get("apple"));
	}
	
	@Test
	void testAddWord() {
		String result = vocabularyService.addOrUpdateWord("banana", "A yellow fruit");
		assertEquals("Word added: banana", result);
		assertEquals(2, vocabularyService.getAllWords().size());
		assertEquals("A yellow fruit", vocabularyService.getAllWords().get("banana"));
	}
	
	@Test
	void testUpdateWord() {
		mockUserInput("yes");
		String result = vocabularyService.addOrUpdateWord("apple", "A sweet red fruit");
		assertEquals("Word 'apple' has been updated.", result);
		assertEquals("A sweet red fruit", vocabularyService.getAllWords().get("apple"));
	}
	
	@Test
	void testDoNotUpdateWord() {
		mockUserInput("no");
		String result = vocabularyService.addOrUpdateWord("apple", "A sweet red fruit");
		assertEquals("Word 'apple' was not updated.", result);
		assertEquals("A fruit", vocabularyService.getAllWords().get("apple"));
	}
	
	@Test
	void testInvalidInput() {
		mockUserInput("invalid");
		String result = vocabularyService.addOrUpdateWord("apple", "A sweet red fruit");
		assertEquals("Invalid input. Please enter 'yes', 'y', 'Y', 'no', or 'n', 'N'.", result);
		assertEquals("A fruit", vocabularyService.getAllWords().get("apple"));
	}
	
	@Test
	void testSearchWordFound() {
		String result = vocabularyService.searchWord("apple");
		assertEquals("A fruit", result);
	}
	
	@Test
	void testSearchWordNotFound() {
		String result = vocabularyService.searchWord("orange");
		assertNull(result);
	}
	
	@Test
	void testRemoveWordSuccess() {
		boolean result = vocabularyService.removeWord("apple");
		assertTrue(result);
		assertEquals(0, vocabularyService.getAllWords().size());
	}
	
	@Test
	void testRemoveWordNotFound() {
		boolean result = vocabularyService.removeWord("orange");
		assertFalse(result);
		assertEquals(1, vocabularyService.getAllWords().size());
	}
	
	@Test
	void testSaveVocabularyToFile() throws IOException {
		vocabularyService.saveVocabularyToFile();
		verify(fileHandler, times(1)).saveVocabulary(anyMap());
	}
	
	@Test
	void testSaveVocabularyToFileException() throws IOException {
		doThrow(new IOException("File save error")).when(fileHandler).saveVocabulary(anyMap());
		vocabularyService.saveVocabularyToFile();
		verify(fileHandler, times(1)).saveVocabulary(anyMap());
	}
	
	private void mockUserInput(String input) {
		Scanner mockScanner = mock(Scanner.class);
		when(mockScanner.nextLine()).thenReturn(input);
		System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
	}
}
