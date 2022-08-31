package demo.service;

import demo.model.Role;

import java.util.List;

public interface RoleService {
    List <Role> findAll();

    Role findByName(String name);
}
