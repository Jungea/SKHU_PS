package net.skhu.etc;

import java.util.Random;

// User의 auth_key를 랜덤으로 생성하기 위해서 만든 클래스
public class TempAuth_key { 

    private boolean lowerCheck;
    private int size;

    public String getKey(int size, boolean lowerCheck) {
        this.size = size;
        this.lowerCheck = lowerCheck;
        return init();
    }

    private String init() {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        do {
            num = ran.nextInt(75)+48;
            if((num>=48 && num<=57) || (num>=65 && num<=90) || (num>=97 && num<=122)) {
                sb.append((char)num);
            }else {
                continue;
            }
        } while (sb.length() < size);
        if(lowerCheck) {
            return sb.toString().toLowerCase();
        }
        return sb.toString();
    }

}
