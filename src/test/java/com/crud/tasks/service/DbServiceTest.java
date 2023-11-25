package com.crud.tasks.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DbService dbService;

    @Test
    void shouldGetAllTasks() {
        //Given
        List<Task> tasks = Arrays.asList(
                new Task(1L, "Task 1", "Description 1"),
                new Task(2L, "Task 2", "Description 2")
        );

        when(taskRepository.findAll()).thenReturn(tasks);

        //When
        List<Task> result = dbService.getAllTasks();

        //Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Description 1", result.get(0).getContent());
        assertEquals("Task 2", result.get(1).getTitle());
        assertEquals("Description 2", result.get(1).getContent());
    }

    @Test
    void shouldGetTaskById() throws TaskNotFoundException {
        //Given
        Long taskId = 1L;
        Task task = new Task(taskId, "Task 1", "Description 1");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        //When
        Task result = dbService.getTask(taskId);

        //Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        assertEquals("Task 1", result.getTitle());
        assertEquals("Description 1", result.getContent());
    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistingTaskById() {
        //Given
        Long nonExistingTaskId = 99L;

        when(taskRepository.findById(nonExistingTaskId)).thenReturn(Optional.empty());

        //When & Then
        assertThrows(TaskNotFoundException.class, () -> dbService.getTask(nonExistingTaskId));
    }

    @Test
    void shouldSaveTask() {
        //Given
        Task taskToSave = new Task(null, "New Task", "New Description");
        Task savedTask = new Task(1L, "New Task", "New Description");

        when(taskRepository.save(taskToSave)).thenReturn(savedTask);

        //When
        Task result = dbService.saveTask(taskToSave);

        //Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getContent());
    }

    @Test
    void shouldDeleteTaskById() throws TaskNotFoundException {
        //Given
        Long taskIdToDelete = 1L;

        when(taskRepository.existsById(taskIdToDelete)).thenReturn(true);

        //When & Then
        assertDoesNotThrow(() -> dbService.deleteTask(taskIdToDelete));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTaskById() {
        //Given
        Long nonExistingTaskId = 99L;

        when(taskRepository.existsById(nonExistingTaskId)).thenReturn(false);

        //When & Then
        assertThrows(TaskNotFoundException.class, () -> dbService.deleteTask(nonExistingTaskId));
    }
}