package Validatecpf.Service;

import Validatecpf.Domain.Endereco;
import Validatecpf.Domain.User;

import Validatecpf.Exceptions.HttpMessageNotReadableException;
import Validatecpf.Exceptions.NotFoundException;
import Validatecpf.Interface.EnderecoClient;
import Validatecpf.Interface.WireMockClient;
import Validatecpf.Port.UserInputPort;
import Validatecpf.Port.UserOutputPort;
import Validatecpf.Domain.WireMock;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateandValidateCPFUseCase implements UserInputPort {

    private final UserOutputPort userOutputPort;

    private final EnderecoClient enderecoClient;

    private final WireMockClient wireMockClient;


    public boolean cpfValidation(String cpf) {
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
        } catch (NumberFormatException ex) {
            return false; // CPF contém caracteres inválidos
        }
    }

    public Long StringtoLong(String cpf){
        try {
            Long cpf2 = Long.valueOf(cpf);
            if (cpf2 == 0) {
                throw new NullPointerException("Algo está errado com o que você digitou! StringtoLong");
            }
            return cpf2;
        }catch (NumberFormatException ex){
            throw new NumberFormatException("Digite apenas números inteiros!");
        } catch (HttpMessageNotReadableException ex){
            throw new HttpMessageNotReadableException("Erro no parsing de JSON!");
        }
    }

    public String Validar(User body) {
        try {
            User user = userOutputPort.fetchByCPF(StringtoLong(body.getCpf()));
            System.out.println(user);
            //Verificando se retornou algo no metodo de busca do repository
            if (user != null) {
                return "User encontrado!";
            }
            if (cpfValidation(body.getCpf())) {
                //Insert no banco
                userOutputPort.save(body);
                return "CPF válido. User criado!";
            } else {
                return "CPF inválido!";
            }
        }catch (HttpMessageNotReadableException ex){
            throw new HttpMessageNotReadableException("Erro no parsing de JSON!");
        }
    }

    //Fluxo 2

    public String ValidarExistencia(User body) {
        try{
            User user = userOutputPort.fetchByCPF(StringtoLong(body.getCpf()));
            //Verificando se o retorno do método é igual ao cpf do body da requisição
            if (user != null){
                if ((user.getCpf()).equals(body.getCpf())) {
                    return "User encontrado!";
                }
                throw new NotFoundException("User não encontrado na Validação!");
            }
            return "F";
        }catch (HttpMessageNotReadableException ex){
            throw new HttpMessageNotReadableException("Erro no parsing de JSON!");
        }
    }

    //Método para captar erro, quando o cpf é null ou 0
//    private void validarNull(User body){
//        if (body.getCpf() == null || (body.getCpf() == 0))
//            throw new NullPointerException("O valor não pode ser null ou 0");
//    }

    public WireMock wireMockRetorno(Long cpf){
        try {
            WireMock wireMock = wireMockClient.FetchWireMockByCPF(cpf);
            if (wireMock == null) {
                throw new NotFoundException("Endereco não encontrado! método");
            }
            return wireMock;
        }catch (NullPointerException ex){
            throw new NullPointerException("Algo está errado com o que você digitou! WireMock");
        }
//        WireMock wireMock = wireMockClient.FetchWireMockByCPF(cpf);
//        if (wireMock.getCEP().equals(wireMockClient.FetchWireMockByCPF(cpf).getCEP())) {
//            return wireMock;
//        }return null;
    }

    public Endereco enderecoRetorno(String cep){
        try {
            Endereco endereco = enderecoClient.getEndereco(cep);
            if (endereco == null) {
                throw new NotFoundException("Endereco não encontrado! método");
            }
            return endereco;
        }catch (NullPointerException ex){
            throw new NullPointerException("Algo está errado com o que você digitou! Endereco");
        }
    }

    public ResponseEntity<?> retornarEndereco(User body){
        try {
            if (ValidarExistencia(body).equals("User encontrado!")) {
                WireMock wiremock = wireMockRetorno(StringtoLong(body.getCpf()));
                if (wiremock != null) {
                    Endereco endereco = enderecoRetorno(wiremock.getCEP());
                    if (endereco != null) {
                        return ResponseEntity.status(200).body(endereco);
                    }
                    throw new NotFoundException("Endereco não encontrado! retorno");
                }
                throw new NotFoundException("WireMock não encontrado! retorno");
            }
            throw new NotFoundException("User não encontrado! retorno");
        }catch (HttpMessageNotReadableException ex){
            throw new HttpMessageNotReadableException("Erro no parsing de JSON!");
        }
    }

    //Teste

    public ResponseEntity<?> retornarEndereco2(String cep) {
            Endereco endereco = enderecoClient.getEndereco(cep);
            System.out.println(endereco);
            return ResponseEntity.ok(endereco);
    }
}
