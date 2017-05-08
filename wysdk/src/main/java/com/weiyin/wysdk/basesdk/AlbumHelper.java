package com.weiyin.wysdk.basesdk;

import com.weiyin.wysdk.WYSdk;

/**
 * 相册的
 * Created by King on 2017/5/2 0002.
 */

public class AlbumHelper {

    public static boolean checkPhotoCount(int photoCount, int bookType, int makeType) {

        photoCount += 2;//加封面封底

        if (bookType == WYSdk.BookType_Big
                || bookType == WYSdk.BookType_Card) {

            if (makeType == WYSdk.MakeType_28P
                    || makeType == WYSdk.MakeType_28P_B
                    || makeType == WYSdk.MakeType_28P_M
                    || makeType == WYSdk.MakeType_28P_M_B) {

                if (photoCount >= 30 && photoCount <= 86) {
                    return false;
                }
            } else if (photoCount >= 20 && photoCount <= 999) {
                return false;
            }

        } else if (bookType == WYSdk.BookType_Photo) {

            if (photoCount >= 16 && photoCount <= 999) {
                return false;
            }

        } else if (bookType == WYSdk.BookType_Calendar) {

            //没封底-1
            if (photoCount - 1 >= 13 && photoCount - 1 <= 25) {
                return false;
            }

        } else if (bookType == WYSdk.BookType_A4) {

            if (makeType == WYSdk.MakeType_A4_D) {
                if (photoCount >= 24 && photoCount <= 62) {
                    return false;
                }
            } else {
                if (photoCount >= 26 && photoCount <= 82) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int[] photoRange(int bookType, int makeType) {

        int[] range = new int[2];
        if (bookType == WYSdk.BookType_Big
                || bookType == WYSdk.BookType_Card) {

            if (makeType == WYSdk.MakeType_28P
                    || makeType == WYSdk.MakeType_28P_B
                    || makeType == WYSdk.MakeType_28P_M
                    || makeType == WYSdk.MakeType_28P_M_B) {

                range[0] = 30;
                range[1] = 86;
            } else {
                range[0] = 20;
                range[1] = 999;
            }

        } else if (bookType == WYSdk.BookType_Photo) {

            range[0] = 16;
            range[1] = 999;

        } else if (bookType == WYSdk.BookType_Calendar) {

            range[0] = 13;
            range[1] = 25;

        } else if (bookType == WYSdk.BookType_A4) {

            if (makeType == WYSdk.MakeType_A4_D) {
                range[0] = 24;
                range[1] = 82;
            } else {
                range[0] = 26;
                range[1] = 82;
            }
        }

        return range;
    }

}
