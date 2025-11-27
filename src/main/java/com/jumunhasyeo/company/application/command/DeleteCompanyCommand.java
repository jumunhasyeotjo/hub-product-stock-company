package com.jumunhasyeo.company.application.command;

import java.util.UUID;

public record DeleteCompanyCommand(
        UUID companyId,
        Long userId
) {
}
