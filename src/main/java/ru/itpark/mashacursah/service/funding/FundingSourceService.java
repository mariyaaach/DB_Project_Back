package ru.itpark.mashacursah.service.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.funding.dto.FundingSource;
import ru.itpark.mashacursah.infrastructure.repository.funding.FundingSourceRepository;

import java.util.List;
import java.util.Optional;


import ru.itpark.mashacursah.service.redis.RedisService;

@Service
@RequiredArgsConstructor
public class FundingSourceService {

    private final FundingSourceRepository fundingSourceRepository;
    private final RedisService redisService;

    private static final String FUNDING_CACHE_KEY = "funding-sources:all";

    public List<FundingSource> getAllFundingSources() {
        return redisService.getCachedList(
                FUNDING_CACHE_KEY,
                FundingSource.class,
                fundingSourceRepository::findAll
        );
    }

    public Optional<FundingSource> getFundingSourceById(Long id) {
        return fundingSourceRepository.findById(id);
    }

    public void createFundingSource(FundingSource fundingSource) {
        fundingSourceRepository.save(fundingSource);
        redisService.clearCache(FUNDING_CACHE_KEY);
    }

    public boolean updateFundingSource(Long id, FundingSource fundingSource) {
        boolean updated = fundingSourceRepository.update(id, fundingSource) > 0;
        if (updated) {
            redisService.clearCache(FUNDING_CACHE_KEY);
        }
        return updated;
    }

    public boolean deleteFundingSource(Long id) {
        boolean deleted = fundingSourceRepository.deleteById(id) > 0;
        if (deleted) {
            redisService.clearCache(FUNDING_CACHE_KEY);
        }
        return deleted;
    }
}