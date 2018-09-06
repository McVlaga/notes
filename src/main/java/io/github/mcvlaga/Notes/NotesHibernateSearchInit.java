package io.github.mcvlaga.Notes;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class NotesHibernateSearchInit implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println("Error occured trying to build Hibernate Search indexes "
                    + e.toString());
        }
    }
}