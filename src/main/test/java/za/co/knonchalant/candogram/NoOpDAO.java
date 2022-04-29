package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.dao.PendingResponseDAO;
import za.co.knonchalant.candogram.domain.PendingResponse;

import java.util.Collections;
import java.util.List;

public class NoOpDAO  extends PendingResponseDAO {
    @Override
    public PendingResponse persistPendingResponse(PendingResponse pendingResponse) {
        return pendingResponse;
    }

    @Override
    public void markDone(PendingResponse response) {
    }

    @Override
    public List<PendingResponse> getPendingResponses(Long userId, Long chatId) {
        return Collections.emptyList();
    }

    @Override
    public void delete(PendingResponse pendingResponse) {
    }

    @Override
    public void clearPending(long userId, long chatId) {
    }
}
