package com.example.controller;

import com.example.exception.BookNotFoundException;
import com.example.model.Book;
import com.example.repository.BookRepository;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses( {
        @ApiResponse(responseCode = "200", description = "Found the book",
          content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Book.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
          content = @Content),
        @ApiResponse(responseCode = "404", description = "Book not found",
          content = @Content)
    })
    @GetMapping("/{id}")
    public EntityModel<Book> findById(@PathVariable Long id) {
        Optional<Book> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw new BookNotFoundException("Book with id " + id + " is not found.");
        }

        EntityModel<Book> entityModel = EntityModel.of(optional.get());

        WebMvcLinkBuilder linkToBooks =
                linkTo(methodOn(BookController.class).findBooks());
        entityModel.add(linkToBooks.withRel("all-books"));

        return entityModel;
    }

    @GetMapping
    public Iterable<Book> findBooks() {
        return repository.findAll();
    }

    /**
     * Filter book.
     * @param pageable
     *      the pageable object
     * @return Page
     */
    @GetMapping("/filter")
    public Page<Book> filterBook(@ParameterObject Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(
            @PathVariable("id") final Long id,
            @RequestBody final Book book) {
        return checkAndUpdate(id, book);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book patchBook(
            @PathVariable("id") final Long id,
            @RequestBody final Book book) {
        return checkAndUpdate(id, book);
    }

    private Book checkAndUpdate(Long id, Book book) {
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

    @PostMapping
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
    public Long deleteBook(@PathVariable final Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return id;
        } 
        return null;
    }
}
