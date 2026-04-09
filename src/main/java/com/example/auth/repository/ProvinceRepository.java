package com.example.auth.repository;

import com.example.auth.model.Province;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProvinceRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProvinceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Province> rowMapper = (rs, rowNum) -> {
        Province p = new Province();
        p.setId(rs.getLong("id"));
        p.setIdProvince(rs.getString("id_province"));
        p.setIdCountry(rs.getString("id_country"));
        p.setCountryName(rs.getString("country_name"));
        p.setCode(rs.getString("code"));
        p.setName(rs.getString("name"));
        p.setStatus(rs.getInt("status"));
        p.setCreatedDate(rs.getString("created_date"));
        p.setCreatedBy(rs.getString("created_by"));
        p.setUpdatedDate(rs.getString("updated_date"));
        p.setUpdatedBy(rs.getString("updated_by"));
        p.setDeletedDate(rs.getString("deleted_date"));
        p.setDeletedBy(rs.getString("deleted_by"));
        return p;
    };

    public List<Province> findAll() {
        String sql = """
            SELECT p.*, c.name AS country_name
            FROM provinces p
            LEFT JOIN countries c ON c.id_country = p.id_country
            ORDER BY p.name ASC
        """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Province> findPage(String search, int page, int size) {
        int offset = page * size;
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT
                p.*
                , c.id_country AS country_id
                , c.code AS country_code
                , c.name AS country_name
            FROM provinces p
            LEFT JOIN countries c ON c.id_country = p.id_country
            WHERE (? = '' OR IFNULL(p.code, '') LIKE ? OR p.name LIKE ? OR IFNULL(c.name, '') LIKE ?)
            ORDER BY p.name ASC
            LIMIT ? OFFSET ?
        """;

        return jdbcTemplate.query(sql, rowMapper, search == null ? "" : search.trim(), s, s, s, size, offset);
    }

    public long countAll(String search) {
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT COUNT(*)
            FROM provinces p
            LEFT JOIN countries c ON c.id_country = p.id_country
            WHERE (? = '' OR IFNULL(p.code, '') LIKE ? OR p.name LIKE ? OR IFNULL(c.name, '') LIKE ?)
        """;

        return jdbcTemplate.queryForObject(sql, Long.class, search == null ? "" : search.trim(), s, s, s);
    }

    public Province findById(Long id) {
        String sql = """
            SELECT p.*, c.name AS country_name
            FROM provinces p
            LEFT JOIN countries c ON c.id_country = p.id_country
            WHERE p.id = ?
        """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public boolean existsByName(String name, Long excludeId) {
        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM provinces WHERE LOWER(name) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, name) > 0;
        }

        String sql = "SELECT COUNT(*) FROM provinces WHERE LOWER(name) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name, excludeId) > 0;
    }

    public boolean existsByCode(String code, Long excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM provinces WHERE LOWER(code) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, code) > 0;
        }

        String sql = "SELECT COUNT(*) FROM provinces WHERE LOWER(code) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, code, excludeId) > 0;
    }

    public void save(Province p) {
        String sql = """
            INSERT INTO provinces
            (
                id_province,
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
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                p.getIdProvince(),
                p.getIdCountry(),
                p.getCode(),
                p.getName(),
                p.getStatus(),
                p.getCreatedDate(),
                p.getCreatedBy(),
                p.getUpdatedDate(),
                p.getUpdatedBy(),
                p.getDeletedDate(),
                p.getDeletedBy()
        );
    }

    public void update(Long id, Province p) {
        String sql = """
            UPDATE provinces
            SET
                id_province = ?,
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
                p.getIdProvince(),
                p.getIdCountry(),
                p.getCode(),
                p.getName(),
                p.getStatus(),
                p.getCreatedDate(),
                p.getCreatedBy(),
                p.getUpdatedDate(),
                p.getUpdatedBy(),
                p.getDeletedDate(),
                p.getDeletedBy(),
                id
        );
    }
}