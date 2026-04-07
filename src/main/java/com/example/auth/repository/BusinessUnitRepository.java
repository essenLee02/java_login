package com.example.auth.repository;

import com.example.auth.model.BusinessUnit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusinessUnitRepository {

    private final JdbcTemplate jdbcTemplate;

    public BusinessUnitRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BusinessUnit> rowMapper = (rs, rowNum) -> {
        BusinessUnit bu = new BusinessUnit();
        bu.setId(rs.getLong("id"));
        bu.setIdBussinessUnit(rs.getString("id_bussiness_unit"));
        bu.setIdCompany(rs.getString("id_company"));
        bu.setCode(rs.getString("code"));
        bu.setName(rs.getString("name"));
        bu.setAddress(rs.getString("address"));
        bu.setIdCountry(rs.getString("id_country"));
        bu.setIdProvince(rs.getString("id_province"));
        bu.setIdCity(rs.getString("id_city"));
        bu.setTaxNumber(rs.getString("tax_number"));
        bu.setEmail(rs.getString("email"));
        bu.setPhoneNumber(rs.getString("phone_number"));
        bu.setStatus(rs.getInt("status"));
        bu.setCreatedDate(rs.getString("created_date"));
        bu.setCreatedBy(rs.getString("created_by"));
        bu.setUpdatedDate(rs.getString("updated_date"));
        bu.setUpdatedBy(rs.getString("updated_by"));
        bu.setDeletedDate(rs.getString("deleted_date"));
        bu.setDeletedBy(rs.getString("deleted_by"));
        return bu;
    };

    public List<BusinessUnit> findPage(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT *
            FROM bussiness_units
            ORDER BY code ASC
            LIMIT ? OFFSET ?
        """;
        return jdbcTemplate.query(sql, rowMapper, size, offset);
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM bussiness_units";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public BusinessUnit findById(Long id) {
        String sql = "SELECT * FROM bussiness_units WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void save(BusinessUnit bu) {
        String sql = """
            INSERT INTO bussiness_units
            (
                id_bussiness_unit,
                id_company,
                code,
                name,
                address,
                id_country,
                id_province,
                id_city,
                tax_number,
                email,
                phone_number,
                status,
                created_date,
                created_by,
                updated_date,
                updated_by,
                deleted_date,
                deleted_by
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                bu.getIdBussinessUnit(),
                bu.getIdCompany(),
                bu.getCode(),
                bu.getName(),
                bu.getAddress(),
                bu.getIdCountry(),
                bu.getIdProvince(),
                bu.getIdCity(),
                bu.getTaxNumber(),
                bu.getEmail(),
                bu.getPhoneNumber(),
                bu.getStatus(),
                emptyToNull(bu.getCreatedDate()),
                bu.getCreatedBy(),
                emptyToNull(bu.getUpdatedDate()),
                bu.getUpdatedBy(),
                emptyToNull(bu.getDeletedDate()),
                bu.getDeletedBy()
        );
    }

    public void update(Long id, BusinessUnit bu) {
        String sql = """
            UPDATE bussiness_units
            SET
                id_bussiness_unit = ?,
                id_company = ?,
                code = ?,
                name = ?,
                address = ?,
                id_country = ?,
                id_province = ?,
                id_city = ?,
                tax_number = ?,
                email = ?,
                phone_number = ?,
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
                bu.getIdBussinessUnit(),
                bu.getIdCompany(),
                bu.getCode(),
                bu.getName(),
                bu.getAddress(),
                bu.getIdCountry(),
                bu.getIdProvince(),
                bu.getIdCity(),
                bu.getTaxNumber(),
                bu.getEmail(),
                bu.getPhoneNumber(),
                bu.getStatus(),
                emptyToNull(bu.getCreatedDate()),
                bu.getCreatedBy(),
                emptyToNull(bu.getUpdatedDate()),
                bu.getUpdatedBy(),
                emptyToNull(bu.getDeletedDate()),
                bu.getDeletedBy(),
                id
        );
    }

    private String emptyToNull(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }
}