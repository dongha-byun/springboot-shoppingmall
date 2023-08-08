package springboot.shoppingmall.product.infra;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.controller.ThumbnailFileService;
import springboot.shoppingmall.product.service.ThumbnailInfo;

@Component
public class LocalThumbnailFileService implements ThumbnailFileService {

    private static final String THUMBNAIL_PATH = "/Users/byundongha/byun/spring/file_dir/shopping_upload/image/";

    @Override
    public ThumbnailInfo save(MultipartFile file) throws IOException {
        String storedThumbnailName = "";
        String viewThumbnailName = "";
        if(!file.isEmpty()) {
            viewThumbnailName = file.getOriginalFilename();
            storedThumbnailName = UUID.randomUUID().toString();
            file.transferTo(new File(THUMBNAIL_PATH + storedThumbnailName));
        }

        return new ThumbnailInfo(storedThumbnailName, viewThumbnailName);
    }

    @Override
    public String getRealFilePath(String fileName) {
        return "file" + THUMBNAIL_PATH + fileName;
    }
}
