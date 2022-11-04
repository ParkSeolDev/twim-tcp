package jpabook.jpashop.api.controller;

import io.swagger.annotations.Api;
import jpabook.jpashop.api.request.BookForm;
import jpabook.jpashop.api.response.UserRes;
import jpabook.jpashop.api.service.ItemService;
import jpabook.jpashop.db.entity.item.Book;
import jpabook.jpashop.db.entity.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "아이템 API", tags = {"Item"})
@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/new")
    public void create(@RequestBody BookForm form) {

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
    }

    @GetMapping("/item-list")
    public ResponseEntity<List<Item>> itemList() {
        List<Item> items = itemService.findItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Item> findItem(@RequestParam Long id) {
        Item item = itemService.findOne(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<BookForm> updateItemForm(@PathVariable("itemId") Long itemId) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
//        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        return ResponseEntity.status(200).body(BookForm.of(form));
    }

    @PostMapping("/{itemId}/edit")
    public ResponseEntity<String> updateItem(@PathVariable Long itemId, @RequestBody BookForm form) {

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

        return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    }
}
