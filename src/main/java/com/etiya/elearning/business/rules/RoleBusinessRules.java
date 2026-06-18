package com.etiya.elearning.business.rules;

import com.etiya.elearning.core.exceptions.BusinessException;
import com.etiya.elearning.dataAccess.abstracts.RoleRepository;
import com.etiya.elearning.dataAccess.abstracts.UserRepository;
import com.etiya.elearning.entities.concretes.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Role'e ait iş kuralları. Manager bu kuralları çağırır; ihlalde BusinessException fırlatılır.
 */
@Service
@AllArgsConstructor
public class RoleBusinessRules {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Role roleMustExist(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Rol bulunamadı. Id: " + id));
    }

    public void roleNameCannotBeDuplicated(String name) {
        if (roleRepository.existsByName(name)) {
            throw new BusinessException("Bu isimde bir rol zaten mevcut: " + name);
        }
    }

    public void roleNameCannotBeDuplicatedForUpdate(String name, Long id) {
        if (roleRepository.existsByNameAndIdNot(name, id)) {
            throw new BusinessException("Bu isimde başka bir rol zaten mevcut: " + name);
        }
    }

    public void roleCannotBeDeletedWhenUsersExist(Long id) {
        if (userRepository.existsByRole_Id(id)) {
            throw new BusinessException("Bu role bağlı kullanıcılar olduğu için rol silinemez. Id: " + id);
        }
    }
}
