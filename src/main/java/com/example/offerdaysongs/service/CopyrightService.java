package com.example.offerdaysongs.service;

import com.example.offerdaysongs.dto.requests.CreateCopyrightRequest;
import com.example.offerdaysongs.exceptions.CompanyNotFoundException;
import com.example.offerdaysongs.exceptions.CopyrightNotFoundException;
import com.example.offerdaysongs.exceptions.RecordingNotFoundException;
import com.example.offerdaysongs.model.Company;
import com.example.offerdaysongs.model.Copyright;
import com.example.offerdaysongs.model.Recording;
import com.example.offerdaysongs.repository.CompanyRepository;
import com.example.offerdaysongs.repository.CopyrightRepository;
import com.example.offerdaysongs.repository.RecordingRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CopyrightService {
    private final CopyrightRepository copyrightRepository;
    private final RecordingRepository recordingRepository;
    private final CompanyRepository companyRepository;

    public CopyrightService(CopyrightRepository copyrightRepository, RecordingRepository recordingRepository, CompanyRepository companyRepository) {
        this.copyrightRepository = copyrightRepository;
        this.recordingRepository = recordingRepository;
        this.companyRepository = companyRepository;
    }

    public List<Copyright> getAll() {
        return copyrightRepository.findAll();
    }

    public Copyright getById(long id) {
        return copyrightRepository.findById(id).orElseThrow(CopyrightNotFoundException::new);
    }

    @Transactional
    public Copyright create(CreateCopyrightRequest request) {
        Copyright copyright = new Copyright();
        copyright.setBegins(request.getBegins());
        copyright.setExpires(request.getExpires());
        copyright.setFee(request.getFee());

        Recording recordingDto = request.getRecording();

        if(recordingDto != null){
            Recording recording = recordingRepository.findById(recordingDto.getId()).orElseThrow(RecordingNotFoundException::new);
            recording.getCopyrights().add(copyright);
            copyright.setRecording(recording);
        }

        Company companyDto = request.getCompany();
        if(companyDto != null){
            Company company = companyRepository.findById(companyDto.getId()).orElseThrow(CompanyNotFoundException::new);
            company.getCopyrights().add(copyright);
            copyright.setCompany(company);
        }

        return copyrightRepository.save(copyright);
    }

    public void update(CreateCopyrightRequest editedCopyright, long id) {
        Copyright oldCopyRight = copyrightRepository.findById(id).orElseThrow(CopyrightNotFoundException::new);
        oldCopyRight.setId(id);
        oldCopyRight.setBegins(editedCopyright.getBegins());
        oldCopyRight.setExpires(editedCopyright.getExpires());
        oldCopyRight.setRecording(editedCopyright.getRecording());
        oldCopyRight.setCompany(editedCopyright.getCompany());
        oldCopyRight.setFee(editedCopyright.getFee());
        copyrightRepository.save(oldCopyRight);
    }

    public List<Copyright> getAllByCompanyId(long id) {
        return copyrightRepository.findAllByCompanyId(id);
    }

    public List<Copyright> getCopyrightsByPeriod(ZonedDateTime begins, ZonedDateTime expires) {
        return copyrightRepository.findAllCopyrightsByBeginsAndExpires(begins, expires);
    }
}
