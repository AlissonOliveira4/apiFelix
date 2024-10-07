package Validatecpf.Interface;

import Validatecpf.Domain.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${viacep.url}", name = "viacep")
public interface EnderecoClient {

    @GetMapping("/{cep}/json/")
    Endereco getEndereco(@PathVariable("cep") String cep);

}
