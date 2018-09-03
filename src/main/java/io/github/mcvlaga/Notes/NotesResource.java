package io.github.mcvlaga.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class NotesResource {

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping("/notes")
    public List<Note> retrieveAllNotes() {
        return notesRepository.findAll();
    }

    @GetMapping("/notes/{id}")
    public Note retrieveNote(@PathVariable long id) {
        Optional<Note> student = notesRepository.findById(id);

        if (!student.isPresent())
            throw new IllegalArgumentException("id-" + id);

        return student.get();
    }

    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable long id) {
        notesRepository.deleteById(id);
    }

    @PostMapping("/notes")
    public ResponseEntity<Object> createNote(@RequestBody Note note) {
        Note savedNote = notesRepository.save(note);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedNote.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Object> updateNote(@RequestBody Note note, @PathVariable long id) {

        Optional<Note> noteOptional = notesRepository.findById(id);

        if (!noteOptional.isPresent())
            return ResponseEntity.notFound().build();

        note.setId(id);

        notesRepository.save(note);

        return ResponseEntity.noContent().build();
    }
}
