package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.RoleRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Role;
import com.etiya.elearning.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User'a ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class UserBusinessRules {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User userMustExist(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Kullanıcı bulunamadı. Id: " + id));
    }

    public Role roleMustExist(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException("Rol bulunamadı. Id: " + roleId));
    }

    public void mailCannotBeDuplicated(String mail) {
        if (userRepository.existsByMail(mail)) {
            throw new BusinessException("Bu mail adresi zaten kullanılıyor: " + mail);
        }
    }

    public void mailCannotBeDuplicatedForUpdate(String mail, Long id) {
        if (userRepository.existsByMailAndIdNot(mail, id)) {
            throw new BusinessException("Bu mail adresi başka bir kullanıcı tarafından kullanılıyor: " + mail);
        }
    }
}
