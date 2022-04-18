package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository parkingSpotRepository) {

        this.userRepository = parkingSpotRepository;
    }

    @Transactional //garante rollback
    public Object save(UserModel userModel) {

        return userRepository.save(userModel);
    }

    public Optional<UserModel> findById(Long id) {

        return userRepository.findById(id);
    }

    public Page<UserModel> findAll(Pageable pageable) {

        return userRepository.findAll(pageable);
    }

    public UserModel findByCpf(String number) {

        return userRepository.findByCpf(number);
    }

    public boolean existsByCpf(String string) {

        return userRepository.existsByCpf(string);
    }
}
