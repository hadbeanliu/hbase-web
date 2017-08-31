package com.lhb.cms.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.lionsoul.jcseg.tokenizer.core.ILexicon;

import com.lhb.data.common.DateUtils;
import com.lhb.data.common.TableUtil;

public class CmsTest {

 /**
  * 2017081419005042
getItem size....0
982918580994958
  * @param args
  */
	public static void main(String[] args) {
		System.out.println(DateUtils.format(null, new Date(1502761200000l)));
		
	}
	
	public static void test2(){
		
//		BufferedReader read=new BufferedReader(new InputStreanew FileInputStream(new File("")));
		
		
	}
	
	public static void test1(){
		
		
		int t=ILexicon.CJK_WORD;
		String word="test";
		String path="/home/hadoop/train";
		String speech="nr";
	
		if(path==null)
			path="";
		
//		File file=new File(path);
		String fileName="";
		String type="CJK_WORD";
		switch(t){
		    case ILexicon.CJK_WORD:fileName="lex-main.lex";break;
			case ILexicon.STOP_WORD:fileName="lex-stopWord.lex";type="STOP_WORD";break;
			case ILexicon.CJK_UNIT:fileName="lex-unit.lex";type="CJK_UNIT";break;
			case ILexicon.CJK_CHAR:fileName="lex-chars.lex";type="CJK_CHAR";break;
			default :throw new IllegalArgumentException("unknow type of word");
		}
//		LocalDate now=LocalDate.now();
		path=path+"/"+fileName;
		
		
		System.out.println("save path :"+ path);
		
		System.out.println("save path :"+ word);
		
		File file=new File(path);
		
			try {
				if(file.exists()){
					System.out.println("save in no exists");
				FileOutputStream out =new FileOutputStream(file, true);
				StringBuffer line=new StringBuffer();
				line.append("\n").append(word).append("/").append(speech).append("/null/null");
				out.write(line.toString().getBytes());
				out.flush();
				out.close();				
				}else {
					System.out.println("save in exists");
					FileOutputStream out =new FileOutputStream(file, true);
					StringBuffer line=new StringBuffer();
					line.append(type).append("\n").append(word).append("/").append(speech).append("/null/null");
					out.write(line.toString().getBytes());
					out.flush();
					out.close();				
					}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		
	
	}

}
