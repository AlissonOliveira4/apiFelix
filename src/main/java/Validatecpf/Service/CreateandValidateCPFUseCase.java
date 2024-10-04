package Validatecpf.Service;

import User.viacep.Endereco;
import Validatecpf.Domain.User;
import Validatecpf.Interface.EnderecoClient;
import Validatecpf.Interface.WireMockClient;
import Validatecpf.Repository.UserRepository;
import WireMock.WireMock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class CreateandValidateCPFUseCase{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnderecoClient enderecoClient;

    @Autowired
    private WireMockClient wireMockClient;


    public boolean cpfValidation(String CPF){
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

    public User fetchByCPF(String cpf){
        return userRepository.findUserByCpf(cpf);
    }

    public User save(User body){
        return userRepository.save(body);
    }

//    public ResponseEntity<String> Validar(User body) {
//        if (fetchByCPF(body.getCpf()) != body){
//            if (cpfValidation(body.getCpf())) {
//                //Insert no banco
//                save(body);
//                return ResponseEntity.status(201).body("User criado!");
//            } else {
//                return ResponseEntity.status(404).body("User não encontrado!");
//            }
//        }
//        return null;
//    }

//    public String Validar(User body) {
//        System.out.println(fetchByCPF(body.getCpf()));
//        long cpf = 0;
//        User user = fetchByCPF(body.getCpf());
//        if (user != null) {
//            cpf = user.getCpf();
//        }
//        if (cpf == body.getCpf()){
//            return "User encontrado";
//        } else {
//            if (cpfValidation(body.getCpf())) {
//                //Insert no banco
//                save(body);
//                return "CPF válido. User criado!";
//            } else {
//                return "CPF inválido!";
//            }
//        }
//    }

    public String Validar(User body) {
        User user = fetchByCPF(body.getCpf());
        //Verificando se retornou algo no método de busca do repository
        if (user != null) {
            return "User encontrado!";
        }
        if (cpfValidation(body.getCpf())) {
            body.setValid(true);
            //Insert no banco
            save(body);
            return "CPF válido. User criado!";
        } else {
            body.setValid(false);
            //Insert no banco
            save(body);
            return "CPF inválido! User criado!";
        }
    }

    //Fluxo 2

    public String ValidarExistencia(User body) {
        User user = fetchByCPF(body.getCpf());
        //Verificando se retornou algo no método de busca do repository
        if (user != null) {
            //Verificando se o retorno do método é igual ao cpf do body da requisição
            if (user.getCpf().equals(body.getCpf())) {
                return "User encontrado!";
            }
            return "User não encontrado!";
        }
        return "User nulo!";
    }

    public WireMock wireMockRetorno(String cpf){
        return wireMockClient.FetchWireMockByCPF(cpf);

    }

    public Endereco enderecoRetorno(String cep){
        return enderecoClient.getEndereco(cep);
    }


    public ResponseEntity<?> retornarEndereco2(@PathVariable String cep) {
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
