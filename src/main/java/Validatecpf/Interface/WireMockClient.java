package Validatecpf.Interface;

import User.viacep.Endereco;
import WireMock.WireMock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "localhost:8081", name = "wiremock")
public interface WireMockClient {

    @GetMapping("/info")
    WireMock FetchWireMockByCPF(@RequestParam("cpf") String cpf);

}
