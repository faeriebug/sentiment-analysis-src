package com.openlab.publicopinion.DataClassify.FeatureSelection;

import com.openlab.publicopinion.DataClassify.FeatureMaker;

/**
 * ����ѡ��������
 * @author HuHaixiao
 *
 */
public abstract class FeatureSelection {
	public abstract String getName();
	/**
	 * ��ô˴����ڴ����Ĵ�ƪ�ĵ�������ֵ 
	 * @param wordID
	 * @param clsID
	 * @param TDM
	 * @return
	 */
	public abstract Number getFeatureForTermInClass(String wordID,int clsID,TrainingDataManager TDM);
	
	/**
	 * �����������ֵ䣬�������ڱ����ļ�
	 * <p>�ʵ��������㿪ʼ
	 * @param feature ��������
	 * @param libPath ѵ�����Ͽ�Ŀ¼�����´�ŷֺ�������ϡ�
	 * @param threshold �����ʵ��У�����������
	 * @param resultFilePath �ʵ�·��
	 */
	public abstract void ConstructFeatureDictionary(FeatureMaker feature,String libPath,String resultFilePath,int threshold);
}
