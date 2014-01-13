package com.eroreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Base64;

public class Secutiry {
	
	private static String decode(String crc){
		String ans = "";
		for (int i = 0; i < crc.length(); i++){
			ans += (char) (crc.charAt(i) - i);    
		}
		return ans;
	}
	
	public static String checkCRC(Context context){
		ZipFile zf;
		
		try {
			zf = new ZipFile(context.getPackageCodePath());
			ZipEntry ze = zf.getEntry("classes.dex");
			long crc = Long.parseLong(decode(context.getString(R.string.crc)));
			if (ze.getCrc() != crc)
				return "Fucked";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK";
	}
	
	public static String checkD(Context context){
		boolean isDebuggable = (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
		if (isDebuggable )
			return "Fucked";
		return "OK";
	}
	
	public static String checkMD5(Context context) {
		String apkPath = context.getPackageCodePath();
		MessageDigest msgDigest = null;
		byte[] digest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
		    e1.printStackTrace();
		}
		   
		byte[] bytes = new byte[8192];
		int byteCount;
		FileInputStream fis = null;
		   
		try {
			fis = new FileInputStream(new File(apkPath));
		 
			while ((byteCount = fis.read(bytes)) > 0)
				msgDigest.update(bytes, 0, byteCount);
		  		digest = msgDigest.digest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		String md5 = decode(context.getString(R.string.md5));
		 
		if ( Arrays.equals(Base64.decode(md5, Base64.DEFAULT), digest) )
			return "OK";
		else
			return "Fucked";
		}
}
