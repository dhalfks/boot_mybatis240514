package com.ezen.www.handler;

import com.ezen.www.domain.FileVO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileHandler {
    private String UP_DIR = "D:\\_myProject\\_java\\_fileUpload\\";

    public List<FileVO> uploadFiles(MultipartFile[] files){
        List<FileVO> flist = new ArrayList<>();
        LocalDate date = LocalDate.now();
        String today = date.toString();
        //2024-05-14
        today = today.replace("-", File.separator);

        File folders = new File(UP_DIR, today);

        if(!folders.exists()){
            folders.mkdirs();  //여러개 폴더 생성
        }
        // ------------------ 폴더 생성 완료
        //fileVO 생성
        for(MultipartFile file : files){
            FileVO fvo = new FileVO();
            fvo.setSaveDir(today);
            fvo.setFileSize(file.getSize());

            String originalFileName = file.getOriginalFilename();
            String onlyFileName = originalFileName.substring(
                originalFileName.lastIndexOf(File.separator)+1
            );
            fvo.setFileName(onlyFileName);

            UUID uuid = UUID.randomUUID();
            String uuidStr = uuid.toString();
            fvo.setUuid(uuidStr);

            //-------------- fvo 설정 마무리
            //디스크에 저장
            String fullFileName = uuidStr+"_"+onlyFileName;
            File storeFile = new File(folders, fullFileName);

            //저장
            try {
                file.transferTo(storeFile);
                if(isImageFile(storeFile)){
                    fvo.setFileType(1);
                    File thumbnail = new File(folders, uuidStr+"_th_"+onlyFileName);
                    Thumbnails.of(storeFile).size(100,100).toFile(thumbnail);
                }
            }catch (Exception e){
                log.info("파일 설정 오류");
                e.printStackTrace();
            }
            //for문 안
            flist.add(fvo);
        }
        return flist;
    }

    private boolean isImageFile(File file) throws IOException {
        String mimeType = new Tika().detect(file);
        return mimeType.startsWith("image")? true : false;
    }

}
