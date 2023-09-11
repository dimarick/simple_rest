package ru.students.simple_rest.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.students.simple_rest.model.Request;

@Service
@Qualifier("RemoteRequestModifierService")
public class RemoteRequestModifierService implements RequestModifierServiceInterface {

    @Override
    public void modifyRequest(Request request) {

        request.setSystemTime(java.time.LocalDateTime.now().toString());

        new RestTemplate().exchange(
                "http://localhost:8082/echo",
                HttpMethod.POST,
                new HttpEntity<>(request),
                new ParameterizedTypeReference<>() {}
        );
    }
}
