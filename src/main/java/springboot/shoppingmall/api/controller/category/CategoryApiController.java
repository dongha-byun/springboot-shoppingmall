package springboot.shoppingmall.api.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.dto.category.CategoryRequest;
import springboot.shoppingmall.dto.category.CategoryResponse;
import springboot.shoppingmall.service.category.CategoryService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity addCategory(@RequestBody CategoryRequest categoryRequest){
        Long categoryId = categoryService.saveCategory(categoryRequest);
        return ResponseEntity.ok(categoryId);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity getCategory(@PathVariable("id") Long id){
        CategoryResponse categoryResponse = categoryService.findCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }
}
