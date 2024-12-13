package ru.itpark.mashacursah.service.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itpark.mashacursah.controllers.http.funding.dto.FundingSource;
import ru.itpark.mashacursah.infrastructure.repository.funding.FundingSourceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingSourceService {
    private final FundingSourceRepository fundingSourceRepository;

    public List<FundingSource> getAllFundingSources() {
        return fundingSourceRepository.findAll();
    }

    public Optional<FundingSource> getFundingSourceById(Long id) {
        return fundingSourceRepository.findById(id);
    }

    public void createFundingSource(FundingSource fundingSource) {
        fundingSourceRepository.save(fundingSource);
    }

    public boolean updateFundingSource(Long id, FundingSource fundingSource) {
        return fundingSourceRepository.update(id, fundingSource) > 0;
    }

    public boolean deleteFundingSource(Long id) {
        return fundingSourceRepository.deleteById(id) > 0;
    }
}