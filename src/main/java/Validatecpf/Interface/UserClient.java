package Validatecpf.Interface;

import User.viacep.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "https://viacep.com.br/ws/", name = "viacep")
public interface UserClient {

    @GetMapping("/{cep}/json")
    Endereco getEndereco(@PathVariable String cep);

}
