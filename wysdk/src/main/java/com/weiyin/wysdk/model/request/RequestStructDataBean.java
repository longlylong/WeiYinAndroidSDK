package com.weiyin.wysdk.model.request;

import com.google.gson.annotations.Expose;
import com.weiyin.wysdk.WYSdk;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 请求添加数据
 * Created by King on 2016/3/30 0030.
 */
public class RequestStructDataBean {

    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_TEXT = 2;
    public static final int TYPE_CHAPTER = 3;

    @Expose
    public String identity;

    /**
     * {@link WYSdk.Print_Book,WYSdk.Print_Card,WYSdk.Print_Photo,WYSdk.Print_Calendar}
     */
    @Expose
    public int bookType;

    /**
     * 第一次提交不用 分批以后第二次提交要传这个
     */
    @Expose
    public int unionId;

    @Expose
    public StructData structData;

    public static class StructData {

        @Expose
        public Cover cover;

        @Expose
        public Flyleaf flyleaf;

        @Expose
        public Preface preface;

        @Expose
        public CopyOnWriteArrayList<Block> dataBlocks;

        @Expose
        public Copyright copyright;

        @Expose
        public Cover backCover;
    }

    public static class Cover {
        @Expose
        public String title;
        @Expose
        public String subTitle;
        @Expose
        public List<Resource> coverImgs;
    }

    public static class Flyleaf {
        @Expose
        public String nick;
        @Expose
        public Resource headImg;
    }

    public static class Preface {
        @Expose
        public String text;
    }

    public static class Chapter {
        @Expose
        public String desc;//章节描述
        @Expose
        public String title;//章节名
    }

    public static class Copyright {
        @Expose
        public String author;//作者
        @Expose
        public String bookName;//书名
    }

    public static class Block {

        public int blockType;

        public boolean isSelected;

        @Expose
        public String text;//纯文本

        @Expose
        public Resource resource;//图片

        @Expose
        public Chapter chapter;//章节
    }

    public static class Resource {
        @Expose
        public String desc;//资源描述
        @Expose
        public String url;//资源地址
        @Expose
        public String lowPixelUrl;//资源地址低精度
        @Expose
        public long originaltime; //拍摄时间
        @Expose
        public int height; //原图高
        @Expose
        public int width; //原图宽
    }
}
