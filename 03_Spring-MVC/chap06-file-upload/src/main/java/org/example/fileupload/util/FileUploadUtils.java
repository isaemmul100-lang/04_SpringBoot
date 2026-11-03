package org.example.fileupload.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class FileUploadUtils {
    /*
    * @Value
    * - application.yaml 등의 설정값을 변수에 주입하는 어노테이션
    * - 설정파일의 해당 경로값을 가져와서 주입해준다.
    * */

    @Value("${file.upload.windows.path}")
    private String windowsPath;
    @Value("${file.upload.mac.path}")
    private String macPath;

    public String getUploadPath(String subdirectory) {
        String osName = System.getProperty("os.name").toLowerCase();
        String basePath;

        if (osName.contains("win")) {
            basePath = windowsPath;
        } else {
            basePath = macPath;
        }

        String uploadPath = basePath + "/" + subdirectory;
        File directory = new File(uploadPath);

        if(!directory.exists()){
            System.out.println("파일 업로드를 위한 디렉토리 생성!");

            boolean created = directory.mkdirs(); // 경로에 맞게 디렉토리 생성

            if(!created) {
                throw new RuntimeException("파일 업로드 디렉토리 생성 실패 : " + uploadPath);
            }
        }

        return uploadPath;
    }
}
