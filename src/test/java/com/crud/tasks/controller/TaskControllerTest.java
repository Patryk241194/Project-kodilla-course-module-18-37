package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DbService service;
    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldFetchAllTasks() throws Exception {
        // Given
        List<TaskDto> tasksDtos = List.of(new TaskDto(1L, "Test Task", "Test Content"));
        List<Task> tasks = List.of(new Task(1L, "Test Task", "Test Content"));
        when(service.getAllTasks()).thenReturn(tasks);
        when(taskMapper.mapToTaskDtoList(tasks)).thenReturn(tasksDtos);

        // When & Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Test Task")))
                .andExpect(jsonPath("$[0].content", is("Test Content")));

    }

    @Test
    void shouldFetchTask() throws Exception {
        // Given
        List<TaskDto> tasksDtos = List.of(new TaskDto(1L, "Test Task 1", "Test Content 1"),
                new TaskDto(2L, "Test Task 2", "Test Content 2"));
        List<Task> tasks = List.of(new Task(1L, "Test Task 1", "Test Content 1"),
                new Task(2L, "Test Task 2", "Test Content 2"));
        when(service.getTask(1L)).thenReturn(tasks.get(0));
        when(service.getTask(2L)).thenReturn(tasks.get(1));
        when(taskMapper.mapToTaskDto(tasks.get(0))).thenReturn(tasksDtos.get(0));
        when(taskMapper.mapToTaskDto(tasks.get(1))).thenReturn(tasksDtos.get(1));

        // When & Then
        mockMvc.perform(get("/v1/tasks/{taskId}", 1).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Task 1")))
                .andExpect(jsonPath("$.content", is("Test Content 1")));

        mockMvc.perform(get("/v1/tasks/{taskId}", 2).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Test Task 2")))
                .andExpect(jsonPath("$.content", is("Test Content 2")));

    }

    @Test
    void shouldCreateTask() throws Exception {
        // Given
        TaskDto taskDto = new TaskDto(1L, "Test Task 1", "Test Content 1");
        Task task = new Task(1L, "Test Task 1", "Test Content 1");

        when(taskMapper.mapToTask(taskDto)).thenReturn(task);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        // When & Then
        mockMvc.perform(post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(status().isOk());

    }

    @Test
    void shouldUpdateTask() throws Exception {
        // Given
        TaskDto updatedTaskDto = new TaskDto(1L, "Updated Task", "Updated Content");
        Task updatedTask = new Task(1L, "Updated Task", "Updated Content");

        when(taskMapper.mapToTask(updatedTaskDto)).thenReturn(updatedTask);
        when(service.saveTask(updatedTask)).thenReturn(updatedTask);
        when(taskMapper.mapToTaskDto(updatedTask)).thenReturn(updatedTaskDto);

        // When & Then
        mockMvc.perform(put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new Gson().toJson(updatedTaskDto)))
                .andExpect(status().isOk());

        verify(taskMapper, times(1)).mapToTask(eq(updatedTaskDto));
        verify(service, times(1)).saveTask(eq(updatedTask));
        verify(taskMapper, times(1)).mapToTaskDto(eq(updatedTask));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        // Given
        Long taskId = 1L;

        // When & Then
        mockMvc.perform(delete("/v1/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteTask(eq(taskId));
    }

}