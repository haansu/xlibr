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
}
