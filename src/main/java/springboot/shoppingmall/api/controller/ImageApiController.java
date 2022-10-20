package springboot.shoppingmall.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@Slf4j
public class ImageApiController {

    private final String filePath="/Users/byundongha/byun/spring/file_dir/shopping_upload/image/";

    @GetMapping("/image/{fileName}")
    public Resource downloadImage(@PathVariable("fileName") String fileName) throws MalformedURLException {
        return new UrlResource("file:"+filePath+fileName);
    }
}
