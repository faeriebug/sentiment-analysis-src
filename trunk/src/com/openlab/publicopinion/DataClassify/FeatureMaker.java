package com.openlab.publicopinion.DataClassify;

/**
 * ��������
 * @author HuHaixiao
 *
 */
public interface FeatureMaker {
	/**
	 * ���ı��л�ȡ����
	 * @param text
	 * @return
	 */
	String[] getFeatureFromDoc(String text);
	
	/**
	 * Ԥ����ӳ���Ĵ�С
	 * <p>�˷����Ĵ����ǻ���Ч���ϵĿ���
	 * @return
	 */
	int estimateMapSize();
}
