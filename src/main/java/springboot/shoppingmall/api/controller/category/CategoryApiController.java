package springboot.shoppingmall.api.controller.category;

import java.net.URI;
import java.net.URISyntaxException;
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
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest){
        CategoryResponse categoryResponse = categoryService.saveCategory(categoryRequest);
        try {
            return ResponseEntity.created(new URI("/category/"+categoryResponse.getId())).body(categoryResponse);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable("id") Long id){
        CategoryResponse categoryResponse = categoryService.findCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }
}
