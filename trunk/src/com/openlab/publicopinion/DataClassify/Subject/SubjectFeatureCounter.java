package com.openlab.publicopinion.DataClassify.Subject;

import java.util.HashMap;
import java.util.Map;

import com.openlab.publicopinion.DataClassify.FeatureCounter;

/**
 * ������������Ȩֵ�ļ��㣬�������������ʵ��к��е�������
 * @author HuHaixiao
 *
 */
public class SubjectFeatureCounter implements FeatureCounter {

	@Override
	public Map<String,Integer> Count(Map<String, Integer> dic, String text) {
		// TODO Auto-generated method stub
		SubjectFeatureMaker SF=new SubjectFeatureMaker(dic);
		String[] features=SF.getFeatureFromDoc(text);//�����ظ���������Ƶ��Ϊ1��
		//��������Ҫ����Ƶ��ͳ�ƣ������Ƿ���кϲ������ǣ�ֱ�Ӵ��������������ⲿ�ϲ�����
		//�ϲ�����
		Map<String,Integer> setFeature=new HashMap<String, Integer>(features.length);
		for (String f : features) {
			if (setFeature.containsKey(f)) {//�������
				setFeature.put(f, setFeature.get(f)+1);//Ƶ��+1
			}else{
				setFeature.put(f, 1);
			}
			
		}
		return setFeature;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
