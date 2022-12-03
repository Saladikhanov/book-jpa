package telran.java2022.book.service;

import java.util.Set;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.book.dao.AuthorRepository;
import telran.java2022.book.dao.BookRepository;
import telran.java2022.book.dao.PublisherRepository;
import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.dto.BookDto;
import telran.java2022.book.exceptions.EntityNotFoundException;
import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    final BookRepository bookRepository;
    final AuthorRepository authorRepository;
    final PublisherRepository publisherRepository;
    final ModelMapper modelMapper;

    @Override
    @Transactional
    public boolean addBook(BookDto bookDto) {
	if(bookRepository.existsById(bookDto.getIsbn())) {
	    return false;
	}
	//Publisher
	Publisher publisher = publisherRepository.findById(bookDto.getPublisher()).orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
	Set<Author> authors = bookDto.getAuthors().stream()
									.map(a -> authorRepository.findById(a.getName()).orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
									.collect(Collectors.toSet());
	Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
	bookRepository.save(book);
	return true;
    }

    @Override
    public BookDto findBookByIsbn(String isbn) {
	Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
	return modelMapper.map(book, BookDto.class);
    }

    @Override
    @Transactional
    public BookDto removeBook(String isbn) {
	Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
	BookDto bookDto = modelMapper.map(book, BookDto.class);
	bookRepository.delete(book);
	return bookDto;
    }

    @Override
    @Transactional
    public BookDto updateBook(String isbn, String title) {
	Book book = bookRepository.findById(isbn).orElseThrow(() -> new EntityNotFoundException());
	book.setTitle(title);
//	bookRepository.save(book);
	return modelMapper.map(book, BookDto.class);
    }

    @Override
//    @Transactional(readOnly=true)
    public Iterable<BookDto> findBooksByAuthor(String authorName) {
	Author author = authorRepository.findById(authorName).orElseThrow(() -> new EntityNotFoundException());
	return author.getBooks().stream()
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
		//	return bookRepository.findBooksByAuthorsName(authorName).map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toList());
    }

    @Override
//    @Transactional(readOnly = true)
    public Iterable<BookDto> findBooksByPublisher(String publisherName) {
	Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(() -> new EntityNotFoundException());
	return publisher.getBooks().stream()
					.map(b -> modelMapper.map(b, BookDto.class))
					.collect(Collectors.toList());
//		.collect(Collectors.toList());
//	return bookRepository.findByPublisherPublisherName(publisherName).map(b -> modelMapper.map(b, BookDto.class))
//													.collect(Collectors.toList());
	
    }

    @Override
    public Iterable<AuthorDto> findBookAuthors(String isbn) {
	Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
	return book.getAuthors().stream()
							.map(a -> modelMapper.map(a, AuthorDto.class))
							.collect(Collectors.toList());
		}

    @Override
    @Transactional
    public Iterable<String> findPublishersByAuthor(String authorName) {
	return publisherRepository.findDistinctByBooksAuthorsName(authorName).map(Publisher::getPublisherName).collect(Collectors.toList());
//	return publisherRepository.findPublishersByAuthor(authorName);
    }

    @Override
    @Transactional
    public AuthorDto removeAuthor(String authorName) {
	Author author = authorRepository.findById(authorName).orElseThrow(() -> new EntityNotFoundException());
//	bookRepository.findBooksByAuthorsName(authorName).forEach(b -> bookRepository.delete(b));
	authorRepository.delete(author);
	return modelMapper.map(author, AuthorDto.class);
    }

}
