package io.github.mcvlaga.Notes;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Service
public class SearchNotesService {

    @Autowired
    private final EntityManager centityManager;


    @Autowired
    public SearchNotesService(EntityManager entityManager) {
        super();
        this.centityManager = entityManager;
    }

    @Transactional
    public List<Note> searchNotes(String searchTerm) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Note.class).get();

        Query notesQuery = qb
                .keyword()
                .wildcard()
                .onFields("title", "text")
                .matching("*" + searchTerm + "*")
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(notesQuery, Note.class);

        // execute search

        List<Note> notes = null;
        try {
            notes = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            ;// do nothing

        }

        return notes;


    }
}
