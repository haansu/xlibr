package com.xlibrpkg;

public class BookData {
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
