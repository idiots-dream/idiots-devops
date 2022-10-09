package com.idiots.devops.entity;

import cn.hutool.core.io.FileUtil;
import lombok.Data;

/**
 * @author blue-light
 * Date 2022-10-09
 * Description
 */
@Data
public class FileEntry {
    private String path;

    private FileType type;

    private String name;

    private Long size;

    private String sizeStr;

    private String createTime;

    private String updateTime;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;

        this.setSizeStr(FileUtil.readableFileSize(size));
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
