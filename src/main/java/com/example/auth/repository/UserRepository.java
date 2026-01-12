package com.example.auth.repository;

import com.example.auth.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<User> mapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password_hash")
    );

    public Optional<User> findByEmail(String email) {
        var sql = "SELECT id, name, email, password_hash FROM users WHERE email = ? LIMIT 1";
        var list = jdbc.query(sql, mapper, email);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public long insert(String name, String email, String passwordHash) {
        var sql = "INSERT INTO users(name, email, password_hash) VALUES(?,?,?)";
        jdbc.update(sql, name, email, passwordHash);

        // Ambil last id (aman untuk contoh; produksi lebih baik pakai KeyHolder)
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return id == null ? 0 : id;
    }
}
