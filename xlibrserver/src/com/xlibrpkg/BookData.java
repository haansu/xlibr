package com.xlibrpkg;

import java.io.Serializable;

public class BookData implements Serializable {
	public int id;
	public String title;
	public String author;
	public String publisher;
	public String synopsis;
	public int releaseYear;

	public BookData () {
		id = -1;
		title = null;
		author = null;
		publisher = null;
		synopsis = null;
		releaseYear = -1;
	}

	public BookData(int id, String title, String author, String publisher, String synopsis,int releaseYear) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.synopsis = synopsis;
		this.releaseYear = releaseYear;
	}

	@Override
	public String toString() {
		return "BookData{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", publisher='" + publisher + '\'' +
				", synopsis='" + synopsis + '\'' +
				", releaseYear=" + releaseYear +
				'}';
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTitle (String title) {
		this.title = title;
	}

	public void setAuthor (String author) {
		this.author = author;
	}

	public void setPublisher (String publisher) {
		this.publisher = publisher;
	}

	public void setSynopsis (String synopsis) {
		this.synopsis = synopsis;
	}

	public void setReleaseYear (int releaseYear) {
		this.releaseYear = releaseYear;
	}
}
