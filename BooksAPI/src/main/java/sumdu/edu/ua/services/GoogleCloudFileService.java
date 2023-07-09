package sumdu.edu.ua.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import org.springframework.beans.factory.annotation.Value;
import com.google.cloud.storage.Storage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GoogleCloudFileService implements FileService {
    @Value("${gcp.bucket.name}")
    private String bucketName;
    Storage storage;

    public GoogleCloudFileService(Storage storage) {
        this.storage = storage;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        BlobId blobId = BlobId.of(bucketName, file.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).
                setContentType(file.getContentType()).build();
        storage.create(blobInfo,file.getBytes());
    }

    public ByteArrayResource downloadFile(String fileName) {

        Blob blob = storage.get(bucketName, fileName);

        return new ByteArrayResource(blob.getContent());
    }

    public boolean deleteFile(String fileName) {

        Blob blob = storage.get(bucketName, fileName);

        return blob.delete();
    }

    public void uploadFile(InputStream is, String fileName, String contentType) throws IOException {
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).
                setContentType(contentType).build();
        storage.create(blobInfo, is.readAllBytes());
    }
}
