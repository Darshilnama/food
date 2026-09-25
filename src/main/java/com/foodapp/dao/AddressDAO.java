package com.foodapp.dao;

import com.foodapp.entity.Address;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class AddressDAO extends GenericDAO<Address> {

    public AddressDAO() {
        super(Address.class);
    }

    public List<Address> findByUserId(Long userId) {
        return em.createQuery("SELECT a FROM Address a WHERE a.user.userId = :uid", Address.class)
                .setParameter("uid", userId)
                .getResultList();
    }
}