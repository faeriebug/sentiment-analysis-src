package com.openlab.publicopinion.DataClassify.Sentiment;

import java.io.*;
import java.util.*;

import com.openlab.publicopinion.DataClassify.FileEncode;

/**
 * ��дʵ䣬��������/���� ���/���۴���
 * 
 * @author HuHaixiao
 * 
 */
public class SentiDictionary {
	public String negPath, degPath, sentPath, vsiPath;

	// �񶨴�
	private Set<String> neg = null;
	// �̶ȸ���
	private Map<String, Integer> deg = null;
	// ��д���
	private Set<String> senti = null;
	// VSI����
	private Set<String> vsi = null;

	public SentiDictionary() {
		this("Test\\SentimentTest\\SentiDictionary\\negDic.txt",
				"Test\\SentimentTest\\SentiDictionary\\degreeDic.txt",
				"Test\\SentimentTest\\SentiDictionary\\sentDic.txt",
				"Test\\SentimentTest\\SentiDictionary\\VSIDic.txt");
	}

	public SentiDictionary(String negPath, String degPath, String sentPath,
			String vsiPath) {
		this.negPath = negPath;
		this.degPath = degPath;
		this.sentPath = sentPath;
		this.vsiPath = vsiPath;
	}

	/**
	 * ��дʵ������
	 * 
	 * @param path
	 */
	public void constructDict(String path) {// ����֪������дʵ�Ľ���

	}

	/**
	 * ��ȡ�񶨴ʵ�
	 * 
	 * @return
	 */
	public Set<String> getNeg() {
		if (neg == null) {
			File negDic = new File(negPath);
			int siz_Neg;
			String text;
			try {
				// ���ط񶨴ʴʵ�
				InputStreamReader in = new InputStreamReader(
						new FileInputStream(negDic), FileEncode.charset);
				BufferedReader br = new BufferedReader(in);
				text = br.readLine();
				if (text == null)
					return null;
				siz_Neg = Integer.parseInt(text);
				neg = new HashSet<String>(siz_Neg);
				while ((text = br.readLine()) != null)
					neg.add(text.trim());
				br.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return neg;
	}

	/**
	 * ��ȡ�̶ȸ��ʵ�
	 * 
	 * @return
	 */
	public Map<String, Integer> getDeg() {
		if (deg == null) {
			try {
				File degDic = new File(degPath);
				int siz_Deg;
				String text;
				InputStreamReader in = new InputStreamReader(
						new FileInputStream(degDic), FileEncode.charset);
				BufferedReader br = new BufferedReader(in);
				// ���س̶ȸ��ʴʵ�

				text = br.readLine();
				siz_Deg = Integer.parseInt(text);
				deg = new HashMap<String, Integer>(siz_Deg);
				int n;
				for (int i = 0; i < 4; i++) {
					text = br.readLine();
					n = Integer.parseInt(text);
					for (int j = 0; j < n; j++) {
						text = br.readLine();
						deg.put(text.trim(), 4 - i);// ����Խ�󣬵ȼ�Խ��
					}
				}
				br.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deg;

	}

	/**
	 * ��ȡ��дʵ�
	 * 
	 * @return
	 */
	public Set<String> getSenti() {
		if (senti == null) {
			File sentDic = new File(sentPath);
			int siz_Sent;
			try {
				String text;
				// ���ط񶨴ʴʵ�

				// ������дʵ�
				InputStreamReader in = new InputStreamReader(
						new FileInputStream(sentDic), FileEncode.charset);
				BufferedReader br = new BufferedReader(in);
				text = br.readLine();
				if (text == null)
					return null;
				siz_Sent = Integer.parseInt(text);
				senti = new HashSet<String>(siz_Sent);
				while ((text = br.readLine()) != null) {
					senti.add(text.trim());
				}
				br.close();
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return senti;

	}

	/**
	 * ��ȡVSI�ʵ�
	 * 
	 * @return
	 */
	public Set<String> getVSI() {
		if (vsi == null) {
			File vsiDic = new File(vsiPath);
			int siz_Vsi;
			try {
				String text;
				// ���ط񶨴ʴʵ�

				// ����VSI�ʵ�
				InputStreamReader in = new InputStreamReader(
						new FileInputStream(vsiDic), FileEncode.charset);
				BufferedReader br = new BufferedReader(in);
				text = br.readLine();
				if (text == null)
					return null;
				siz_Vsi = Integer.parseInt(text);
				vsi = new HashSet<String>(siz_Vsi);
				while ((text = br.readLine()) != null) {
					vsi.add(text.trim());
				}
				br.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vsi;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SentiDictionary SD = new SentiDictionary();
		System.out.println(SD.getSenti().contains("����"));
	}

}
