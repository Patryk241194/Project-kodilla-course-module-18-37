package com.crud.tasks.trello.validator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.crud.tasks.domain.TrelloBoard;
import com.crud.tasks.domain.TrelloCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrelloValidatorTestSuite {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TrelloValidator.class);
    private final TrelloValidator trelloValidator = new TrelloValidator();
    private ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    @BeforeEach
    void setup() {
        listAppender.start();
        LOGGER.addAppender(listAppender);
    }

    @Test
    void validateCardTest() {
        // Given
        TrelloCard trelloCardTest = new TrelloCard("Test card", "Description", "top", "1");
        TrelloCard trelloCardNotTest = new TrelloCard("Real card", "Description", "top", "1");

        // When
        trelloValidator.validateCard(trelloCardTest);
        trelloValidator.validateCard(trelloCardNotTest);

        // Then
        List<ILoggingEvent> logs = listAppender.list;

        assertEquals(2, logs.size());
        assertEquals(Level.INFO, logs.get(0).getLevel());
        assertEquals("Someone is testing my application!", logs.get(0).getMessage());
        assertEquals(Level.INFO, logs.get(1).getLevel());
        assertEquals("Seems that my application is used in proper way.", logs.get(1).getMessage());

    }

    @Test
    void validateTrelloBoardsTest() {
        // Given
        List<TrelloBoard> trelloBoards = Arrays.asList(
                new TrelloBoard("1", "Test board", Arrays.asList()),
                new TrelloBoard("2", "Real board", Arrays.asList())
        );

        // When
        List<TrelloBoard> filteredBoards = trelloValidator.validateTrelloBoards(trelloBoards);

        // Then
        assertEquals(1, filteredBoards.size());
        assertEquals("2", filteredBoards.get(0).getId());
        assertEquals("Real board", filteredBoards.get(0).getName());
    }
}