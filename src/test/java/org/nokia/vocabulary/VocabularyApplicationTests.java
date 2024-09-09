package org.nokia.vocabulary;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.test.autoconfigure.ShellTest;

@ShellTest
@ComponentScan(basePackages = "org.nokia.vocabulary")
class VocabularyApplicationTests {
	
	@Test
	void contextLoads() {
	}
	
}
