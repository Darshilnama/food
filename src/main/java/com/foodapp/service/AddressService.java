package com.foodapp.service;

import com.foodapp.dao.AddressDAO;
import com.foodapp.dao.UserDAO;
import com.foodapp.entity.Address;
import com.foodapp.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AddressService {

    @Inject
    private AddressDAO addressDAO;

    @Inject
    private UserDAO userDAO;

    @Transactional
    public Address addAddress(Long userId, String label, String street, String city,
                              String state, String zipCode, boolean isDefault) {
        User user = userDAO.findById(userId);
        if (user == null) throw new IllegalArgumentException("User not found");

        Address address = new Address();
        address.setUser(user);
        address.setLabel(label);
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZipCode(zipCode);
        address.setIsDefault(isDefault);
        return addressDAO.save(address);
    }

    public List<Address> getAddressesForUser(Long userId) {
        return addressDAO.findByUserId(userId);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressDAO.findById(addressId);
        if (address != null) addressDAO.delete(address);
    }
    public Address getById(Long addressId) {
        return addressDAO.findById(addressId);
    }
}