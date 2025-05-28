package co.edu.uceva.programaservice.domain.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "facultad-service", url = "http://api.mewings.joptionpane.software")
public interface IFacultadClient {
    @GetMapping("api/v1/facultad-service/facultades")
    ResponseEntity<Map<String, Object>> getFacultades();
}
