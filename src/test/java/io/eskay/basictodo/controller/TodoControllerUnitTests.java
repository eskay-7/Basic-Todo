package io.eskay.basictodo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.dto.request.UpdateTodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.entity.Todo;
import io.eskay.basictodo.service.TodoService;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TodoControllerUnitTests {

    /*
    *todoservice
    * objectMapper
    * MockMVC
    * */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private TodoService todoService;

    @Test
    public void createTodo_ReturnCreatedTodoDto() throws Exception {
        //Arrange
        var request = new CreateTodoRequest("Go to parents meeting");
        var todoDto = new TodoDto(1L,request.name(),LocalDate.now(),false);
        when(todoService.createTodo(request)).thenReturn(todoDto);

        //Act
        var response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isCreated());
        response.andExpect(MockMvcResultMatchers.jsonPath("id", CoreMatchers.is(1)));
        response.andExpect(MockMvcResultMatchers.jsonPath("name", CoreMatchers.is(todoDto.name())));
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void getAllTodos_ReturnsTodoDtoList() throws Exception{
        //Arrange
        var todoDto = new TodoDto(7L,"Play music",LocalDate.now(),true);
        var todoDto2 = new TodoDto(8L,"Go for a walk",LocalDate.now(),false);
        when(todoService.getAllTodos()).thenReturn(List.of(todoDto,todoDto2));

        //Act
        var response = mockMvc.perform(get("/api/todos"));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isOk());
        verify(todoService, never()).getAllTodosByCompletedStatus(anyBoolean());
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void getAllTodos_ReturnsFilteredTodoDtoList() throws Exception {
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
        when(todoService.getAllTodosByCompletedStatus(filterBy))
                .thenReturn(List.of(todo1Dto,todo2Dto));

        //Act
        var response = mockMvc.perform(get("/api/todos")
                .queryParam("completed","false"));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isOk());
        verify(todoService).getAllTodosByCompletedStatus(filterBy);
        verify(todoService, never()).getAllTodos();
        response.andExpect(MockMvcResultMatchers.jsonPath("size()",CoreMatchers.is(2)));
        var responseValue = response.andReturn().getResponse().getContentAsString();
        Assertions.assertThat(responseValue).contains(List.of(todo1Dto.name(),todo2Dto.name()));
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void updateTodo_ReturnsUpdatedTodoDto() throws Exception {
        //Arrange
        Long id = 7L;
        var request = new UpdateTodoRequest(id,"Watch the sky fall",true);
        var todoDto = new TodoDto(id,request.name(),LocalDate.now(),request.completed());
        when(todoService.updateTodo(request))
                .thenReturn(todoDto);

        //Act
        var response = mockMvc.perform(put("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("id", CoreMatchers.is(7)));
        response.andExpect(MockMvcResultMatchers.jsonPath("name", CoreMatchers.is(todoDto.name())));
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }


    @Test
    public void getTodo_ReturnsTodoDto() throws Exception {
        //Arrange
        var todoDto = new TodoDto(7L,"Play music",LocalDate.now(),true);
        when(todoService.getTodo(7L)).thenReturn(todoDto);

        //Act
        var response = mockMvc.perform(get("/api/todos/7"));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("id", CoreMatchers.is(7)));
        response.andExpect(MockMvcResultMatchers.jsonPath("name", CoreMatchers.is(todoDto.name())));
        System.out.println(response.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void deleteTodo_ReturnsVoid() throws Exception {
        //Arrange
        Long id = 2L;

        //Act
        var response = mockMvc.perform(delete("/api/todos/2"));

        //Assert
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(todoService).deleteTodo(id);
    }
}
