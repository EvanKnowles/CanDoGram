package za.co.knonchalant.candogram;

import com.google.gson.Gson;
import za.co.knonchalant.candogram.dao.PendingResponseDAO;
import za.co.knonchalant.candogram.domain.BaseDetail;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.*;

import javax.ejb.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by evan on 2016/03/08.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class HandlerBot {
    private static final Logger LOGGER = Logger.getLogger(HandlerBot.class.getName());

    @EJB
    PendingResponseDAO telegramDAO;

    public HandlerBot() {
    }

    public HandlerBot(PendingResponseDAO telegramDAO) {
        this.telegramDAO = telegramDAO;
    }

    @Asynchronous
    public void handle(IBotAPI bot, IUpdate update) {
        if (update.isInline()) {
            IInlineHandler inlineHandler = bot.getInlineHandler();
            if (inlineHandler != null) {
                inlineHandler.handle(update);
            }
            return;
        }

        List<IMessageHandler> iMessageHandlers = bot.getHandlers();

        String text = getText(update);
        if (text == null  || (!text.startsWith("/"))) {
            if (tryHandleAsResponse(update, iMessageHandlers, bot)) {
                return;
            }
        }

        for (IMessageHandler iMessageHandler : iMessageHandlers) {
            try {
                handle(iMessageHandler, update);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Handler " + iMessageHandler.toString() + " broke", ex);
            }
        }
    }

    private void clearPending(IUpdate update) {
        List<PendingResponse> pendingResponses = telegramDAO.getPendingResponses(update.getUser().getId(), update.getChatId());

        for (PendingResponse pendingResponse : pendingResponses) {
            telegramDAO.delete(pendingResponse);
        }
    }

    private boolean tryHandleAsResponse(IUpdate update, List<IMessageHandler> iMessageHandlers, IBotAPI bot) {
        List<PendingResponse> pendingResponses = telegramDAO.getPendingResponses(update.getUser().getId(), update.getChatId());

        for (PendingResponse pendingResponse : pendingResponses) {
            for (IMessageHandler iMessageHandler : iMessageHandlers) {
                try {
                    if (processHandler(update, pendingResponse, iMessageHandler, bot)) return true;
                }catch (Exception ex) {
                    LOGGER.log(Level.WARNING, iMessageHandler.getClass().toString() + " failed on message: " + ex.toString());
                }
            }
        }
        return false;
    }

    private boolean processHandler(IUpdate update, PendingResponse pendingResponse, IMessageHandler iMessageHandler, IBotAPI bot) {
        if (!(iMessageHandler instanceof IResponseMessageHandler)) {
            return false;
        }

        IResponseMessageHandler handler = (IResponseMessageHandler) iMessageHandler;
        List<IResponseHandler> handlers = handler.getHandlers();
        for (IResponseHandler<?> iResponseHandler : handlers) {
            if (tryHandle(update, pendingResponse, iResponseHandler, bot)) return true;
        }

        return false;
    }

    private boolean tryHandle(IUpdate update, PendingResponse pendingResponse, IResponseHandler iResponseHandler, IBotAPI bot) {
        try {
            Gson gson = new Gson();
            BaseDetail details = (BaseDetail) gson.fromJson(pendingResponse.getDetails(), iResponseHandler.getDetailsClass());

            if (details.getStep() == iResponseHandler.getStep() && pendingResponse.getIdentifier().equals(iResponseHandler.getIdentifier())) {
                iResponseHandler.setBot(bot);
                pendingResponse.setStepRetry(false);
                pendingResponse.setStepHandled(false);

                PendingResponse resultResponse = iResponseHandler.handleResponse(update, details, pendingResponse);

                if (resultResponse.isStepHandled()) {
                    details.nextStep();
                }
                resultResponse.setDetails(details);

                if (!resultResponse.isComplete()) {
                    telegramDAO.persistPendingResponse(resultResponse);
                } else {
                    clearPending(update);
                }

                return resultResponse.isComplete() || resultResponse.isStepHandled() || resultResponse.isStepRetry();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Handle response issue.", ex);
            return true;
        }

        return false;
    }

    public String getText(IUpdate update) {
        if (update == null || update.getText() == null) {
            return null;
        }

        return update.getText();
    }

    private void handle(IMessageHandler iMessageHandler, IUpdate update) {
        try {
            if (iMessageHandler.matches(update)) {
                PendingResponse handle = iMessageHandler.handle(update);
                if (handle != null) {
                    telegramDAO.clearPending(update.getUser().getId(), update.getChatId());

                    LOGGER.info(handle.getIdentifier() + " expecting a response");
                    telegramDAO.persistPendingResponse(handle);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Command handler exception", ex);
        }
    }
}
