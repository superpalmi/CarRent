package com.palmieri.demo.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.palmieri.demo.entities.Logins;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Repository("persistentTokenRepository")
@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
public class PersistentTokenDao  implements PersistentTokenRepository
{
    @PersistenceContext
    protected EntityManager entityManager;

    private void flushAndClear()
    {
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void createNewToken(PersistentRememberMeToken token)
    {
        Logins logins = new Logins();

        logins.setNomeutente(token.getUsername());
        logins.setSerie(token.getSeries());
        logins.setToken(token.getTokenValue());
        logins.setUsato(token.getDate());

        this.entityManager.persist(logins);
        flushAndClear();
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed)
    {
        String JPQL = "SELECT a FROM Logins a WHERE a.serie = :series ";

        Logins logins = (Logins) entityManager.createQuery(JPQL)
                .setParameter("series", series)
                .getSingleResult();

        logins.setToken(tokenValue);
        logins.setUsato(lastUsed);

        this.entityManager.merge(logins);
        flushAndClear();

    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId)
    {
        String JPQL = "SELECT a FROM Logins a WHERE a.serie = :series ";


        Logins logins = (Logins) entityManager.createQuery(JPQL)
                .setParameter("series", seriesId)
                .getSingleResult();

        if (logins != null)
        {
            return new PersistentRememberMeToken(
                    logins.getNomeutente(),
                    logins.getSerie(),
                    logins.getToken(),
                    logins.getUsato());
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username)
    {
        String JPQL = "delete from Logins where nomeutente = :userName";

        // UTILIZZIAMO IL JPQL
        entityManager
                .createQuery(JPQL)
                .setParameter("userName", username)
                .executeUpdate();

        flushAndClear();

    }


}
