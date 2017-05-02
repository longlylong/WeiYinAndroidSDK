package com.weiyin.wysdk.basesdk;

import com.weiyin.wysdk.WYSdk;

/**
 * 相册的
 * Created by King on 2017/5/2 0002.
 */

public class AlbumHelper {

    public static boolean checkPhotoCount(int photoCount, int bookType) {

        photoCount += 2;//加封面封底

        if (bookType == WYSdk.Print_Book
                || bookType == WYSdk.Print_Card) {

            if (photoCount >= 20 && photoCount <= 999) {
                return false;
            }

        } else if (bookType == WYSdk.Print_Photo) {

            if (photoCount >= 16 && photoCount <= 999) {
                return false;
            }

        } else if (bookType == WYSdk.Print_Calendar) {

            //没封底-1
            if (photoCount - 1 >= 13 && photoCount - 1 <= 25) {
                return false;
            }

        } else if (bookType == WYSdk.Print_J_A5) {

            if (photoCount >= 26 && photoCount <= 82) {
                return false;
            }

        } else if (bookType == WYSdk.Print_D_A5) {

            if (photoCount >= 24 && photoCount <= 62) {
                return false;
            }


        } else if (bookType == WYSdk.Print_D_YL
                || bookType == WYSdk.Print_D_YL_M
                || bookType == WYSdk.Print_D_YL_B
                || bookType == WYSdk.Print_D_YL_M_B) {

            if (photoCount >= 30 && photoCount <= 86) {
                return false;
            }
        }

        return true;
    }

    public static int[] photoRange(int bookType) {
        
        int[] range = new int[2];
        if (bookType == WYSdk.Print_Book
                || bookType == WYSdk.Print_Card) {

            range[0] = 20;
            range[1] = 999;

        } else if (bookType == WYSdk.Print_Photo) {

            range[0] = 16;
            range[1] = 999;

        } else if (bookType == WYSdk.Print_Calendar) {

            range[0] = 13;
            range[1] = 25;

        } else if (bookType == WYSdk.Print_J_A5) {

            range[0] = 26;
            range[1] = 82;

        } else if (bookType == WYSdk.Print_D_A5) {

            range[0] = 24;
            range[1] = 82;


        } else if (bookType == WYSdk.Print_D_YL
                || bookType == WYSdk.Print_D_YL_M
                || bookType == WYSdk.Print_D_YL_B
                || bookType == WYSdk.Print_D_YL_M_B) {

            range[0] = 30;
            range[1] = 86;
        }

        return range;
    }
}
