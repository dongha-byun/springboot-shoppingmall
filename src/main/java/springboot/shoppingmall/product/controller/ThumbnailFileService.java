package springboot.shoppingmall.product.controller;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.service.ThumbnailInfo;

public interface ThumbnailFileService {
    ThumbnailInfo save(MultipartFile file) throws IOException;

    String getRealFilePath(String fileName);
}
