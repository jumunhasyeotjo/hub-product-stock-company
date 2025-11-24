package com.jumunhasyeo.stock.infrastructure.external;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.presentation.HubWebController;
import com.jumunhasyeo.stock.application.service.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static java.lang.Boolean.FALSE;

@Component
@RequiredArgsConstructor
public class HubInternalCallClientImpl implements HubClient {
    private final HubWebController hubWebController;

    @Override
    public boolean existHub(UUID hubId) {
        var response = hubWebController.exist(hubId);
        if(!isOk(response)){
            throw new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        if(FALSE.equals(extractExist(response))){
            throw new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        return true;
    }

    private Boolean extractExist(ResponseEntity<ApiRes<Map<String, Boolean>>> response) {
        if (response.getBody() == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        return response.getBody().getData().get("exists");
    }

    private boolean isOk(ResponseEntity<ApiRes<Map<String, Boolean>>> response) {
        return response.getStatusCode().equals(HttpStatusCode.valueOf(200));
    }
}
