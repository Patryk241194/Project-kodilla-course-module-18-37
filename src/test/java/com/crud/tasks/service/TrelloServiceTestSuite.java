package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.*;
import com.crud.tasks.trello.client.TrelloClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrelloServiceTestSuite {

    @InjectMocks
    private TrelloService trelloService;
    @Mock
    private TrelloClient trelloClient;
    @Mock
    private SimpleEmailService emailService;
    @Mock
    private AdminConfig adminConfig;

    @Test
    void shouldFetchTrelloBoards() {
        // Given
        List<TrelloBoardDto> expectedBoards = List.of(
                new TrelloBoardDto("1", "Board 1", List.of(new TrelloListDto("1", "List 1", false))),
                new TrelloBoardDto("2", "Board 2", List.of(new TrelloListDto("2", "List 2", true)))
        );

        when(trelloClient.getTrelloBoards()).thenReturn(expectedBoards);

        // When
        List<TrelloBoardDto> actualBoards = trelloService.fetchTrelloBoards();

        // Then
        assertNotNull(actualBoards);
        assertEquals(actualBoards.size(),2);
    }

    @Test
    void shouldCreateCardAndSendEmail() {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("TestCard", "TestDescription", "top", "1");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("testId", "TestCard", "http://test-url.com");

        when(trelloClient.createNewCard(trelloCardDto)).thenReturn(createdTrelloCardDto);
        when(adminConfig.getAdminMail()).thenReturn("admin@example.com");

        // When
        CreatedTrelloCardDto newCard = trelloService.createTrelloCard(trelloCardDto);

        // Then
        assertNotNull(newCard);
        assertEquals("testId", newCard.getId());
        assertEquals("TestCard", newCard.getName());
        assertEquals("http://test-url.com", newCard.getShortUrl());
        verify(emailService, times(1)).send(any(Mail.class));
    }

    @Test
    void shouldNotSendEmailIfCardCreationFails() {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("TestCard", "TestDescription", "top", "1");

        when(trelloClient.createNewCard(trelloCardDto)).thenReturn(null);

        // When
        CreatedTrelloCardDto newCard = trelloService.createTrelloCard(trelloCardDto);

        // Then
        assertNull(newCard);
        verify(emailService, times(0)).send(any(Mail.class));
    }
}