package com.todo.api.todo.repository;

import com.todo.api.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Todo findTopByUserIdOrderByIdDesc(Long userId);

    Page<Todo> findByUserIdOrderByIdAsc(Long userId, Pageable pageable);

    Page<Todo> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

}
