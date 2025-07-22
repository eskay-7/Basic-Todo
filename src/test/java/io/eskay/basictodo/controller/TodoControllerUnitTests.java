package io.eskay.basictodo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.eskay.basictodo.dto.request.TodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.entity.Todo;
import io.eskay.basictodo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TodoControllerUnitTests {

    /*
    *todoservice
    * objectMapper
    * MockMVC
    * */
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @MockitoBean
//    private TodoService todoService;
//
//    @Test
//    public void createTodo_ReturnCreatedTodoDto(){
//        //Arrange
//        var request = new TodoRequest("Go to parents meeting");
//        var todoDto = new TodoDto(1L,request.name(),LocalDate.now(),false);
//        when(todoService.createTodo(request)).thenReturn(todoDto);
//
//        //Act
//        var response = mockMvc.perform(post("/api/")
//                .cont)
//    }
}
