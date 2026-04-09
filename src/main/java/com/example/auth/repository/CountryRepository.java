package com.example.auth.repository;

import com.example.auth.model.Country;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CountryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Country> rowMapper = (rs, rowNum) -> {
        Country c = new Country();
        c.setId(rs.getLong("id"));
        c.setIdCountry(rs.getString("id_country"));
        c.setCode(rs.getString("code"));
        c.setName(rs.getString("name"));
        c.setStatus(rs.getInt("status"));
        c.setCreatedDate(rs.getString("created_date"));
        c.setCreatedBy(rs.getString("created_by"));
        c.setUpdatedDate(rs.getString("updated_date"));
        c.setUpdatedBy(rs.getString("updated_by"));
        c.setDeletedDate(rs.getString("deleted_date"));
        c.setDeletedBy(rs.getString("deleted_by"));
        return c;
    };

    public List<Country> findAll() {
        String sql = "SELECT * FROM countries ORDER BY name ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Country> findPage(String search, int page, int size) {
        int offset = page * size;
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT *
            FROM countries
            WHERE (? = '' OR IFNULL(code, '') LIKE ? OR name LIKE ?)
            ORDER BY name ASC
            LIMIT ? OFFSET ?
        """;

        return jdbcTemplate.query(sql, rowMapper, search == null ? "" : search.trim(), s, s, size, offset);
    }

    public long countAll(String search) {
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT COUNT(*)
            FROM countries
            WHERE (? = '' OR IFNULL(code, '') LIKE ? OR name LIKE ?)
        """;

        return jdbcTemplate.queryForObject(sql, Long.class, search == null ? "" : search.trim(), s, s);
    }

    public Country findById(Long id) {
        String sql = "SELECT * FROM countries WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public boolean existsByName(String name, Long excludeId) {
        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM countries WHERE LOWER(name) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, name) > 0;
        }

        String sql = "SELECT COUNT(*) FROM countries WHERE LOWER(name) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name, excludeId) > 0;
    }

    public boolean existsByCode(String code, Long excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM countries WHERE LOWER(code) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, code) > 0;
        }

        String sql = "SELECT COUNT(*) FROM countries WHERE LOWER(code) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, code, excludeId) > 0;
    }

    public void save(Country c) {
        String sql = """
            INSERT INTO countries
            (
                id_country,
                code,
                name,
                status,
                created_date,
                created_by,
                updated_date,
                updated_by,
                deleted_date,
                deleted_by
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                c.getIdCountry(),
                c.getCode(),
                c.getName(),
                c.getStatus(),
                c.getCreatedDate(),
                c.getCreatedBy(),
                c.getUpdatedDate(),
                c.getUpdatedBy(),
                c.getDeletedDate(),
                c.getDeletedBy()
        );
    }

    public void update(Long id, Country c) {
        String sql = """
            UPDATE countries
            SET
                id_country = ?,
                code = ?,
                name = ?,
                status = ?,
                created_date = ?,
                created_by = ?,
                updated_date = ?,
                updated_by = ?,
                deleted_date = ?,
                deleted_by = ?
            WHERE id = ?
        """;

        jdbcTemplate.update(
                sql,
                c.getIdCountry(),
                c.getCode(),
                c.getName(),
                c.getStatus(),
                c.getCreatedDate(),
                c.getCreatedBy(),
                c.getUpdatedDate(),
                c.getUpdatedBy(),
                c.getDeletedDate(),
                c.getDeletedBy(),
                id
        );
    }
}