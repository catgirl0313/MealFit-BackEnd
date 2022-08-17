package com.mealfit.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {


    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("")
    private String s3FullPath;

    public List<String> uploadFileInS3(List<MultipartFile> files, String dirName) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            uploadOne(file, dirName);
        }
        return imageUrls;
    }

    // 글 수정(+ 기존 s3에 있는 이미지 정보 삭제)
    public List<String> update(List<String> deleteImageUrls, List<MultipartFile> newFiles, String dirName) {
        //이미지 삭제 후 재업로드
        deleteFileInS3(deleteImageUrls);
        return uploadFileInS3(newFiles, dirName);
    }

    //기존 s3에 있는 기존 이미지 정보 삭제
    public void deleteFileInS3(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            if (!imageUrl.isBlank()) {
                imageUrl = imageUrl.replace(s3FullPath, "");
                boolean isExistObject = amazonS3Client.doesObjectExist(bucket, imageUrl);
                log.info("isExistObject : {}", isExistObject);
                if (isExistObject) {
                    amazonS3Client.deleteObject(bucket, imageUrl);
                }
            }
        }
    }

    private String createFileName(String fileName) {
        // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며,
        // 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    public String uploadOne(MultipartFile file, String dirName) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return uploadFile(file, dirName + fileName, objectMetadata);
    }

    private String uploadFile(MultipartFile file, String fileName, ObjectMetadata objectMetadata) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream,
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하셨습니다");
        }
    }

    //기존 이미지 삭제
    public void deleteOne(String lastImage) {
        if (!lastImage.equals("")) {
            lastImage = lastImage.replace("", "");
            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, lastImage);
            System.out.println("지워야할 url 주소 : " + lastImage);
            System.out.println("isExistObject : " + isExistObject);
            if (isExistObject) {
                amazonS3Client.deleteObject(bucket, lastImage);
            }
        }
    }


}

