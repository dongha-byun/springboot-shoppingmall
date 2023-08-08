package springboot.shoppingmall.product.infra;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.controller.ThumbnailFileService;
import springboot.shoppingmall.product.service.ThumbnailInfo;

@Component
public class LocalThumbnailFileService implements ThumbnailFileService {

    @Value("${thumbnail.local.prefix}")
    private String LOCAL_FILE_PROTOCOL_PREFIX;

    @Value("${thumbnail.local.path}")
    private String THUMBNAIL_PATH;

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
        return LOCAL_FILE_PROTOCOL_PREFIX + THUMBNAIL_PATH + fileName;
    }
}
