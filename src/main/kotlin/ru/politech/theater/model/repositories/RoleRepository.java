package ru.politech.theater.model.repositories;

import ru.politech.theater.model.datastructures.Role;
import ru.politech.theater.model.mappers.RoleMapper;

import java.util.List;

public class RoleRepository implements Repository<Role> {
    private RoleMapper roleMapper = new RoleMapper();

    @Override
    public void add(Role role) {
        roleMapper.add(role);
    }

    @Override
    public void update(Role item) {}

    @Override
    public void remove(Role item) {
        roleMapper.removeRole(item.getId());
    }

    @Override
    public Role get(int id) {
        return roleMapper.get(id);
    }

    @Override
    public List<Role> query() {
        return roleMapper.getAll();
    }

    public void acceptRole(int roleId) {
        roleMapper.acceptRole(roleId);
    }

    public List<Role> getAllForActor(int actorId) {
        return roleMapper.getAllNotAcceptedForActor(actorId);
    }

    public List<Role> getAllForPerformance(int performanceId) {
        return roleMapper.getAllForPerformance(performanceId);
    }
}
