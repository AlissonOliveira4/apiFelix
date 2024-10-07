package Validatecpf.Service;

import Validatecpf.Domain.Endereco;
import Validatecpf.Domain.User;
import Validatecpf.Interface.EnderecoClient;
import Validatecpf.Interface.WireMockClient;
import Validatecpf.Repository.UserRepository;
import Validatecpf.Domain.WireMock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreateandValidateCPFUseCase{

    private final UserRepository userRepository;

    private final EnderecoClient enderecoClient;

    private final WireMockClient wireMockClient;


    public boolean cpfValidation(long CPF){
        String cpf = String.valueOf(CPF);
        int digito1;
        int digito2;
        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        int[] peso1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        try {
            // Calcula o primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * peso1[i];
            }
            int resto = soma % 11;
            if (resto < 2) {
                digito1 = 0;
            } else {
                digito1 = 11 - resto;
            }

            // Calcula o segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Integer.parseInt(String.valueOf(cpf.charAt(i))) * peso2[i];
            }
            resto = soma % 11;
            if (resto < 2) {
                digito2 = 0;
            } else {
                digito2 = 11 - resto;
            }
            // Verifica os dígitos verificadores
            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                    digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (NumberFormatException e) {
            return false; // CPF contém caracteres inválidos
        }
    }

    public User fetchByCPF(long cpf){
        try {
            return userRepository.findUserByCpf(cpf);
        }catch (NumberFormatException e){
            return null;
        }
    }

    public User save(User body){
        return userRepository.save(body);
    }

    public String Validar(User body) {
        User user = fetchByCPF(body.getCpf());
        //Verificando se retornou algo no metodo de busca do repository
        if (user != null) {
            return "User encontrado!";
        }
        if (cpfValidation(body.getCpf())) {
            //Insert no banco
            save(body);
            return "CPF válido. User criado!";
        } else {
            return "CPF inválido!";
        }
    }

    //Fluxo 2

    public String ValidarExistencia(User body) {
        User user = fetchByCPF(body.getCpf());
        //Verificando se retornou algo no método de busca do repository
        if (user != null) {
            //Verificando se o retorno do método é igual ao cpf do body da requisição
            if (Objects.equals(user.getCpf(), body.getCpf())) {
                return "User encontrado!";
            }
            return "User não encontrado!";
        }
        return "User nulo!";
    }

    public WireMock wireMockRetorno(long cpf){
        WireMock wireMock = wireMockClient.FetchWireMockByCPF(cpf);
        if (wireMock.getCEP().equals(wireMockClient.FetchWireMockByCPF(cpf).getCEP())) {
            return wireMock;
        }return null;
    }

    public Endereco enderecoRetorno(String cep){
        Endereco endereco = enderecoClient.getEndereco(cep);
        if (endereco.getCep().equals(enderecoClient.getEndereco(cep).getCep())) {
            return endereco;
        } return null;
    }

    public ResponseEntity<?> retornarEndereco(User body){
        if (ValidarExistencia(body).equals("User encontrado!")){
            WireMock wiremock = wireMockRetorno(body.getCpf());
            if (wiremock != null) {
                Endereco endereco = enderecoRetorno(wiremock.getCEP());
                if (endereco != null) {
                    return ResponseEntity.status(200).body(endereco);
                }
                return ResponseEntity.status(404).body("Endereço deu errado!");
            }
            return ResponseEntity.status(404).body("WireMock não encontrado!");
        }
        return ResponseEntity.status(404).body("User não encontrado!");
    }

    //Teste

    public ResponseEntity<?> retornarEndereco2(String cep) {
        try {
            Endereco endereco = enderecoClient.getEndereco(cep);
            System.out.println(endereco);
            return ResponseEntity.ok(endereco);
        } catch (Exception e) {
            // Loga o erro com o stack trace para análise
            e.printStackTrace();
            // Retorna uma mensagem de erro sem o stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar o endereço para o CEP: " + cep);
        }
    }
}
