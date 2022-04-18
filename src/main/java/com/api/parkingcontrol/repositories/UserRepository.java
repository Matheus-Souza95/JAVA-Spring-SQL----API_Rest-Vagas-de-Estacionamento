package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {

    boolean existsByCpf(String string);

    UserModel findByCpf(String number);

    UserModel findByName(String number);

    Page<UserModel> findAll(Pageable pageable);

    Optional<UserModel> findById(Long id);

}

