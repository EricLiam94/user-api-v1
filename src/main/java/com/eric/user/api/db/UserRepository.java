package com.eric.user.api.db;

import com.eric.user.api.models.UserData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserData,Long> {

    public Optional<UserData> findByusername(String username);

    @Override
    List<UserData> findAll();
}
