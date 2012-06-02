package com.openlab.publicopinion.DataClassify;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * �ֵ䴦����
 * <p>�����ֵ��ļ��ĵ���
 * <p>�����ֵ����ݵ���ӳ������ӳ���
 * @author HuHaixiao
 *
 */
public class Dictionary {
	
	/**
	 * �����ֵ�
	 * <p>�����������ӳ�����
	 * <p>�����ɴ����ַ������������š��磺����->120
	 * <p>ѡȡMap��ԭ���ǣ�����ͨ���ַ����ؼ���ֱ���ҵ�����
	 * @param dictionary �ֵ�·��
	 * @return  �ֵ���ӳ���
	 */
	public  Map<String, Integer> loadDictionary(String dictionary){
		try {
			String text;
			File file = new File(dictionary);
//			FileReader fileReader = new FileReader(file);
//			BufferedReader br = new BufferedReader(fileReader);
			InputStreamReader in=new InputStreamReader(new FileInputStream(file), FileEncode.charset);
			BufferedReader br = new BufferedReader(in);
			text = br.readLine();
			if(text==null)return null;
			int size=Integer.parseInt(text);//��ȡ�ʵ��С,��������map
			Map<String, Integer> map=new HashMap<String, Integer>(size);
			
			while ((text = br.readLine()) != null){
				String[] word=text.split(",");
				if(word.length==2)
					map.put(word[0], Integer.valueOf(word[1]));//�����map��
			}
			br.close();
			in.close();
			
			return map;
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �����ֵ�
	 * <p>�����������ӳ�����
	 * <p>�����ɴ�������������Ӧ���ַ������磺120->����
	 * @param dictionary �ֵ�·��
	 * @return  �ֵ���ӳ���
	 */
	public  String[] loadDictionaryInv(String dictionary){
		String[] allWords=null;
		try {
			String text;
			File file = new File(dictionary);
			InputStreamReader in=new InputStreamReader(new FileInputStream(file), FileEncode.charset);
			BufferedReader br = new BufferedReader(in);
			text = br.readLine();			//��ȡ�ʵ��С
			if(text==null)return null;
			int size=Integer.parseInt(text);
			allWords=new String[size];
			
			while ((text = br.readLine()) != null){
				String[] word=text.split(",");
				if(word.length==2)
					allWords[Integer.parseInt(word[1])]=word[0];
			}
			br.close();
			in.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return allWords;
	}
	
	/**
	 * ��������
	 * <p>���ļ��������ַ�������ʽ���
	 * @return ����ļ������ַ���
	 */
	public String readAll(String filePath){
		StringBuilder sb = new StringBuilder();
		try {
			String text;
			File file = new File(filePath);
			InputStreamReader in=new InputStreamReader(new FileInputStream(file), FileEncode.charset);
			BufferedReader br = new BufferedReader(in);
			
			while ((text = br.readLine()) != null)
				sb.append(text+"\n");
			br.close();
			in.close();
		} catch (IOException err) {
			System.out.println(err.toString());
			err.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] arg){
		
	}

	
}