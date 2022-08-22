package com.mealfit.common.storageService;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    List<String> uploadMultipartFile(List<MultipartFile> file, String dirName);

    void deleteOne(String lastImage);
}
