package com.openlab.publicopinion.DataClassify.Sentiment;

import java.util.ArrayList;

import com.openlab.publicopinion.DataClassify.FeatureMaker;
import com.openlab.publicopinion.DataClassify.Sentiment.negDegProces.ndpCls1;

/**
 * �Ӿ����л�ȡ��������
 * ������̰�����
 * �񶨴ʡ��̶ȸ��ʴ���
 * ��дʵ����
 * @author HuHaixiao
 *
 */
public class SemanticFeatureMaker implements FeatureMaker{
	//��дʵ�
	private SentiDictionary SD=null;
	private negDegProc ndProc;
	private SentenceSpliter SS=new SentenceSpliter();
	
	public SemanticFeatureMaker(SentiDictionary SD){
		this.SD=SD;
		ndProc=new ndpCls1(SD);
	}
	

	@Override
	public String[] getFeatureFromDoc(String text) {
		// TODO Auto-generated method stub
		String[][] sents=SS.getSentences(text);
		ArrayList<String> lisF=new ArrayList<String>();
		for (String[] sent : sents) {
			lisF.addAll(getFeatures(sent));
		}
		return lisF.toArray(new String[lisF.size()]);
	}

	@Override
	public int estimateMapSize() {
		// TODO Auto-generated method stub
		return SD.getSenti().size();
	}

	
	public ArrayList<String> getFeatures(String[] sent){
		ArrayList<String> lisD=new ArrayList<String>();
		String[] re;
		//���Ƚ��и��ʴ���
		re=ndProc.match(sent);//ʶ��ƥ��
		for (String f : re) {
			lisD.add(f);
		}
		return lisD;		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SentiDictionary SD=new SentiDictionary();
		SentenceSpliter SS=new SentenceSpliter();
		SemanticFeatureMaker SF=new SemanticFeatureMaker(SD);
		String text="��̺���� ���|�µď���͹���� ˯�úܲ���� �����ܲ� �ܳ� �x�Ѕ^�жξ��x ���Ǻܷ��� ���X�����͸��"+
"������� 2007��7��2�� �� �Ƶ����ҋ���� �ܰ� ���Mȥ���g���� ��ʧ���� ��ʲ�N�Y������@�N�� �������ס���� ���סһ����˷���";
		String[][] sents=SS.getSentences(text);
		for (String[] s : sents) {
			for (String f : SF.getFeatures(s)) {
				System.out.print(f+"  ");
			}
		}
	}

}
