package springboot.shoppingmall.file.presentation;

import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.file.presentation.response.FileSaveResponse;
import springboot.shoppingmall.file.util.FileHandler;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileHandler fileHandler;

    @PostMapping("/files/temp")
    public ResponseEntity<FileSaveResponse> tempFileSave(@RequestPart(name = "file") MultipartFile file) {
        String storeFileName = fileHandler.saveTempContentImageFile(file);
        String tempContentImageURI = "/content/img/temp/" + storeFileName;
        return ResponseEntity.ok(new FileSaveResponse(tempContentImageURI));
    }

    @GetMapping("/content/img/temp/{storedFileName}")
    public Resource getTempContentImage(@PathVariable("storedFileName") String storeFileName) throws MalformedURLException {
        String tempContentImagePath = fileHandler.getTempContentImagePath(storeFileName);
        return new UrlResource("file://" + tempContentImagePath);
    }

    @GetMapping("/content/img/prod/{storedFileName}")
    public Resource getProdContentImage(@PathVariable("storedFileName") String storeFileName) throws MalformedURLException {
        String prodContentImagePath = fileHandler.getProdContentImagePath(storeFileName);
        return new UrlResource("file://" + prodContentImagePath);
    }

}
