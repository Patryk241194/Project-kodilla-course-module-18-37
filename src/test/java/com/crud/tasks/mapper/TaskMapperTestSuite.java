package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTestSuite {
    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void mapToTask() {
        // Given
        TaskDto taskDto = new TaskDto(1L, "Test Task", "Task Content");

        // When
        Task task = taskMapper.mapToTask(taskDto);

        // Then
        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getTitle(), task.getTitle());
        assertEquals(taskDto.getContent(), task.getContent());
    }

    @Test
    void mapToTaskDto() {
        // Given
        Task task = new Task(1L, "Test Task", "Task Content");

        // When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        // Then
        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getTitle(), taskDto.getTitle());
        assertEquals(task.getContent(), taskDto.getContent());
    }

    @Test
    void mapToTaskDtoList() {
        // Given
        List<Task> tasks = Arrays.asList(
                new Task(1L, "Test Task 1", "Test Content 1"),
                new Task(2L, "Test Task 2", "Test Content 2"),
                new Task(3L, "Test Task 3", "Test Content 3")
        );

        // When
        List<TaskDto> taskDtos = taskMapper.mapToTaskDtoList(tasks);

        // Then
        assertEquals(tasks.size(), taskDtos.size());

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            TaskDto taskDto = taskDtos.get(i);

            assertEquals(task.getId(), taskDto.getId());
            assertEquals(task.getTitle(), taskDto.getTitle());
            assertEquals(task.getContent(), taskDto.getContent());
        }
    }

    @Test
    void mapToTaskList() {
        // Given
        List<TaskDto> taskDtos = Arrays.asList(
                new TaskDto(1L, "Test Task 1", "Test Content 1"),
                new TaskDto(2L, "Test Task 2", "Test Content 2"),
                new TaskDto(3L, "Test Task 3", "Test Content 3")
        );

        // When
        List<Task> tasks = taskMapper.mapToTaskList(taskDtos);

        // Then
        assertEquals(taskDtos.size(), tasks.size());

        for (int i = 0; i < taskDtos.size(); i++) {
            TaskDto taskDto = taskDtos.get(i);
            Task task = tasks.get(i);

            assertEquals(taskDto.getId(), task.getId());
            assertEquals(taskDto.getTitle(), task.getTitle());
            assertEquals(taskDto.getContent(), task.getContent());
        }
    }
}