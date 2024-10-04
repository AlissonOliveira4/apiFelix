package Validatecpf.Interface;

import User.viacep.Endereco;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://viacep.com.br/ws/", name = "viacep")
public interface EnderecoClient {

    @GetMapping("{cep}/json/")
    Endereco getEndereco(@PathVariable("cep") String cep);

}
