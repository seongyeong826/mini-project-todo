package com.todo.api.user.entity;

import com.todo.api.auth.request.SignUpRequest;
import com.todo.api.todo.entity.Todo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todos;

    public static User create(Role role, SignUpRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
            .account(request.account())
            .password(passwordEncoder.encode(request.password()))
            .nickname(request.nickname())
            .role(role)
            .build();
    }

    public void insertTodo(Todo todo) {
        this.todos.add(todo);
        todo.addUser(this);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
