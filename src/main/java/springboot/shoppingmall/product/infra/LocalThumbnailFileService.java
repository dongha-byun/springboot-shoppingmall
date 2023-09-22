package springboot.shoppingmall.product.infra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.product.presentation.ThumbnailFileService;
import springboot.shoppingmall.product.application.ThumbnailInfo;

@Component
public class LocalThumbnailFileService implements ThumbnailFileService {

    @Value("${thumbnail.local.prefix}")
    private String LOCAL_FILE_PROTOCOL_PREFIX;

    @Value("${thumbnail.local.real-path}")
    private String THUMBNAIL_REAL_PATH;

    @Override
    public ThumbnailInfo save(MultipartFile file) throws IOException {
        String storedThumbnailName = "";
        String viewThumbnailName = "";
        if(!file.isEmpty()) {
            viewThumbnailName = file.getOriginalFilename();
            storedThumbnailName = UUID.randomUUID().toString();
            file.transferTo(new File(THUMBNAIL_REAL_PATH + storedThumbnailName));
        }

        return new ThumbnailInfo(storedThumbnailName, viewThumbnailName);
    }

    @Override
    public String getRealFilePath(String fileName) {
        return LOCAL_FILE_PROTOCOL_PREFIX + THUMBNAIL_REAL_PATH + fileName;
    }

    @Override
    public List<ThumbnailInfo> save(List<MultipartFile> images) throws IOException {
        if(images == null || images.isEmpty()) {
            return new ArrayList<>();
        }

        return images.stream()
                .map(file -> {
                    try {
                        return save(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
