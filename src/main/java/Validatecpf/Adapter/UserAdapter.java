package Validatecpf.Adapter;

import Validatecpf.Domain.User;
import Validatecpf.Port.UserOutputPort;
import Validatecpf.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserOutputPort {

    private final UserRepository userRepository;

    public User fetchByCPF(Long cpf){
        return userRepository.findUserByCpf(cpf);
    }


    public User save(User body){
        return userRepository.save(body);
    }
}
