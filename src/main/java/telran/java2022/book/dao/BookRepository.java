package telran.java2022.book.dao;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;

public interface BookRepository extends CrudRepository<Book, String> {

//    Stream<Book> findBooksByBookAuthors(String author);

    Stream<Book> findByPublisher(String publisher);

//    Stream<Author> findBookAuthors(String isbn);
    
//    List<String> findPublishersByAuthors(String authorName);

}
