package clean.code.challenge.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import clean.code.challenge.model.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {

}
