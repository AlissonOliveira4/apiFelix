package Validatecpf.Service;

import Validatecpf.Domain.User;
import Validatecpf.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateandValidateCPFUseCase{

    @Autowired
    private UserRepository userRepository;


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
        System.out.println(fetchByCPF(body.getCpf()));
        long cpf = 0;
        User user = fetchByCPF(body.getCpf());
        //Verificando se retornou algo no método de busca do repository
        if (user != null) {
            cpf = user.getCpf();
        }
        //Verificando se o retorno do método é igual ao cpf do body da requisição
        if (cpf == body.getCpf()) {
            return "User encontrado!";
        } else {
            if (cpfValidation(body.getCpf())) {
                //Insert no banco
                save(body);
                return "CPF válido. User criado!";
            } else {
                return "CPF inválido!";
            }
        }
    }
}
