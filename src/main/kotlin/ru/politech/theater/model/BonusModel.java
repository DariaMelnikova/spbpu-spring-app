package ru.politech.theater.model;

import ru.politech.theater.model.datastructures.Bonus;
import ru.politech.theater.model.datastructures.BonusRequest;
import ru.politech.theater.model.datastructures.User;
import ru.politech.theater.model.repositories.BonusRepository;
import ru.politech.theater.model.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class BonusModel {
    private UserRepository userRepository = new UserRepository();
    private BonusRepository bonusRepository = new BonusRepository();

    public void giveBonusTo(User actor, Float amount) {
        if (actor == null) throw new IllegalStateException("Некорректный пользователь");
        if (amount == null) throw new IllegalStateException("Некорректная сумма");

        bonusRepository.add(new Bonus(-1, actor.getId(), amount));
    }

    public void removeBonus(BonusRequest bonus) {
        if (bonus == null) throw new IllegalStateException("Некорректный бонус");

        bonusRepository.remove(bonus.getBonus());
    }

    public List<BonusRequest> getAll() {
        List<BonusRequest> bonusRequests = new ArrayList<>();
        List<Bonus> bonuses = bonusRepository.query();

        bonuses.forEach(bonus -> bonusRequests.add(new BonusRequest(userRepository.get(bonus.getActorId()), bonus)));

        return bonusRequests;
    }
}
