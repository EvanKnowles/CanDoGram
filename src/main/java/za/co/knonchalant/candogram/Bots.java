package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.handlers.IInlineHandler;
import za.co.knonchalant.candogram.handlers.IMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;

import java.util.List;

public class Bots {
    private final String name;
    private List<IBotAPI> apis;
    private List<IMessageHandler> handlers;
    private IInlineHandler inlineHandler;

    public Bots(String name, List<IBotAPI> apis, List<IMessageHandler> handlers) {
        this(name, apis, handlers, null);
    }

    public Bots(String name, List<IBotAPI> apis, List<IMessageHandler> handlers, IInlineHandler inlineHandler) {
        this.apis = apis;
        this.name = name;

        this.handlers = handlers;
        this.inlineHandler = inlineHandler;

        for (IBotAPI api : apis) {
            api.addHandlers(handlers);
            api.setInlineHandler(inlineHandler);
        }
    }

    public String getName() {
        return name;
    }

    public List<IBotAPI> getApis() {
        return apis;
    }

    public List<IMessageHandler> getHandlers() {
        return handlers;
    }

    public IInlineHandler getInlineHandler() {
        return inlineHandler;
    }

    public void send(long chatId, String message) {
        for (IBotAPI api : apis) {
            api.sendMessage(chatId, message, null, true, null, null);
        }
    }

    public void sendPhoto(IUpdate chatId, byte[] file) {
        for (IBotAPI api : apis) {
            api.sendPhoto(chatId, file);
        }
    }

    @Override
    public String toString() {
        return "Bots{" +
                "name='" + name + '\'' +
                '}';
    }
}
