package com.openlab.publicopinion.DataClassify.Sentiment;

import java.util.LinkedList;

import com.openlab.publicopinion.DataClassify.KeyWord;

/**
 * �����·ָ�ɾ��ӵĸ�����
 * <p>ͬʱ�ִʱ���������Ϣ
 * @author HuHaixiao
 *
 */
public class SentenceSpliter {
	private static String[] SentSpliter={"/wj","/ww","/wt","/wd","/wf"};
	
	/**
	 * ���ؾ��Ӿ���,���ָ���ȥ����
	 * @param text
	 * @return
	 */
	public String[][] getSentences(String text){
		KeyWord words=new KeyWord();
		LinkedList<String[]> sent=new LinkedList<String[]>();
		String[] re=words.WordsExtract(text, null, true);
		LinkedList<String> temp=new LinkedList<String>();
		int i,j;
		outer:for (i = 0; i < re.length; i++) {
						for (j = 0; j < SentSpliter.length; j++) {//����ÿ���ָ���
							if (re[i].endsWith(SentSpliter[j]) && !temp.isEmpty()){//�Ǿ��ӵķָ���,�Ҿ��Ӳ�Ϊ��
								sent.add(temp.toArray(new String[temp.size()]));
								temp.clear();
								continue outer;//�������ѭ��
							}
						}
						//���еĳɷ�
						temp.add(re[i]);
		}
		return sent.toArray(new String[sent.size()][]);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String text="û���뵽��������ط�.��Ȼ�з��������õı��ݡ��Ƶ���Ա����ѵ�Ǻܵ�λ�ˡ�" +
				"������Ҿ�������һЩ���Ǽ��Ƶ����ˡ���Ȼ����ί�д����İ���.ȱ���������ס����" +
				"������,��ʩ�е�¾�.��ͨ�ܱ���.�ӾƵ경��5���Ӿ͵�������Ŀ����С��һ����.";
		SentenceSpliter SS=new SentenceSpliter();
		String[][] re=SS.getSentences(text);
		for (int i = 0; i < re.length; i++) {
			for (int j = 0; j < re[i].length; j++) {
				System.out.print(re[i][j]);
			}
			System.out.println();
		}
	}

}
