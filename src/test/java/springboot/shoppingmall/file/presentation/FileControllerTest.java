package springboot.shoppingmall.file.presentation;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.file.presentation.request.FileSaveRequest;

@WebMvcTest(controllers = FileController.class)
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("파일을 저장하고, 저장된 경로를 반환한다.")
    void save() throws Exception {
        // given
        FileSaveRequest fileSaveRequest = new FileSaveRequest("content/temp/1709220301");
        String requestBody = objectMapper.writeValueAsString(fileSaveRequest);
        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                requestBody.getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "임시저장 파일 명",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "temp-file-image-content".getBytes()
        );

        // when & then
        mockMvc.perform(multipart("/files/temp")
                        .file(data)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.realFileName", notNullValue()))
                .andExpect(jsonPath("$.storeFileName", notNullValue()));
    }
}