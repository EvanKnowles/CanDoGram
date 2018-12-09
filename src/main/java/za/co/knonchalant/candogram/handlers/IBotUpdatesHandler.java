package za.co.knonchalant.candogram.handlers;

import java.util.List;

public interface IBotUpdatesHandler {

    void handle(List<IUpdate> updates);

}
