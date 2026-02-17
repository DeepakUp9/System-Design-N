package com.library.models;

import java.util.Objects;

/**
 * Entity representing an author of books.
 * Contains author metadata and biographical information.
 */
public class Author {
    private final String authorId;
    private String name;
    private String description;
    private String biography;

    public Author(String authorId, String name, String description) {
        this.authorId = authorId;
        this.name = name;
        this.description = description;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(authorId, author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId='" + authorId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
