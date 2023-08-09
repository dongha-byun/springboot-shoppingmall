package springboot.shoppingmall.product.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.controller.ThumbnailFileService;
import springboot.shoppingmall.product.service.ThumbnailInfo;

@TestConfiguration
public class TestFileConfiguration {

    @Bean
    public ThumbnailFileService thumbnailFileService() {
        return new ThumbnailFileService() {

            @Override
            public ThumbnailInfo save(MultipartFile file) throws IOException {
                String originalFilename = file.getOriginalFilename();
                return new ThumbnailInfo("test-stored-file-name", originalFilename);
            }

            @Override
            public String getRealFilePath(String fileName) {
                return fileName;
            }

            @Override
            public List<ThumbnailInfo> save(List<MultipartFile> images) throws IOException {
                return new ArrayList<>();
            }
        };
    }
}
