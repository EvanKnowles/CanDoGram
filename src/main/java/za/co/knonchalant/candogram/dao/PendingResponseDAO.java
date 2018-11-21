package za.co.knonchalant.candogram.dao;


import za.co.knonchalant.candogram.domain.PendingResponse;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PendingResponseDAO {
    @PersistenceContext
    EntityManager em;

    public PendingResponse persistPendingResponse(PendingResponse pendingResponse) {
        return em.merge(pendingResponse);
    }

    public void markDone(PendingResponse response) {
        response.setComplete(true);
        em.merge(response);
    }

    public List<PendingResponse> getPendingResponses(Long userId, Long chatId) {
        TypedQuery<PendingResponse> query = em.createQuery("Select n from PendingResponse n where n.chatId = :chatId and n.userId = :userId and n.complete = false", PendingResponse.class);
        query.setParameter("chatId", chatId);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
}
