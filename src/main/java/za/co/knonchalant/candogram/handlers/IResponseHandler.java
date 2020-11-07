package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.api.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;

/**
 * Created by evan on 2016/03/11.
 */
public interface IResponseHandler<S> {

    public int getStep();

    public PendingResponse handleResponse(IUpdate update, S state, PendingResponse pendingResponse) ;

    public Class<S> getDetailsClass();

    public void setBot(IBotAPI bot);

    public String getIdentifier();
}
