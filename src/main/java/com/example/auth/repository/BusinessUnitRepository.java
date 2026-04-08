package com.example.auth.repository;

import com.example.auth.model.BusinessUnit;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BusinessUnitRepository {

    private final JdbcTemplate jdbcTemplate;

    public BusinessUnitRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long countAll(String search) {
        String keyword = "%" + (search == null ? "" : search.trim()) + "%";

        String sql = """
            SELECT COUNT(*)
            FROM bussiness_units bu
            WHERE
                bu.code LIKE ?
                OR bu.name LIKE ?
                OR bu.id_bussiness_unit LIKE ?
                OR bu.id_company LIKE ?
                OR bu.id_country LIKE ?
                OR bu.id_province LIKE ?
                OR bu.id_city LIKE ?
                OR bu.email LIKE ?
                OR bu.phone_number LIKE ?
                OR (CASE WHEN bu.status = 1 THEN 'active' ELSE 'inactive' END) LIKE ?
            """;

        return jdbcTemplate.queryForObject(sql, Long.class,
                keyword, keyword, keyword, keyword, keyword,
                keyword, keyword, keyword, keyword, keyword
        );
    }

    public List<BusinessUnit> findPage(String search, int page, int size) {
        String keyword = "%" + (search == null ? "" : search.trim()) + "%";
        int offset = page * size;

        String sql = """
            SELECT
                bu.id,
                bu.id_bussiness_unit,
                bu.id_company,
                bu.code,
                bu.name,
                bu.address,
                bu.id_country,
                bu.id_province,
                bu.id_city,
                bu.tax_number,
                bu.email,
                bu.phone_number,
                bu.status,
                bu.created_date,
                bu.created_by,
                bu.updated_date,
                bu.updated_by,
                uc.name AS created_by_name,
                uu.name AS updated_by_name
            FROM bussiness_units bu
            LEFT JOIN users uc ON bu.created_by = uc.id
            LEFT JOIN users uu ON bu.updated_by = uu.id
            WHERE
                bu.code LIKE ?
                OR bu.name LIKE ?
                OR bu.id_bussiness_unit LIKE ?
                OR bu.id_company LIKE ?
                OR bu.id_country LIKE ?
                OR bu.id_province LIKE ?
                OR bu.id_city LIKE ?
                OR bu.email LIKE ?
                OR bu.phone_number LIKE ?
                OR (CASE WHEN bu.status = 1 THEN 'active' ELSE 'inactive' END) LIKE ?
            ORDER BY bu.id DESC
            LIMIT ? OFFSET ?
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
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
            bu.setCreatedByName(rs.getString("created_by_name"));
            bu.setUpdatedByName(rs.getString("updated_by_name"));
            return bu;
        },
                keyword, keyword, keyword, keyword, keyword,
                keyword, keyword, keyword, keyword, keyword,
                size, offset
        );
    }

    public BusinessUnit findById(Long id) {
        String sql = """
            SELECT
                bu.id,
                bu.id_bussiness_unit,
                bu.id_company,
                bu.code,
                bu.name,
                bu.address,
                bu.id_country,
                bu.id_province,
                bu.id_city,
                bu.tax_number,
                bu.email,
                bu.phone_number,
                bu.status,
                bu.created_date,
                bu.created_by,
                bu.updated_date,
                bu.updated_by,
                bu.deleted_date,
                bu.deleted_by,
                uc.name AS created_by_name,
                uu.name AS updated_by_name
            FROM bussiness_units bu
            LEFT JOIN users uc ON bu.created_by = uc.id
            LEFT JOIN users uu ON bu.updated_by = uu.id
            WHERE bu.id = ?
            """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
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
            bu.setCreatedByName(rs.getString("created_by_name"));
            bu.setUpdatedByName(rs.getString("updated_by_name"));
            return bu;
        }, id);
    }

    public void save(BusinessUnit businessUnit) {
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
                created_by
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)
            """;

        jdbcTemplate.update(sql,
                businessUnit.getIdBussinessUnit(),
                businessUnit.getIdCompany(),
                businessUnit.getCode(),
                businessUnit.getName(),
                businessUnit.getAddress(),
                businessUnit.getIdCountry(),
                businessUnit.getIdProvince(),
                businessUnit.getIdCity(),
                businessUnit.getTaxNumber(),
                businessUnit.getEmail(),
                businessUnit.getPhoneNumber(),
                businessUnit.getStatus(),
                businessUnit.getCreatedBy()
        );
    }

    public void update(Long id, BusinessUnit businessUnit) {
        String sql = """
            UPDATE bussiness_units
            SET
                name = ?,
                address = ?,
                id_country = ?,
                id_province = ?,
                id_city = ?,
                tax_number = ?,
                email = ?,
                phone_number = ?,
                status = ?,
                updated_date = NOW(),
                updated_by = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                businessUnit.getName(),
                businessUnit.getAddress(),
                businessUnit.getIdCountry(),
                businessUnit.getIdProvince(),
                businessUnit.getIdCity(),
                businessUnit.getTaxNumber(),
                businessUnit.getEmail(),
                businessUnit.getPhoneNumber(),
                businessUnit.getStatus(),
                businessUnit.getUpdatedBy(),
                id
        );
    }

    public void delete(Long id) {
        String sql = "DELETE FROM bussiness_units WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}