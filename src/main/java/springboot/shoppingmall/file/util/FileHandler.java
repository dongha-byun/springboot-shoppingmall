package springboot.shoppingmall.file.util;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileHandler {
    private static final String CONTENT_IMAGE_TEMP_DIR = "/Users/byundongha/byun/spring/file_dir/shopping_upload/content/img/temp/";
    private static final String CONTENT_IMAGE_PROD_DIR = "/Users/byundongha/byun/spring/file_dir/shopping_upload/content/img/prod/";

    public String getTempContentImagePath(String storeFileName) {
        return CONTENT_IMAGE_TEMP_DIR + storeFileName;
    }

    public String getProdContentImagePath(String storeFileName) {
        return CONTENT_IMAGE_PROD_DIR + storeFileName;
    }

    public String saveTempContentImageFile(MultipartFile file) {
        return save(file, CONTENT_IMAGE_TEMP_DIR);
    }

    private String save(MultipartFile file, String path) {
        // 물리파일 저장 로직 태우기 - 임시저장은 임시경로에 물리파일만 저장한다.
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일 없음.");
        }

        try {
            String realFileName = file.getOriginalFilename();
            if(!StringUtils.hasText(realFileName)) {
                throw new IllegalArgumentException("파일명 없음.");
            }

            String storeFileName = UUID.randomUUID().toString();
            String tempPath = path + storeFileName;
            file.transferTo(new File(tempPath));
            return storeFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyContentImageTempToProd(List<String> storeFileNames) {
        storeFileNames.forEach(
                s -> {
                    try {
                        File source = new File(CONTENT_IMAGE_TEMP_DIR + s);
                        File destination = new File(CONTENT_IMAGE_PROD_DIR + s);
                        if (source.isFile()) {
                            Files.copy(source.toPath(), destination.toPath(), REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

}
