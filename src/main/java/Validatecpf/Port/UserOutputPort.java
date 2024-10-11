package Validatecpf.Port;

import Validatecpf.Domain.User;

public interface UserOutputPort {

    User fetchByCPF(Long cpf);

    User save(User body);
}
