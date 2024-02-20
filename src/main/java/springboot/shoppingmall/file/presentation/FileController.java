package springboot.shoppingmall.file.presentation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.file.presentation.response.FileSaveResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    private static final String TEMP_FILE_DIR = "/Users/byundongha/byun/spring/file_dir/shopping_upload/temp/";

    @PostMapping("/files/temp")
    public ResponseEntity<FileSaveResponse> tempFileSave(@RequestPart(name = "file") MultipartFile file) {
        log.info("fileName = {}", file.getOriginalFilename());

        // 물리파일 저장 로직 태우기 - 임시저장은 임시경로에 물리파일만 저장한다.
        if(file.isEmpty()) {
            throw new IllegalArgumentException("파일 없음.");
        }

        try {
            String realFileName = file.getOriginalFilename();
            if(!StringUtils.hasText(realFileName)) {
                throw new IllegalArgumentException("파일명 없음.");
            }

            String replaceFileName = realFileName.replaceAll(" ", "_");
            String storeFileName = UUID.randomUUID().toString();
            String tempPath = TEMP_FILE_DIR + storeFileName;
            File tempDir = new File(tempPath);
            boolean isSuccessMkdir = tempDir.mkdir();
            if(!isSuccessMkdir) {
                throw new IllegalStateException("폴더 생성 실패. 재시도 요망");
            }

            file.transferTo(new File(tempPath + "/" + replaceFileName));
            return ResponseEntity.ok(new FileSaveResponse(replaceFileName, storeFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/files/temp/{storedFileName}/{realFileName}")
    public Resource getFile(@PathVariable("storedFileName") String storeFileName,
                            @PathVariable("realFileName") String realFileName) throws MalformedURLException {
        return new UrlResource("file://" + TEMP_FILE_DIR + storeFileName + "/" + realFileName);
    }
}
