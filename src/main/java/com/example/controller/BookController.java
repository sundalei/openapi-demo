package com.example.controller;

import com.example.exception.BookNotFoundException;
import com.example.model.Book;
import com.example.repository.BookRepository;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " is not found."));
    }

    @GetMapping("/")
    public Collection<Book> findBooks() {
        return repository.findAll();
    }

    @GetMapping("/filter")
    public Page<Book> filterBook(@ParameterObject Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(
            @PathVariable("id") final String id,
            @RequestBody final Book book) {
        Optional<Book> optional = repository.findById(id);
        if (optional.isPresent()) {
            Book update = optional.get();
            update.setTitle(book.getTitle());
            update.setAuthor(book.getAuthor());
            return update;
        } else {
            book.setId(id);
            return book;
        }
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book patchBook(
            @PathVariable("id") final String id,
            @RequestBody final Book book) {
        Optional<Book> optional = repository.findById(id);
        if (optional.isPresent()) {
            Book update = optional.get();
            update.setTitle(book.getTitle());
            update.setAuthor(book.getAuthor());
            return update;
        } else {
            book.setId(id);
            return book;
        }
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Book postBook(@NotNull @Valid @RequestBody final Book book) {
        repository.save(book);
        return book;
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "/")
    @ResponseStatus(HttpStatus.OK)
    public Book headBook() {
        return new Book();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteBook(@PathVariable final String id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return id;
        } 
        return null;
    }
}
