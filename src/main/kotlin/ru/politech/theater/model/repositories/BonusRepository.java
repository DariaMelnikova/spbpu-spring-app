package ru.politech.theater.model.repositories;


import ru.politech.theater.model.datastructures.Bonus;
import ru.politech.theater.model.mappers.BonusMapper;

import java.util.List;

public class BonusRepository implements Repository<Bonus> {
    private BonusMapper bonusMapper = new BonusMapper();

    @Override
    public void add(Bonus item) {
        bonusMapper.add(item);
    }

    @Override
    public void update(Bonus item) {

    }

    @Override
    public void remove(Bonus item) {
        bonusMapper.remove(item);
    }

    @Override
    public Bonus get(int id) {
        return null;
    }

    @Override
    public List<Bonus> query() {
        return bonusMapper.getAll();
    }

}
