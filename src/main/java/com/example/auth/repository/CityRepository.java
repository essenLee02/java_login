package com.example.auth.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.auth.model.City;

@Repository
public class CityRepository {

    private final JdbcTemplate jdbcTemplate;

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<City> rowMapper = (rs, rowNum) -> {
        City c = new City();
        c.setId(rs.getLong("id"));
        c.setIdCity(rs.getString("id_city"));
        c.setIdProvince(rs.getString("id_province"));
        c.setProvinceName(rs.getString("province_name"));
        c.setIdCountry(rs.getString("id_country"));
        c.setCountryName(rs.getString("country_name"));
        c.setCode(rs.getString("code"));
        c.setName(rs.getString("name"));
        c.setStatus(rs.getInt("status"));
        c.setCreatedDate(rs.getString("created_date"));
        c.setCreatedBy(rs.getString("created_by"));
        c.setUpdatedDate(rs.getString("updated_date"));
        c.setUpdatedBy(rs.getString("updated_by"));
        c.setDeletedDate(rs.getString("deleted_date"));
        c.setDeletedBy(rs.getString("deleted_by"));

        // penting
        c.setCreatedByName(rs.getString("created_by_name"));
        c.setUpdatedByName(rs.getString("updated_by_name"));

        return c;
    };

    public List<City> findPage(String search, int page, int size) {
        int offset = page * size;
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT 
                ct.*,
                c.name AS country_name,
                p.name AS province_name,
                uc.name AS created_by_name,
                uu.name AS updated_by_name
            FROM cities ct
            LEFT JOIN countries c
                ON c.id_country = ct.id_country
            LEFT JOIN provinces p
                ON p.id_province = ct.id_province
            LEFT JOIN users uc
                ON uc.id = ct.created_by
            LEFT JOIN users uu
                ON uu.id = ct.updated_by
            WHERE (? = '' OR IFNULL(ct.code, '') LIKE ? OR ct.name LIKE ? OR IFNULL(c.name, '') LIKE ? OR IFNULL(p.name, '') LIKE ?)
            ORDER BY ct.name ASC
            LIMIT ? OFFSET ?
        """;

        return jdbcTemplate.query(sql, rowMapper, search == null ? "" : search.trim(), s, s, s, s, size, offset);
    }

    public long countAll(String search) {
        String s = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT COUNT(*)
            FROM cities ct
            LEFT JOIN countries c ON c.id_country = ct.id_country
            LEFT JOIN provinces p ON p.id_province = ct.id_province
            WHERE (? = '' OR IFNULL(ct.code, '') LIKE ? OR ct.name LIKE ? OR IFNULL(c.name, '') LIKE ? OR IFNULL(p.name, '') LIKE ?)
        """;

        return jdbcTemplate.queryForObject(sql, Long.class, search == null ? "" : search.trim(), s, s, s, s);
    }

    public City findById(Long id) {
        String sql = """
            SELECT
                ct.*,
                c.name AS country_name,
                p.name AS province_name,
                uc.name AS created_by_name,
                uu.name AS updated_by_name
            FROM cities ct
            LEFT JOIN countries c
                ON c.id_country = ct.id_country
            LEFT JOIN provinces p
                ON p.id_province = ct.id_province
            LEFT JOIN users uc
                ON uc.id = ct.created_by
            LEFT JOIN users uu
                ON uu.id = ct.updated_by
            WHERE ct.id = ?
        """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public boolean existsByName(String name, Long excludeId) {
        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM cities WHERE LOWER(name) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, name) > 0;
        }

        String sql = "SELECT COUNT(*) FROM cities WHERE LOWER(name) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name, excludeId) > 0;
    }

    public boolean existsByCode(String code, Long excludeId) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        if (excludeId == null) {
            String sql = "SELECT COUNT(*) FROM cities WHERE LOWER(code) = LOWER(?)";
            return jdbcTemplate.queryForObject(sql, Long.class, code) > 0;
        }

        String sql = "SELECT COUNT(*) FROM cities WHERE LOWER(code) = LOWER(?) AND id <> ?";
        return jdbcTemplate.queryForObject(sql, Long.class, code, excludeId) > 0;
    }

    public void save(City c) {
        String sql = """
            INSERT INTO cities
            (
                id_city,
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
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                c.getIdCity(),
                c.getIdProvince(),
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

    public void update(Long id, City c) {
        String sql = """
            UPDATE cities
            SET
                id_city = ?,
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
                c.getIdCity(),
                c.getIdProvince(),
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