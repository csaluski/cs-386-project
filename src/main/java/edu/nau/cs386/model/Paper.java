package edu.nau.cs386.model;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Paper {

    private UUID uuid;
    private String title;
    private File pdf;
    private List<String> authors;

    public Paper(String title, File pdf, List<String> authors) {
        this.title = title;
        this.pdf = pdf;
        this.authors = authors;
        this.uuid = UUID.randomUUID();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getPdf() {
        return pdf;
    }

    public void setPdf(File pdf) {
        this.pdf = pdf;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paper paper = (Paper) o;
        return Objects.equals(uuid, paper.uuid) && Objects.equals(title, paper.title) && Objects.equals(pdf, paper.pdf) && Objects.equals(authors, paper.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, pdf, authors);
    }

}
