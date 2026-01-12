package com.example.auth.repository;

import com.example.auth.model.Item;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public ItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Item> ITEM_ROW_MAPPER = new RowMapper<>() {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item it = new Item();
            it.setId(rs.getLong("id"));
            it.setCode(rs.getString("code"));
            it.setDescription(rs.getString("description"));
            it.setItemType(rs.getString("item_type"));
            it.setStock(rs.getBigDecimal("stock"));
            it.setNote(rs.getString("note"));
            it.setBusinessUnit(rs.getString("business_unit"));
            it.setCreatedAt(rs.getTimestamp("created_at"));
            it.setCreatedBy(rs.getString("created_by"));
            it.setUpdatedAt(rs.getTimestamp("updated_at"));
            it.setUpdatedBy(rs.getString("updated_by"));
            return it;
        }
    };

    public List<Item> findAll() {
        String sql = "SELECT id, code, description, item_type, stock, note, business_unit, created_at, created_by, updated_at, updated_by " +
                "FROM items ORDER BY business_unit, code";
        return jdbcTemplate.query(sql, ITEM_ROW_MAPPER);
    }

    public Optional<Item> findById(long id) {
        try {
            String sql = "SELECT id, code, description, item_type, stock, note, business_unit, created_at, created_by, updated_at, updated_by FROM items WHERE id = ?";
            Item it = jdbcTemplate.queryForObject(sql, ITEM_ROW_MAPPER, id);
            return Optional.ofNullable(it);
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public void insert(Item item) {
        String sql = "INSERT INTO items (code, description, item_type, stock, note, business_unit, created_at, created_by, updated_at, updated_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL)";
        jdbcTemplate.update(
                sql,
                item.getCode(),
                item.getDescription(),
                item.getItemType(),
                item.getStock(),
                item.getNote(),
                item.getBusinessUnit(),
                new Timestamp(System.currentTimeMillis()),
                item.getCreatedBy()
        );
    }

    public void update(Item item) {
        String sql = "UPDATE items SET code = ?, description = ?, item_type = ?, stock = ?, note = ?, business_unit = ?, updated_at = ?, updated_by = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                item.getCode(),
                item.getDescription(),
                item.getItemType(),
                item.getStock(),
                item.getNote(),
                item.getBusinessUnit(),
                new Timestamp(System.currentTimeMillis()),
                item.getUpdatedBy(),
                item.getId()
        );
    }

    public Optional<Item> findByCodeAndBusinessUnit(String code, String businessUnit) {
        try {
            String sql = "SELECT id, code, description, item_type, stock, note, business_unit, created_at, created_by, updated_at, updated_by " +
                    "FROM items WHERE code = ? AND business_unit = ?";
            Item it = jdbcTemplate.queryForObject(sql, ITEM_ROW_MAPPER, code, businessUnit);
            return Optional.ofNullable(it);
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean existsCodeBusinessUnitExceptId(String code, String businessUnit, long exceptId) {
        String sql = "SELECT COUNT(1) FROM items WHERE code = ? AND business_unit = ? AND id <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code, businessUnit, exceptId);
        return count != null && count > 0;
    }
}
