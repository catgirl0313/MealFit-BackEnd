package com.mealfit.common.storageService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class MockStorageService implements StorageService {


    @Override
    public List<String> uploadMultipartFile(List<MultipartFile> file, String dirName) {
        return new ArrayList<>();
    }

    @Override
    public void deleteOne(String lastImage) {

    }
}

