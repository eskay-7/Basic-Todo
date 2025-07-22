package io.eskay.basictodo.service;

import io.eskay.basictodo.dto.request.CreateTodoRequest;
import io.eskay.basictodo.dto.request.PatchTodoRequest;
import io.eskay.basictodo.dto.request.UpdateTodoRequest;
import io.eskay.basictodo.dto.response.TodoDto;
import io.eskay.basictodo.exception.ResourceNotFoundException;
import io.eskay.basictodo.mapper.TodoDtoMapper;
import io.eskay.basictodo.mapper.TodoRequestMapper;
import io.eskay.basictodo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;
    private final TodoDtoMapper dtoMapper;
    private final TodoRequestMapper requestMapper;

    public TodoServiceImpl(TodoRepository repository,
                           TodoDtoMapper dtoMapper,
                           TodoRequestMapper requestMapper
    ) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    public List<TodoDto> getAllTodos() {
        return repository.findAll()
                .stream()
                .map(dtoMapper)
                .toList();
    }

    @Override
    public List<TodoDto> getAllTodosByCompletedStatus(boolean isComplete) {
        return repository.findAllByCompleted(isComplete)
                .stream()
                .map(dtoMapper)
                .toList();
    }

    @Override
    public TodoDto getTodo(Long id) {
        return repository
                .findById(id)
                .map(dtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(id)));
    }

    @Override
    public TodoDto createTodo(CreateTodoRequest request) {
        var todo = requestMapper.apply(request);
        var createdTodo = repository.save(todo);
        return dtoMapper.apply(createdTodo);
    }

    @Override
    public TodoDto toggleCompletedStatus(Long id, boolean isComplete) {
        var foundTodo = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(id)));
        foundTodo.setCompleted(isComplete);
        return dtoMapper.apply(repository.save(foundTodo));
    }

    @Override
    public void deleteTodo(Long id) {
        var foundTodo = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(id)));

        repository.deleteById(id);
    }

    @Override
    public TodoDto updateTodo(Long id, String name) {
        var foundTodo = repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(id)));
        foundTodo.setName(name);
        return dtoMapper.apply(repository.save(foundTodo));
    }

    @Override
    public TodoDto updateTodo(UpdateTodoRequest request) {
        var foundTodo = repository
                .findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(request.id())));
        foundTodo.setName(request.name());
        foundTodo.setCompleted(request.completed());
        return dtoMapper.apply(repository.save(foundTodo));
    }

    @Override
    public TodoDto patchTodo(PatchTodoRequest request) {
        //Check if name is not null and then verify that it has min of 4chars
        //Check that completed is not null
        var foundTodo = repository
                .findById(request.id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todo with id '%d' not found, check and try again".formatted(request.id())));
        if (validatePatchRequestName(request))
            foundTodo.setName(request.name());

        if (request.completed() != null)
            foundTodo.setCompleted(request.completed());

        return dtoMapper.apply(repository.save(foundTodo));
    }

    private boolean validatePatchRequestName(PatchTodoRequest request) {
        if(request.name() != null)
            return (!request.name().isBlank() && request.name().length() >= 4);
        return false;
    }
}
