package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.domain.PendingResponse;

/**
 * Created by evan on 2016/03/05.
 */
public interface IMessageHandler {
    public boolean matches(IUpdate update);

    public PendingResponse handle(IUpdate update);

    public String getCommand();

    /**
     * If this returns non-null, the command will be printed when the server comes up so that you can send it to BotFather.
     */
    public String getDescription();
}
