package Validatecpf.Port;


import Validatecpf.Domain.User;
import org.springframework.http.ResponseEntity;

public interface UserInputPort {

    String Validar(User body);

    ResponseEntity<?> retornarEndereco(User body);

    ResponseEntity<?> retornarEndereco2(String cep);
}
