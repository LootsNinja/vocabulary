package org.nokia.vocabulary.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@ShellTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ComponentScan(basePackages = "org.nokia.vocabulary")
public class VocabularyShellTest {
	@Autowired
	private ShellTestClient shellClient;
	
	@Test
	void testShellCommands() {
		ShellTestClient.InteractiveShellSession session = shellClient.interactive().run();
		
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> ShellAssertions.assertThat(session.screen()).containsText("shell"));
		
		// Simulates typing the "help" command followed by Enter, to display available commands.
		session.write(session.writeSequence().text("help").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> ShellAssertions.assertThat(session.screen()).containsText("AVAILABLE COMMANDS"));
		
		// Adds vocabulary entries using the 'a' command, defining words and their meanings.
		session.write(session.writeSequence().text("a 'search' 'when you look for something'").carriageReturn().build());
		session.write(session.writeSequence().text("a 'apple' 'its a nice fruit'").carriageReturn().build());
		session.write(session.writeSequence().text("a --word 'word' --meaning 'meaning of a word'").carriageReturn().build());
		
		// Searches for the word "search" using the 's' command.
		session.write(session.writeSequence().text("s search").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("when you look for something");
		});
		
		// Searches for the word "word" using the 's' command.
		session.write(session.writeSequence().text("s word").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("meaning of a word");
		});
		
		// Lists all stored words with the 'l' command.
		session.write(session.writeSequence().text("l").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("apple: its a nice fruit");
			ShellAssertions.assertThat(session.screen()).containsText("word: meaning of a word");
		});
		
		// Searches for a word that doesn't exist, "notfound".
		session.write(session.writeSequence().text("s 'notfound'").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("Word not found.");
		});
		
		// Removes the word "search" from the vocabulary using the 'r' command.
		session.write(session.writeSequence().text("r 'search'").carriageReturn().build());
		
		// Replaces the "apple" definition with a new one, simulating user input to confirm the replacement.
		session.write(session.writeSequence().text("a 'apple' 'I like red apples'").carriageReturn().build());
		ByteArrayInputStream yesInput = new ByteArrayInputStream("y".getBytes());
		System.setIn(yesInput);
		
		// Lists the words to confirm the new "apple" definition is stored.
		session.write(session.writeSequence().text("l").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("apple: I like red apples");
		});
		
		// Attempts to add two definitions for the word "letter" and tests conflict resolution.
		session.write(session.writeSequence().text("a 'letter' 'part of alphabet'").carriageReturn().build());
		session.write(session.writeSequence().text("a 'letter' 'something you send in post'").carriageReturn().build());
		ByteArrayInputStream noInput = new ByteArrayInputStream("n".getBytes());
		System.setIn(noInput);
		
		// Lists all words again, ensuring that the first "letter" definition is retained.
		session.write(session.writeSequence().text("l").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("letter: part of alphabet");
		});
		
		// Removes the words "apple", "word", and "letter" from the vocabulary.
		session.write(session.writeSequence().text("r 'apple'").carriageReturn().build());
		session.write(session.writeSequence().text("r 'word'").carriageReturn().build());
		session.write(session.writeSequence().text("r 'letter'").carriageReturn().build());
		
		// Lists all words again, ensuring that no words remain in the vocabulary.
		session.write(session.writeSequence().text("l").carriageReturn().build());
		await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
			ShellAssertions.assertThat(session.screen()).containsText("No words stored.");
		});
	}
}
