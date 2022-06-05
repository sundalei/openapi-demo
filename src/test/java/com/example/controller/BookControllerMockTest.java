package com.example.controller;

import com.example.exception.BookNotFoundException;
import com.example.model.Book;
import com.example.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookController.class})
public class BookControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository repository;

    @Test
    public void bookShouldReturnFromRepository() throws Exception {
        Long bookId = 1L;

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Spring");
        book.setAuthor("Spring");

        String response = new ObjectMapper().writeValueAsString(book);

        when(repository.findById(bookId)).thenReturn(Optional.of(book));
        this.mockMvc.perform(get("/books/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    public void bookShouldThrowException() throws Exception {
        Long bookId = 2L;
        when(repository.findById(bookId)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/books/2"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result -> assertEquals("Book with id 2 is not found.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
