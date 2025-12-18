package com.jumunhasyeo.company.application;

import com.jumunhasyeo.company.application.command.CreateCompanyCommand;
import com.jumunhasyeo.company.application.command.DeleteCompanyCommand;
import com.jumunhasyeo.company.application.command.UpdateCompanyCommand;
import com.jumunhasyeo.company.application.dto.response.CompanyRes;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    CompanyRes create(CreateCompanyCommand command);
    CompanyRes update(UpdateCompanyCommand command);
    UUID delete(DeleteCompanyCommand command);
    CompanyRes getById(UUID companyId);
    List<CompanyRes> getAll();
    boolean existsById(UUID companyId);
    boolean existsByIdAndHubId(UUID companyId, UUID hubId);
}
