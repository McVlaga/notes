package io.github.mcvlaga.Notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
public class NotesController {

    @Autowired
    private SearchNotesService searchNotesService;

    @Autowired
    private NotesRepository notesRepository;

    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return notesRepository.findAll(new Sort(Sort.Direction.DESC, "updatedOn"));
    }

    @GetMapping("/notes/search={searchTerm}")
    public List<Note> search(@PathVariable(value = "searchTerm") String searchTerm) {
        return searchNotesService.searchNotes(searchTerm);
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return notesRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("id-" + noteId));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        Note note = notesRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("id-" + noteId));

        notesRepository.delete(note);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/notes")
    public Note createNote(@RequestBody Note note) {
        return notesRepository.save(note);
    }

    @PutMapping("/notes/{id}")
    public Note updateNote(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody Note noteDetails) {

        Note note = notesRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("id-" + noteId));

        note.setText(noteDetails.getText());
        note.setTitle(noteDetails.getTitle());

        return notesRepository.save(note);
    }
}
