package springboot.shoppingmall.product.presentation;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.application.ThumbnailInfo;

public interface ThumbnailFileService {
    ThumbnailInfo save(MultipartFile file) throws IOException;

    String getRealFilePath(String fileName);

    List<ThumbnailInfo> save(List<MultipartFile> images) throws IOException;
}
