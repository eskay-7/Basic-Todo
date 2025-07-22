package io.eskay.basictodo.service;

import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.entity.Todo;
import io.eskay.basictodo.exception.ResourceNotFoundException;
import io.eskay.basictodo.mapper.TodoDtoMapper;
import io.eskay.basictodo.mapper.TodoRequestMapper;
import io.eskay.basictodo.repository.TodoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceUnitTests {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoDtoMapper dtoMapper;

    @Mock
    private TodoRequestMapper requestMapper;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    public void getAllTodos_ReturnsAllTodoDtoList() {
        //Arrange
        var todo1 = Todo.builder().id(1L).name("Go for a walk")
                .createdAt(LocalDate.now()).completed(false)
                .build();
        var todo2 = Todo.builder().id(2L).name("Listen to music")
                .createdAt(LocalDate.now()).completed(false)
                .build();

        var todo1Dto = new TodoDto(
                todo1.getId(),
                todo1.getName(),
                todo1.getCreatedAt(),
                todo1.isCompleted());
        var todo2Dto = new TodoDto(
                todo2.getId(),
                todo2.getName(),
                todo2.getCreatedAt(),
                todo2.isCompleted());

        when(todoRepository.findAll()).thenReturn(List.of(todo1,todo2));
        when(dtoMapper.apply(todo1)).thenReturn(todo1Dto);
        when(dtoMapper.apply(todo2)).thenReturn(todo2Dto);

        //Act
        var todoList = todoService.getAllTodos();

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.size()).isEqualTo(2);
        Assertions.assertThat(todoList.containsAll(List.of(todo1Dto,todo2Dto))).isTrue();
    }

    @Test
    public void getAllTodos_ReturnsEmptyTodoDtoList() {
        //Arrange
        when(todoRepository.findAll()).thenReturn(List.of());

        //Act
        var todoList = todoService.getAllTodos();

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.isEmpty()).isTrue();
    }

    @Test
    public void getAllTodosByCompletedStatus_ReturnsFilteredTodoDtoList() {
        //Arrange
        boolean filterBy = false;
        var todo1 = Todo.builder().id(1L).name("Go for a walk").createdAt(LocalDate.now()).completed(false).build();
        var todo2 = Todo.builder().id(2L).name("Listen to music").createdAt(LocalDate.now()).completed(false).build();

        var todo1Dto = new TodoDto(
                todo1.getId(),
                todo1.getName(),
                todo1.getCreatedAt(),
                todo1.isCompleted());
        var todo2Dto = new TodoDto(
                todo2.getId(),
                todo2.getName(),
                todo2.getCreatedAt(),
                todo2.isCompleted());

        when(todoRepository.findAllByCompleted(filterBy)).thenReturn(List.of(todo1,todo2));
        when(dtoMapper.apply(todo1)).thenReturn(todo1Dto);
        when(dtoMapper.apply(todo2)).thenReturn(todo2Dto);

        //Act
        var todoList = todoService.getAllTodosByCompletedStatus(filterBy);

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.size()).isEqualTo(2);
//        boolean result = todoList.stream().allMatch(todo -> todo.completed() == filterBy);
//        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(todoList).allMatch(todo -> todo.completed() == filterBy);
    }

    @Test
    public void getAllTodosByCompletedStatus_ReturnsEmptyTodoDtoList() {
        //Arrange
        boolean filterBy = false;
        when(todoRepository.findAllByCompleted(filterBy)).thenReturn(List.of());

        //Act
        var todoList = todoService.getAllTodosByCompletedStatus(filterBy);

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.isEmpty()).isTrue();
    }

    @Test
    public void createTodo_ReturnsSavedTodoDto() {
        //Arrange
        var request = new CreateTodoRequest("Go for movies");
        var todo = Todo.builder()
                .id(1L).name(request.name())
                .createdAt(LocalDate.now())
                .completed(false).build();
        var todoDto = new TodoDto(
                todo.getId(),
                todo.getName(),
                todo.getCreatedAt(),
                todo.isCompleted());

        when(requestMapper.apply(request)).thenReturn(todo);
        when(todoRepository.save(todo)).thenReturn(todo);
        when(dtoMapper.apply(todo)).thenReturn(todoDto);

        //Act
        var savedTodo = todoService.createTodo(request);

        //Assert
        Assertions.assertThat(savedTodo).isNotNull();
        Assertions.assertThat(savedTodo).isEqualTo(todoDto);
    }

    @Test
    public void getTodo_ReturnsTodoDto() {
        //Arrange
        Long id = 1L;
        var todo = Todo.builder()
                .id(id).name("Clean my apartment")
                .createdAt(LocalDate.now())
                .completed(false).build();
        var todoDto = new TodoDto(
                todo.getId(),
                todo.getName(),
                todo.getCreatedAt(),
                todo.isCompleted());

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(dtoMapper.apply(todo)).thenReturn(todoDto);

        //Act
        var foundTodo = todoService.getTodo(id);

        //Assert
        Assertions.assertThat(foundTodo).isNotNull();
        Assertions.assertThat(foundTodo).isEqualTo(todoDto);
    }

    @Test
    public void getTodo_ThrowsNotFound() {
        //Arrange
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> todoService.getTodo(id));
    }

    @Test
    public void toggleCompletedStatus_ReturnsUpdatedTodoDto() {
        //Arrange
        Long id = 1L;
        boolean isComplete = true;
        var todo = Todo.builder()
                .id(id).name("Watch tonight's UCL final")
                .createdAt(LocalDate.now())
                .completed(false).build();
        var todoDto = new TodoDto(
                todo.getId(),
                todo.getName(),
                todo.getCreatedAt(),
                todo.isCompleted());

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);
        when(dtoMapper.apply(todo)).thenReturn(todoDto);

        //Act
        var returnedTodo = todoService.toggleCompletedStatus(id,isComplete);

        //Assert
        Assertions.assertThat(returnedTodo).isNotNull();
        Assertions.assertThat(todo.isCompleted()).isEqualTo(isComplete);
    }

    @Test
    public void toggleCompletedStatus_ThrowsNotFound() {
        //Arrange
        Long id = 1L;
        boolean isComplete = true;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() -> todoService.toggleCompletedStatus(id,isComplete));

        //Assert
        verify(todoRepository, never()).save(any());
        verify(dtoMapper, never()).apply(any());
    }

    @Test
    public void deleteTodo_ReturnsVoid() {
        //Arrange
        Long id = 1L;
        var todo = Todo.builder()
                .id(id).name("Order dinner")
                .createdAt(LocalDate.now())
                .completed(false).build();
        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));

        //Act
        todoService.deleteTodo(id);

        //Assert
        verify(todoRepository).deleteById(id);
    }

    @Test
    public void deleteTodo_ThrowsNotFound() {
        //Arrange
        Long id = 1L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() -> todoService.deleteTodo(id));

        //Assert
        verify(todoRepository, never()).deleteById(id);
    }

    @Test
    public void updateTodo_ReturnsUpdatedTodoDto() {
        //Arrange
        Long id = 1L;
        String newName = "Buy ps5 for son";
        var todo = Todo.builder()
                .id(id).name("Buy motor car for son")
                .createdAt(LocalDate.now())
                .completed(false).build();
        var todoDto = new TodoDto(
                todo.getId(),
                todo.getName(),
                todo.getCreatedAt(),
                todo.isCompleted());

        when(todoRepository.findById(id)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);
        when(dtoMapper.apply(todo)).thenReturn(todoDto);

        //Act
        var updatedTodo = todoService.updateTodo(id,newName);

        //Assert
        Assertions.assertThat(updatedTodo).isNotNull();
        Assertions.assertThat(todo.getName()).isEqualTo(newName);
    }

    @Test
    public void updateTodo_ThrowsNotFound() {
        //Arrange
        Long id = 1L;
        String newName = "Buy ticker for movies";
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        //Act
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> todoService.updateTodo(id,newName));

        //Assert
        verify(todoRepository, never()).save(any());
        verify(dtoMapper, never()).apply(any());
    }
}