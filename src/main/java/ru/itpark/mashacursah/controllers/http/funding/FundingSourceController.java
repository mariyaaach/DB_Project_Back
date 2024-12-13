package ru.itpark.mashacursah.controllers.http.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itpark.mashacursah.controllers.http.funding.dto.FundingSource;
import ru.itpark.mashacursah.infrastructure.aspects.Log;
import ru.itpark.mashacursah.service.funding.FundingSourceService;

import java.util.List;

@RestController
@Log
@RequestMapping("/api/funding-sources")
@RequiredArgsConstructor
public class FundingSourceController {
    private final FundingSourceService fundingSourceService;

    @GetMapping
    public List<FundingSource> getAllFundingSources() {
        return fundingSourceService.getAllFundingSources();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundingSource> getFundingSourceById(@PathVariable Long id) {
        return fundingSourceService.getFundingSourceById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createFundingSource(@RequestBody FundingSource fundingSource) {
        fundingSourceService.createFundingSource(fundingSource);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFundingSource(@PathVariable Long id, @RequestBody FundingSource fundingSource) {
        if (fundingSourceService.updateFundingSource(id, fundingSource)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFundingSource(@PathVariable Long id) {
        if (fundingSourceService.deleteFundingSource(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}