package ru.shaplov.logic;

import ru.shaplov.models.Item;
import ru.shaplov.persistence.DbStore;
import ru.shaplov.persistence.IStore;

import java.util.List;

/**
 * @author shaplov
 * @since 05.07.2019
 */
public class DbLogic implements ILogic {

    private static final DbLogic INCTANCE = new DbLogic();

    private final IStore store = DbStore.getInstance();

    private DbLogic() {
    }

    public static DbLogic getInstance() {
        return INCTANCE;
    }

    @Override
    public Item add(Item item) {
        return store.add(item);
    }

    @Override
    public Item update(Item item) {
        Item model = store.get(item);
        model.setDone(item.isDone());
        return store.update(model);
    }

    @Override
    public void delete(Item item) {
        store.delete(item);
    }

    @Override
    public Item get(Item item) {
        return store.get(item);
    }

    @Override
    public List<Item> getAll() {
        return store.getAll();
    }
}
