package io.eskay.basictodo.repository;

import io.eskay.basictodo.entity.Todo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TodoRepositoryUnitTests {

    @Autowired
    private TodoRepository todoRepository;


    @Test
    public void save_ReturnsSavedTodo() {
        //Arrange
        var todo = Todo.builder().name("Go out for a walk").build();
        //Act
        var savedTodo = todoRepository.save(todo);

        //Assert
        //not null, id not null and name must match
        Assertions.assertThat(savedTodo).isNotNull();
        Assertions.assertThat(savedTodo.getId()).isGreaterThan(0L);
        Assertions.assertThat(savedTodo.getName()).isNotNull();
        Assertions.assertThat(savedTodo.getName()).isEqualTo(todo.getName());
    }

    @Test
    public void save_ReturnsUpdatedTodo() {
        //Arrange
        var todo = Todo.builder().name("Listen to music").completed(false).build();
        var savedTodo = todoRepository.save(todo);
        savedTodo.setName("Listen to pop music");
        savedTodo.setCompleted(true);

        //Act
        var updatedTodo = todoRepository.save(savedTodo);

        //Assert
        Assertions.assertThat(updatedTodo).isNotNull();
        Assertions.assertThat(updatedTodo).isEqualTo(savedTodo);
        Assertions.assertThat(updatedTodo.isCompleted()).isTrue();
    }

    @Test
    public void findAll_ReturnsAllTodosList() {
        //Arrange
        var todo1 = Todo.builder().name("Play games").build();
        var todo2 = Todo.builder().name("Read a book").build();

        todoRepository.saveAll(List.of(todo1,todo2));

        //Act
        var todoList = todoRepository.findAll();

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.size()).isEqualTo(2);
        Assertions.assertThat(todoList).containsAll(List.of(todo1,todo2));
    }

    @Test
    public void findAll_ReturnsEmptyTodosList() {
        //Arrange
        //Act
        var todoList = todoRepository.findAll();
        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.isEmpty()).isTrue();
    }

    @Test
    public void findAllByCompletedStatus_ReturnsFilteredTodoList() {
        //Arrange
        boolean filterBy = false;
        var todo1 = Todo.builder().name("Go out for a walk").completed(true).build();
        var todo2 = Todo.builder().name("Read a book").completed(false).build();
        var todo3 = Todo.builder().name("Go for swimming").completed(true).build();
        var todo4 = Todo.builder().name("Play games").completed(false).build();

        todoRepository.saveAll(List.of(todo1,todo2,todo3,todo4));

        //Act
        var todoList = todoRepository.findAllByCompleted(filterBy);

        //Assert
        Assertions.assertThat(todoList).isNotNull();
//        var result = todoList.stream().allMatch(todo -> todo.isCompleted() == filterBy);
//        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(todoList).allMatch(todo -> todo.isCompleted() == filterBy);
    }

    @Test
    public void findAllByCompletedStatus_ReturnsEmptyList() {
        //Arrange
        boolean filterBy = false;
        var todo1 = Todo.builder().name("Go out for a walk").completed(true).build();
        var todo2 = Todo.builder().name("Read a book").completed(true).build();

        todoRepository.saveAll(List.of(todo1,todo2));

        //Act
        var todoList = todoRepository.findAllByCompleted(filterBy);

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.isEmpty()).isTrue();
    }

    @Test
    public void findById_ReturnsSavedTodo() {
        //Arrange
        var todo = Todo.builder().name("Go to the beach").build();
        todoRepository.save(todo);

        //Act
        var returnedTodo = todoRepository.findById(todo.getId());

        //Assert
        Assertions.assertThat(returnedTodo.isPresent()).isTrue();
        Assertions.assertThat(returnedTodo.get()).isEqualTo(todo);
    }

    @Test
    public void deleteById_ReturnsVoid() {
        //Arrange
        var todo = Todo.builder().name("Play football").build();
        todoRepository.save(todo);
        var todoListBeforeDelete = todoRepository.findAll();

        //Act
        todoRepository.deleteById(todo.getId());
        var todoListAfterDelete = todoRepository.findAll();

        //Assert
        Assertions.assertThat(todoListBeforeDelete.size()).isEqualTo(1);
        Assertions.assertThat(todoListAfterDelete.isEmpty()).isTrue();
    }
}
