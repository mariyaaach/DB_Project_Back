package ru.itpark.mashacursah.infrastructure.repository.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.itpark.mashacursah.controllers.http.funding.dto.FundingSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FundingSourceRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<FundingSource> findAll() {
        final String sql = "SELECT funding_source_id, name, description FROM funding_sources";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FundingSource fs = new FundingSource();
            fs.setFundingSourceId(rs.getLong("funding_source_id"));
            fs.setName(rs.getString("name"));
            fs.setDescription(rs.getString("description"));
            return fs;
        });
    }

    public Optional<FundingSource> findById(Long id) {
        final String sql = "SELECT funding_source_id, name, description FROM funding_sources WHERE funding_source_id = :id";
        try {
            FundingSource fs = jdbcTemplate.queryForObject(sql, Map.of("id", id), (rs, rowNum) -> {
                FundingSource fundingSource = new FundingSource();
                fundingSource.setFundingSourceId(rs.getLong("funding_source_id"));
                fundingSource.setName(rs.getString("name"));
                fundingSource.setDescription(rs.getString("description"));
                return fundingSource;
            });
            return Optional.of(fs);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(FundingSource fundingSource) {
        final String sql = "INSERT INTO funding_sources (name, description) VALUES (:name, :description)";
        Map<String, Object> params = Map.of(
            "name", fundingSource.getName(),
            "description", fundingSource.getDescription()
        );
        jdbcTemplate.update(sql, params);
    }

    public int update(Long id, FundingSource fundingSource) {
        final String sql = "UPDATE funding_sources SET name = :name, description = :description WHERE funding_source_id = :id";
        Map<String, Object> params = Map.of(
            "name", fundingSource.getName(),
            "description", fundingSource.getDescription(),
            "id", id
        );
        return jdbcTemplate.update(sql, params);
    }

    public int deleteById(Long id) {
        final String sql = "DELETE FROM funding_sources WHERE funding_source_id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id));
    }
}