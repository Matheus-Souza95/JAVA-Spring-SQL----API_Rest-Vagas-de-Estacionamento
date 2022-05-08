package com.api.parkingcontrol.services;

import com.api.parkingcontrol.exception.AlreadyExistException;
import com.api.parkingcontrol.exception.ResourceNotFoundException;
import com.api.parkingcontrol.form.UserForm;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.ProfileRepository;
import com.api.parkingcontrol.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
public class UserService {
    final UserRepository userRepository;
    final ProfileRepository profileRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public UserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional //garante rollback
    public Object saveNew(UserForm userSource) {

        if (existsByCpf(userSource.getCpf())) throw new AlreadyExistException("CPF ja esta em uso");

        UserModel userTarget = new UserModel();
        BeanUtils.copyProperties(userSource, userTarget);

        userTarget.setPassword(bCryptPasswordEncoder().encode(userSource.getPassword()));
        userTarget.setProfiles(profileRepository.findByName("ROLE_DEFAULT"));

        return userRepository.save(userTarget);
    }

    public UserModel saveExistent(UserModel userModel) {
        return userRepository.save(userModel);
    }

    public UserModel findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    }

    public Page<UserModel> findAll(Pageable pageable) {
        Page<UserModel> pageUser = userRepository.findAll(pageable);

        pageUser.stream().findFirst().orElseThrow(() -> new ResourceNotFoundException("Nao há usuarios registrados"));
        return pageUser;
    }

    public UserModel findByCpf(String number) {
        return userRepository.findByCpf(number).orElseThrow(() -> new ResourceNotFoundException("CPF nao encontrado"));
    }

    public boolean existsByCpf(String string) {
        return userRepository.existsByCpf(string);
    }

    public UserModel update(Map<String, String> sourceFields, String id) {
        UserModel userTarget = findById(Long.parseLong(id));

        if (existsByCpf(userTarget.getCpf())) throw new AlreadyExistException("CPF ja esta em uso");

        sourceFields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserModel.class, key);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, userTarget, value);
        });
        return saveExistent(userTarget);
    }

}
