package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.domain.PendingResponse;

public interface IInlineHandler {
    public PendingResponse handle(IUpdate update);
}
