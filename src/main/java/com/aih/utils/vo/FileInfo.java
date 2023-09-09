package com.aih.utils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String fileName;
    private long fileLength;
    private String fileSize;
    private String lastModifiedTime;
}
