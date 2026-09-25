package com.foodapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class GenericDAO<T> {

    @PersistenceContext(unitName = "foodDeliveryPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public void delete(T entity) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }

    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class)
                .getSingleResult();
    }
}