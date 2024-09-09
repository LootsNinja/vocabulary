# Vocabulary Management Shell Application

This application is part of Nokia's recruitment process. It allows users to manage a vocabulary through an interactive
shell environment. Users can add, search, list, and remove words and their meanings, all while interacting through a
Spring Shell-based interface.

It's built using Java 17, Spring Shell, Maven, and tested with JUnit 5 and Mockito.

## Prerequisites

- Java JDK 17 or newer.
- Maven.
- Enable annotation processing in your IDE.

## Setting Up the Application

**Install Dependencies**

Run the following command in the root directory of the project to install the necessary dependencies:

```bash
./mvnw clean install
```

## Running the Application

To run the application, use the Intellij IDEA run configuration stored in the repo, which will launch the Spring Shell
environment.

Alternatively, you can run the application directly using the following Maven command:

```bash
./mvnw spring-boot:run
```

## Available Commands

Here is a list of the commands supported by the application:

#### 1. Add a word with its meaning

 ```bash
a 'word' 'meaning'
```

Example:

 ```bash
a 'apple' 'A common fruit'
```

#### 2. Search for a word

```bash
s word
```

Example:

```bash
s apple
```

#### 3. List all stored words

```bash
l
```

#### 4. List all stored words

```bash
r [word]
```

Example

```bash
r apple
```

#### 5. Quit the application and save dictionary to a file

```bash
q
```

# Notes

* The application currently supports a basic set of features for vocabulary management.
* Error handling includes cases where the word already exists or cannot be found in the vocabulary.
* To write long words or meanings with spaces, have them between quotation marks '' / ""
* if you need to have words that has the apostrophe, add the word or the meaning between double quotation ""
* The vocabulary data is stored in a JSON file named vocabulary.json in the root directory of the application.
* you can use short or long versions of the commands:
    * a/add, s/search, r/remove, q/quit, l/list
    * a --word [word] --meaning [meaning]
    * s --word [word]