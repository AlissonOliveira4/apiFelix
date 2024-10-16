package Validatecpf.Controller;

import Validatecpf.Domain.User;
import Validatecpf.Port.UserInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController //Indica que essa classe é um controller REST. Junção das anotações Controller e ResponseBody
//StateLess -> a cada nova requisição eu recebo todas as informações que eu preciso para fazer aquela funcionalidade que preciso (manda um token, depois da primeira vez para não estragar a experiência dele), StateFull  -> vai manter o estado de cada cliente
@RequiredArgsConstructor
@RequestMapping("/desafio")
public class ValidationController {

    private final UserInputPort user;


    @PostMapping("/validar")
    public String ValidateCPFPost(@RequestBody User body){
        return user.Validar(body);
    }

    @GetMapping("/endereco")
    public ResponseEntity<?> retornarEndereco(@RequestBody User body) {
        return user.retornarEndereco(body);
    }

    @GetMapping("/buscar-endereco/{cep}")
    public ResponseEntity<?> retornarEndereco2(@PathVariable String cep) {
        return user.retornarEndereco2(cep);
    }

}