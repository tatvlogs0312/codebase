package org.example.codebase.common;

import org.example.codebase.exception.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {

    /**
     * Convert MultipartFile to File
     *
     * @param file
     * @return
     */
    public static File convertFile(MultipartFile file) {
        try {
            File f = new File(System.getProperty("java.io.tmpdir") +
                                "tmp_" + UUID.randomUUID() +
                                "_" +
                                file.getName() +
                                ".jpg");
            file.transferTo(f);
            return f;
        } catch (IOException ex) {
            throw new ApplicationException("");
        }
    }

    public static File convertFileV2(MultipartFile multipartFile) {
        try {
            File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            file.createNewFile(); // Create the file if it doesn't exist
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes()); // Write the bytes of the multipart file to the file
            }
            return file;
        } catch (Exception e) {
            throw new ApplicationException("");
        }
    }

    public static File convertFileV2(byte[] bytes) {
        try {
            File file = new File("file_temp");
            file.createNewFile(); // Create the file if it doesn't exist
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes); // Write the bytes of the multipart file to the file
            }
            return file;
        } catch (Exception e) {
            throw new ApplicationException("");
        }
    }


    /**
     * Xóa File tạm đã tạo
     *
     * @param file
     */
    public static void cleanFileNotNull(File file) {
        if (file != null && file.isFile()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                throw new ApplicationException("");
            }

        }
    }

    public static InputStream getFileInputStream(MultipartFile file) throws IOException {
        return new ByteArrayInputStream(file.getBytes());
    }

    public static InputStream getFileInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
}
