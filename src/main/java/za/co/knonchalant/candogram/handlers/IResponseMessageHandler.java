package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.domain.BaseDetail;

import java.util.List;

/**
 * Created by evan on 2016/03/11.
 */
public interface IResponseMessageHandler<S extends BaseDetail> {
    public List<IResponseHandler<S>> getHandlers();
}
