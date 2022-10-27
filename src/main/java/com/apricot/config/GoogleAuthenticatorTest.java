package com.apricot.config;

import java.io.IOException;

import com.apricot.util.QRGenUtils;

public class GoogleAuthenticatorTest {
 
    public void genSecretTest() {// 生成密钥
    	String secret = GoogleAuthenticator.generateSecretKey();
        // 把这个qrcode生成二维码，用google身份验证器扫描二维码就能添加成功
        String qrcode = GoogleAuthenticator.getQRBarcode("1002674966@qq.com", secret);
        System.out.println("qrcode:" + qrcode + ",key:" + secret);
        try {
			System.out.println(QRGenUtils.execute(qrcode, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * 对app的随机生成的code,输入并验证
     */
    public void verifyTest(String secret, long code) {
        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);
        boolean r = ga.check_code(secret, code, t);
        System.out.println("检查code是否正确？" + r);
    }

    public static void main(String[] args) {
//    	new Test().genSecretTest();
    	String secret="LTKUKCAQV4O73EPD";
    	new GoogleAuthenticatorTest().verifyTest(secret, 125606);
	}
}
