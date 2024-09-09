package org.nokia.vocabulary;

import org.jline.reader.LineReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.Shell;
import org.springframework.shell.context.ShellContext;
import org.springframework.shell.jline.InteractiveShellRunner;
import org.springframework.shell.jline.PromptProvider;

/**
 * Main entry point for the Vocabulary Application.
 * Extends the InteractiveShellRunner to provide an interactive Spring Shell environment.
 */
@SpringBootApplication
public class VocabularyApplication extends InteractiveShellRunner {
	
	/**
	 * Constructs the VocabularyApplication with dependencies for interactive shell operation.
	 */
	public VocabularyApplication(LineReader lineReader, PromptProvider promptProvider, Shell shell, ShellContext shellContext) {
		super(lineReader, promptProvider, shell, shellContext);
	}
	
	/**
	 * Main method that starts the Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(VocabularyApplication.class, args);
	}
	
}
