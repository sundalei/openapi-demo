package com.example.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import nonapi.io.github.classgraph.json.Id;

public class Book {

    @Id
    private String id;

    /**
     * book title
     */
    @NotBlank
    @Size(max = 20)
    private String title;

    /**
     * book author
     */
    @NotBlank
    @Size(max = 30)
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
