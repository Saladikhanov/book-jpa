package telran.java2022.book.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "isbn")
@Entity
@Table(name = "BOOK")
public class Book implements Serializable {

    private static final long serialVersionUID = 7934730430500833561L;
    @Id
    String isbn;
    @Column(name = "TITLE")
    String title;
    @ManyToMany
    @JoinTable(
	    name = "BOOK_AUTHORS",
	    joinColumns = @JoinColumn(name = "BOOK_ISBN"),
	    inverseJoinColumns = @JoinColumn(name = "AUTHORS_NAME")
	    
	    )
    Set<Author> authors;
    @ManyToOne
    Publisher publisher;
}
