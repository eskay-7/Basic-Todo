package io.eskay.basictodo.repository;

import io.eskay.basictodo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {
    List<Todo> findAllByCompleted(boolean isComplete);
}
