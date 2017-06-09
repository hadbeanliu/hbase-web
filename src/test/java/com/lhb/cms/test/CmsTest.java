package com.lhb.cms.test;

public class CmsTest {


	public static void main(String[] args) {
		
		String str="abc fef,fdfs";
		
		for(String s:str.split(",| ")){
			System.out.println(s);
		}
		
		
	}

}
