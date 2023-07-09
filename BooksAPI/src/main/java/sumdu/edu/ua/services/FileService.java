package sumdu.edu.ua.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    void uploadFile(MultipartFile file) throws IOException;
    ByteArrayResource downloadFile(String fileName);
    boolean deleteFile(String fileName);
    void uploadFile(InputStream is, String fileName, String contentType) throws IOException;
}
