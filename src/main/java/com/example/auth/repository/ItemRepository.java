package com.example.auth.repository;

import com.example.auth.model.Item;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Item> rowMapper = (rs, rowNum) -> {
        Item i = new Item();

        i.setId(rs.getLong("id"));
        i.setCode(rs.getString("code"));
        i.setDescription(rs.getString("description"));
        i.setItemType(rs.getString("item_type"));

        // stock -> BigDecimal
        i.setStock(rs.getBigDecimal("stock"));

        i.setNote(rs.getString("note"));

        i.setBusinessUnit(rs.getString("business_unit"));

        // created_at / updated_at -> Timestamp (sesuai model Item)
        i.setCreatedAt(rs.getTimestamp("created_at"));
        i.setCreatedBy(rs.getString("created_by"));

        Timestamp upd = rs.getTimestamp("updated_at");
        if (upd != null) {
            i.setUpdatedAt(upd);
            i.setUpdatedBy(rs.getString("updated_by"));
        }

        return i;
    };


    // ===== Pagination =====
    public List<Item> findPage(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT * FROM items
            ORDER BY id DESC
            LIMIT ? OFFSET ?
        """;
        return jdbcTemplate.query(sql, rowMapper, size, offset);
    }

    public long countAll() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM items", Long.class);
    }

    // ===== CRUD =====
    public void save(Item item) {
        String sql = """
            INSERT INTO items(code, description, item_type, stock, note, business_unit, created_at, created_by)
            VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)
        """;
        jdbcTemplate.update(
                sql,
                item.getCode(),
                item.getDescription(),
                item.getItemType(),
                item.getStock(),
                item.getNote(),
                item.getBusinessUnit(),
                item.getCreatedBy()
        );
    }

    public void update(Long id, Item item) {
        String sql = """
            UPDATE items
            SET 
                code = ?
                , description = ?
                , item_type = ?
                , stock = ?
                , note = ?
                , business_unit = ?
                , updated_at = NOW()
                , updated_by = ?
            WHERE id = ?
        """;
        jdbcTemplate.update(
                sql,
                item.getCode(),
                item.getDescription(),
                item.getItemType(),
                item.getStock(),
                item.getNote(),
                item.getBusinessUnit(),
                item.getUpdatedBy(),
                id
        );
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM items WHERE id = ?", id);
    }

    public Item findById(Long id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
