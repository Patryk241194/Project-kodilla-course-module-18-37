package com.crud.tasks.trello.client;

import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.config.TrelloConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrelloClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloClient.class);
    private final RestTemplate restTemplate;
    private final TrelloConfig trelloConfig;


    private URI buildTrelloApiUrl(String path, MultiValueMap<String, String> queryParams) {
        return UriComponentsBuilder.fromHttpUrl(trelloConfig.getTrelloApiEndpoint() + path)
                .queryParams(queryParams)
                .build()
                .encode()
                .toUri();
    }

    public List<TrelloBoardDto> getTrelloBoards() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("key", trelloConfig.getTrelloAppKey());
        queryParams.add("token", trelloConfig.getTrelloToken());
        queryParams.add("fields", "name,id");
        queryParams.add("lists", "all");

        URI url = buildTrelloApiUrl("/members/" + trelloConfig.getTrelloUsername() + "/boards", queryParams);

        try {
            TrelloBoardDto[] boardsResponse = restTemplate.getForObject(url, TrelloBoardDto[].class);
            return Optional.ofNullable(boardsResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(p -> Objects.nonNull(p.getId()) && Objects.nonNull(p.getName()))
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public CreatedTrelloCard createNewCard(TrelloCardDto trelloCardDto) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("key", trelloConfig.getTrelloAppKey());
        queryParams.add("token", trelloConfig.getTrelloToken());
        queryParams.add("name", trelloCardDto.getName());
        queryParams.add("desc", trelloCardDto.getDescription());
        queryParams.add("pos", trelloCardDto.getPos());
        queryParams.add("idList", trelloCardDto.getListId());

        URI url = buildTrelloApiUrl("/cards", queryParams);

        return restTemplate.postForObject(url, null, CreatedTrelloCard.class);
    }
}
