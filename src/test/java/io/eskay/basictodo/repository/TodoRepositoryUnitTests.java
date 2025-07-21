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
    public void saveReturnsSavedTodo() {
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
        Assertions.assertThat(savedTodo.getCreatedAt()).isNotNull();
    }

    @Test
    public void saveReturnsUpdatedTodo() {
        //Arrange
        var todo = Todo.builder().name("Listen to music").completed(false).build();
        var savedTodo = todoRepository.save(todo);
        savedTodo.setName("Listen to pop music");
        savedTodo.setCompleted(true);

        //Act
        var updatedTodo = todoRepository.save(savedTodo);

        //Assert
        Assertions.assertThat(updatedTodo).isNotNull();
        Assertions.assertThat(updatedTodo.getId()).isEqualTo(savedTodo.getId());
        Assertions.assertThat(updatedTodo.getName()).isEqualTo(savedTodo.getName());
        Assertions.assertThat(updatedTodo.isCompleted()).isTrue();
    }

    @Test
    public void findAllReturnsAllTodosList() {
        //Arrange
        var todo1 = Todo.builder().name("Play games").build();
        var todo2 = Todo.builder().name("Read a book").build();

        todoRepository.save(todo1);
        todoRepository.save(todo2);

        //Act
        var todoList = todoRepository.findAll();

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.size()).isEqualTo(2);
        Assertions.assertThat(todoList.containsAll(List.of(todo1,todo2))).isTrue();
    }

    @Test
    public void findAllReturnsEmptyTodosList() {
        //Arrange
        //Act
        var todoList = todoRepository.findAll();
        //Assert
        Assertions.assertThat(todoList).isNotNull();
        Assertions.assertThat(todoList.isEmpty()).isTrue();
    }

    @Test
    public void findAllByCompletedStatusReturnsFilteredTodoList() {
        //Arrange
        var todo1 = Todo.builder().name("Go out for a walk").completed(true).build();
        var todo2 = Todo.builder().name("Read a book").completed(false).build();
        var todo3 = Todo.builder().name("Go for swimming").completed(true).build();
        var todo4 = Todo.builder().name("Play games").completed(false).build();

        todoRepository.saveAll(List.of(todo1,todo2,todo3,todo4));

        //Act
        var todoList = todoRepository.findAllByCompleted(false);

        //Assert
        Assertions.assertThat(todoList).isNotNull();
        var result = todoList.stream().allMatch(todo -> !todo.isCompleted());
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void findByIdReturnsSavedTodo() {
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
    public void deleteByIdReturnEmpty() {
        //Arrange
        var todo = Todo.builder().name("Play football").build();
        todoRepository.save(todo);
        var todoListBeforeDelete = todoRepository.findAll();
        Assertions.assertThat(todoListBeforeDelete.size()).isEqualTo(1);

        //Act
        todoRepository.deleteById(todo.getId());
        var todoListAfterDelete = todoRepository.findAll();

        //Assert
        Assertions.assertThat(todoListAfterDelete.isEmpty()).isTrue();
    }
}
