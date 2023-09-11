package ru.students.simple_rest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.students.simple_rest.model.Request;
import ru.students.simple_rest.model.Response;
import ru.students.simple_rest.service.RequestModifierServiceInterface;
import ru.students.simple_rest.service.ResponseModifierServiceInterface;

@Slf4j
@RestController
public class SimpleController
{
    private final ResponseModifierServiceInterface responseModifierServiceInterface;
    private final RequestModifierServiceInterface requestModifierServiceInterface;

    private SimpleController(
            @Qualifier("ErrorInfoModifierService") ResponseModifierServiceInterface responseModifierServiceInterface,
            @Qualifier("RemoteRequestModifierService") RequestModifierServiceInterface requestModifierServiceInterface
    ) {

        this.responseModifierServiceInterface = responseModifierServiceInterface;
        this.requestModifierServiceInterface = requestModifierServiceInterface;
    }

    @PostMapping(value = "/echo")
    public ResponseEntity<Response> echo(@RequestBody Request request)
    {
        log.info("Incoming request: " + String.valueOf(request));

        requestModifierServiceInterface.modifyRequest(request);

        var response = Response.builder()
            .uid(request.getUid())
            .operationUid(request.getOperationUid())
            .systemTime(request.getSystemTime())
            .code("success")
            .errorCode(null)
            .errorMessage(null)
            .build();

        var modifiedResponse = responseModifierServiceInterface.modify(response);

        log.info("Outgoing response: " + String.valueOf(modifiedResponse));

        return new ResponseEntity<>(modifiedResponse, HttpStatus.OK);
    }
}
