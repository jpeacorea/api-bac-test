package com.bac.bac.repository;

import com.bac.bac.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT exists(select 1 from administrators where email=?1)", nativeQuery = true)
    boolean existsAdministratorByEmail(String email);
}
