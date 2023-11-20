package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TrelloMapperTestSuite {

    private TrelloMapper trelloMapper = new TrelloMapper();

    @Test
    void mapToBoards() {
        // Given
        TrelloBoardDto trelloBoardDto = new TrelloBoardDto("1", "Test Board", Collections.emptyList());
        TrelloBoard expectedBoard = new TrelloBoard("1", "Test Board", Collections.emptyList());

        // When
        TrelloBoard actualBoard = trelloMapper.mapToBoards(Collections.singletonList(trelloBoardDto)).get(0);

        // Then
        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    void mapToBoardsDto() {
        // Given
        TrelloBoard trelloBoard = new TrelloBoard("1", "Test Board", Collections.emptyList());
        TrelloBoardDto expectedBoardDto = new TrelloBoardDto("1", "Test Board", Collections.emptyList());

        // When
        TrelloBoardDto actualBoardDto = trelloMapper.mapToBoardsDto(Collections.singletonList(trelloBoard)).get(0);

        // Then
        assertEquals(expectedBoardDto, actualBoardDto);
    }

    @Test
    void mapToList() {
        // Given
        TrelloListDto trelloListDto = new TrelloListDto("1", "Test List", false);
        TrelloList expectedList = new TrelloList("1", "Test List", false);

        // When
        TrelloList actualList = trelloMapper.mapToList(Collections.singletonList(trelloListDto)).get(0);

        // Then
        assertEquals(expectedList, actualList);
    }

    @Test
    void mapToListDto() {
        // Given
        TrelloList trelloList = new TrelloList("1", "Test List", false);
        TrelloListDto expectedListDto = new TrelloListDto("1", "Test List", false);

        // When
        TrelloListDto actualListDto = trelloMapper.mapToListDto(Collections.singletonList(trelloList)).get(0);

        // Then
        assertEquals(expectedListDto, actualListDto);
    }

    @Test
    void mapToCard() {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test Card", "Test Description", "Test pos", "Test listId");
        TrelloCard expectedCard = new TrelloCard("Test Card", "Test Description", "Test pos", "Test listId");

        // When
        TrelloCard actualCard = trelloMapper.mapToCard(trelloCardDto);

        // Then
        assertEquals(expectedCard, actualCard);
    }

    @Test
    void mapToCardDto() {
        // Given
        TrelloCard trelloCard = new TrelloCard("Test Card", "Test Description", "Test pos", "Test listId");
        TrelloCardDto expectedCardDto = new TrelloCardDto("Test Card", "Test Description", "Test pos", "Test listId");

        // When
        TrelloCardDto actualCardDto = trelloMapper.mapToCardDto(trelloCard);

        // Then
        assertEquals(expectedCardDto, actualCardDto);
    }
}