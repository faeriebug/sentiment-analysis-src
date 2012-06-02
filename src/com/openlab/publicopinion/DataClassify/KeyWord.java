package com.openlab.publicopinion.DataClassify;


import ICTCLAS.kevin.zhang.ICTCLAS2011;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
/**
 * ���ķִʸ�����
 * @author HuHaixiao
 *
 */
public class KeyWord {

	/**
	 * �ִ�,���ϲ��ظ��Ĵ��
	 * ��֤ԭ�ı������˳��
	 * @param sInput ���ִʵ��ı��ַ���
	 * @param wf �������������Ϊnull,��ֱ�ӽ����Ｐ�������Ϣ
	 * @param posTagged �Ƿ񽫴��Ա��һͬ�����ڽ����
	 * @return �ִʵĽ��
	 * @throws IOException
	 */
	public String[] WordsExtract(String sInput,WordFilter wf,boolean posTagged) 
	{
			String nativeStr="";
			try {
				nativeStr = WordsSplit(sInput);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			nativeStr.trim();
			String[] tempString=nativeStr.split("[\\s��]+");//ȥ�������ո� ȫ�ǿո񣬰�ǿո�
			ArrayList<String> wordDiff=new ArrayList<String>(tempString.length);
			for(int i=0;i<tempString.length-1;i++)
			{
				String[] word=tempString[i].trim().split("/");
				if(word.length!=2)//���ִַʴ���
					continue;			//��������
				if(wf==null || !wf.isStoped(word[0], word[1])){//������Ϊ�ջ���δ��ͣ�ã�������б�
					if(posTagged)
						wordDiff.add(tempString[i]);//�����Ｐ����Լ����б�
					else
						wordDiff.add(word[0]);
				}
			}	
					
			return wordDiff.toArray(new String[wordDiff.size()]);
	}

	/**
	 * ���ı��ַ�����ȡ�ʣ��ϲ��ظ��Ĵ�,�޷���֤ԭ�ı������˳��
	 * @param sInput �ı��ַ���
	 * @param wf ���������
	 * @param posTagged �Ƿ񽫴��Ա��һͬ�����ڽ����
	 * @return ��������
	 * @throws IOException
	 */
	public String[] WordsExtractMerge(String sInput,WordFilter wf,boolean posTagged) 
	{
			String nativeStr="";
			try {
				nativeStr = WordsSplit(sInput);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			nativeStr.trim();
			String[] tempString=nativeStr.split("[\\s��]+");//ȥ�������ո�
			HashSet<String> wordDiff=new HashSet<String>(tempString.length);
			for(int i=1;i<tempString.length-1;i++)
			{
				String[] word=tempString[i].trim().split("/");
				if(word.length!=2)
					continue;
				//��ȡ���ʡ����ʣ���ȥ ����"��"������"��"�����򶯴ʣ������ݴʡ�����ʡ�״̬��
				if(wf==null || !wf.isStoped(word[0], word[1])){//������Ϊ�ջ���δ��ͣ�ã�������б�
					if(posTagged)
						wordDiff.add(tempString[i]);//�����Ｐ����Լ����б�
					else
						wordDiff.add(word[0]);
				}
			}	

			return wordDiff.toArray(new String[wordDiff.size()]);
	}
	
	/**
	 * ֱ�ӵ���ICTCLAS�ִ�,�������Ϊ�ַ���
	 * @param sInput
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String WordsSplit(String sInput) throws UnsupportedEncodingException{
		ICTCLAS2011 testICTCLAS30 = new ICTCLAS2011();

		String argu = "./lib/libICTCLAS";
		if (ICTCLAS2011.ICTCLAS_Init((argu+"\0").getBytes(FileEncode.charset),0) == false)
		{
			System.out.println("Init Fail!");
			return null;
		}
		testICTCLAS30.ICTCLAS_SetPOSmap(0);

		byte nativeBytes[] = testICTCLAS30.ICTCLAS_ParagraphProcess((sInput+"\0").getBytes(FileEncode.charset), 1);
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length, FileEncode.charset);
		
		ICTCLAS2011.ICTCLAS_Exit();
		
		return nativeStr;
		
	}
	
	public static void main(String[] args) {
		String text="��̺���ߣ������µĵ���͹������˯�úܲ�����������ܲ�ܳ����������жξ��룬 ���Ǻܷ��� ���Ҿ������͸��"+
		"������� 2007��7��2�� �� �Ƶ����ۺ��� �ܰ� ����ȥ�����Ժ� ��ʧ������ Ϊʲô��������ô�࣬ ��������ס�������סһ����˷���";
				
		KeyWord words = new KeyWord();
		String[] re=words.WordsExtract(text, null,true);
		for (int i = 0; i < re.length; i++) {
			System.out.println(re[i]);
		}
//		String sInput="����helloworlf!,,";
//		try {
//			for (byte b : sInput.getBytes("utf8")) {
//			System.out.println(b);
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
}
