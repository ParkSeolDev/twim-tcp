package jpabook.jpashop.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter @Setter
public class BookForm {

//    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public static BookForm of(BookForm form) {
        BookForm bookForm = new BookForm();
        bookForm.setAuthor(form.getAuthor());
        bookForm.setIsbn(form.isbn);
        bookForm.setName(form.name);
        bookForm.setPrice(form.price);
//        bookForm.setId(form.getId());
        bookForm.setStockQuantity(form.getStockQuantity());
        return bookForm;
    }
}

