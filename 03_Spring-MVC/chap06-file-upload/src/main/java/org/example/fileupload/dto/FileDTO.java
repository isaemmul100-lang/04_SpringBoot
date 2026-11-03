package org.example.fileupload.dto;

import java.util.Objects;

public class FileDTO {

    private String originalFileName;
    private String savedName;
    private String filePath;
    private String fileDescription;

    public FileDTO() {
    }

    public FileDTO(String originalFileName, String savedName, String filePath, String fileDescription) {
        this.originalFileName = originalFileName;
        this.savedName = savedName;
        this.filePath = filePath;
        this.fileDescription = fileDescription;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDTO fileDTO = (FileDTO) o;
        return Objects.equals(originalFileName, fileDTO.originalFileName) && Objects.equals(savedName, fileDTO.savedName) && Objects.equals(filePath, fileDTO.filePath) && Objects.equals(fileDescription, fileDTO.fileDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalFileName, savedName, filePath, fileDescription);
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "originalFileName='" + originalFileName + '\'' +
                ", savedName='" + savedName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileDescription='" + fileDescription + '\'' +
                '}';
    }

}
