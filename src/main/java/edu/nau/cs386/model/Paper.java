package edu.nau.cs386.model;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Paper {

    private UUID uuid;
    private String title;
    private String paperAbstract;
    private File pdf;
    private String doi;
    private List<String> authors;
    private List<UUID> owners;


    public Paper(String title, File pdf, String paperAbstract, String doi, List<String> authors, List<UUID> owners) {
        this.title = title;
        this.pdf = pdf;
        this.paperAbstract = paperAbstract;
        this.doi = doi;
        this.authors = authors;
        this.owners = owners;
        this.uuid = UUID.randomUUID();
    }


    public Paper(String title, File pdf, List<String> authors, UUID owner) {
        this(title, pdf, "", "", authors, List.of(owner));
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


    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public List<UUID> getOwners() {
        return owners;
    }

    public void setOwners(List<UUID> owners) {
        this.owners = owners;
    }


    public void addAuthor(String author) {
        this.authors.add(author);
    }

    public void addOwner(UUID owner) {
        this.owners.add(owner);
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
