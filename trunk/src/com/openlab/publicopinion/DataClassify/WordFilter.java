package com.openlab.publicopinion.DataClassify;

/**
 * ���������
 * <p>ʵ�ִ˽ӿڵ�������жϸ������ԵĴ����Ƿ񱻹��˵�
 * @author HuHaixiao
 *
 */
public interface WordFilter {
	/**
	 * �жϴ����Ƿ�ͣ��
	 * @param word ����
	 * @param pos ����:��ICTCLAS���Ա�עΪ׼
	 * @return true,�����ͣ�ã�false,�������ʹ��
	 */
	boolean isStoped(String word,String pos);
}
