package com.crud.tasks.controller;

import com.crud.tasks.domain.CreatedTrelloCardDto;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.domain.TrelloListDto;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@WebMvcTest(TrelloController.class)
class TrelloControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TrelloFacade trelloFacade;

    @Test
    void shouldFetchEmptyTrelloBoards() throws Exception {
        // Given
        when(trelloFacade.fetchTrelloBoards()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/v1/trello/boards").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    void shouldFetchTrelloBoards() throws Exception {
        // Given
        List<TrelloListDto> trelloLists = List.of(new TrelloListDto("1", "Test list", false));
        List<TrelloBoardDto> trelloBoards = List.of(new TrelloBoardDto("1", "Test Task", trelloLists));
        when(trelloFacade.fetchTrelloBoards()).thenReturn(trelloBoards);

        // When & Then
        mockMvc.perform(get("/v1/trello/boards").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("Test Task")))
                .andExpect(jsonPath("$[0].lists", hasSize(1)))
                .andExpect(jsonPath("$[0].lists[0].id", is("1")))
                .andExpect(jsonPath("$[0].lists[0].name", is("Test list")))
                .andExpect(jsonPath("$[0].lists[0].closed", is(false)));

    }

    @Test
    void shouldCreateTrelloCard() throws Exception {
        // Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("Test", "Test description", "top", "1");
        CreatedTrelloCardDto createdTrelloCardDto = new CreatedTrelloCardDto("232", "Test", "http://test.com");

        when(trelloFacade.createCard(trelloCardDto)).thenReturn(createdTrelloCardDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(trelloCardDto);

        // When & Then
        mockMvc.perform(post("/v1/trello/cards")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(jsonPath("$.id", is("232")))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.shortUrl", is("http://test.com")));


    }

}