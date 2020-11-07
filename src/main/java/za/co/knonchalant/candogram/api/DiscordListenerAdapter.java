package za.co.knonchalant.candogram.api;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import za.co.knonchalant.candogram.api.update.DiscordUpdate;

import java.util.ArrayList;
import java.util.List;

public class DiscordListenerAdapter extends ListenerAdapter {
    private List<DiscordUpdate> updateList = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        updateList.add(new DiscordUpdate(msg));
    }

    public List<DiscordUpdate> flush() {
        List<DiscordUpdate> updateList = this.updateList;
        this.updateList = new ArrayList<>();
        return updateList;
    }
}
