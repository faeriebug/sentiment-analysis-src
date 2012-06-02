package com.openlab.publicopinion.DataClassify.Subject;

import java.util.Map;

import com.openlab.publicopinion.DataClassify.Dictionary;
import com.openlab.publicopinion.DataClassify.FeatureMaker;
import com.openlab.publicopinion.DataClassify.KeyWord;
import com.openlab.publicopinion.DataClassify.TextClsWordFilter;
import com.openlab.publicopinion.DataClassify.WordFilter;

/**
 * ���ı��л�ȡ��������
 * @author HuHaixiao
 *
 */
public class SubjectFeatureMaker implements FeatureMaker {
	/**�ֵ�Ŀ¼*/
	private  String dicPath;
	/**�����ֵ��ӳ���*/
	private  Map<String, Integer> dic;
	/**���������������*/
	private static WordFilter wf;
	
	/**
	 * ��ȡ�ʵ����е�����
	 * @param dic
	 */
	public SubjectFeatureMaker(String dictionaryPath){
		this.dicPath=dictionaryPath;
		dic=(new Dictionary()).loadDictionary(dicPath);
		wf=new TextClsWordFilter(dic);
	}
	
	/**
	 * ��ȡ�ʵ����е�����
	 * @param dic
	 */
	public SubjectFeatureMaker(Map<String,Integer> dic){
		this.dic=dic;
		wf=new TextClsWordFilter(dic);
	}
	
	/**
	 * ����ʵ䣬��ȡ���е�����
	 */
	public SubjectFeatureMaker(){
		this.dic=null;
		wf=new TextClsWordFilter();
	}

	@Override
	public String[] getFeatureFromDoc(String text) {
		// TODO Auto-generated method stub
		KeyWord words = new KeyWord();
		return words.WordsExtract(text, wf, false);
	}

	@Override
	public int estimateMapSize() {
		// TODO Auto-generated method stub
		return dic.size();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
