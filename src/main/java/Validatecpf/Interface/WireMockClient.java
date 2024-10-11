package Validatecpf.Interface;

import Validatecpf.Domain.WireMock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${wiremock.url}",name = "wiremock")
public interface WireMockClient {

    @GetMapping("/info")
    WireMock FetchWireMockByCPF(@RequestParam("cpf") Long cpf);

}
