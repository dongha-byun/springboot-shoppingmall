package springboot.shoppingmall.product.presentation;

import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ThumbnailController {
    private final ThumbnailFileService thumbnailFileService;

    @GetMapping("/thumbnail/{fileName}")
    public Resource getThumbnail(@PathVariable("fileName") String fileName) throws MalformedURLException {
        return new UrlResource(thumbnailFileService.getRealFilePath(fileName));
    }
}
