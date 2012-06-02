package com.openlab.publicopinion.DataClassify.FeatureWeighting;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.openlab.publicopinion.DataClassify.*;

public abstract class FeatureWeight{
	public abstract String getName();
	/**
	 * ѵ��ʱ���߲���ʱʹ�ã��Զ��������Ϣ����
	 * Ϊ���Ͽ��������ļ���������ֵ���������ڱ����ļ�(libsvm��ʽ)
	 * <p>�����Ŵ�1��ʼ
	 * @param dicPath �ʵ�Ŀ¼
	 * @param libPath ���Ͽ�Ŀ¼�����´�Ŵ�����������ϡ�
	 * @param resultFilePath ����Ϊlibsvm��ʽ�������ĵ�����ֵ�ļ�
	 */
	public  void CalcFeaturesForAllFiles_Training(FeatureMaker FM,FeatureCounter FC,String dicPath,String libPath,String resultFilePath) {
		CategorizingDataManager CDM=new CategorizingDataManager(FC,dicPath, libPath,true);
		Dictionary dicEx=new Dictionary();
		String[] docs=CDM.getDocs();
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			int docID=0;// �ı�ID��ͬһĿ¼�е���
			for(int i=0;i<docs.length;i++){//��ÿһ�����
				for(String doc:CDM.getFilesPath(docs[i])){//��ÿһƪ�ĵ�
					//д����������
					String temp=FeatureVectorOfDoc(FM,dicEx.readAll(doc),docID++,CDM);
					//System.out.println((i+1)+" "+temp+"\n");
					fw.write((i+1)+" "+temp+"\n");
				}
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ʱʹ�ã��������Ϣ(���ͬһΪ1)
	 * Ϊ���Ͽ��������ļ���������ֵ���������ڱ����ļ�(libsvm��ʽ),����ѵ��ģʽ(��ͬʱ������ID)
	 * @param dicPath �ʵ�Ŀ¼
	 * @param libPath ���Ͽ�Ŀ¼�����´�Ŵ�����������ϡ�
	 * @param resultFilePath ����Ϊlibsvm��ʽ�������ĵ�����ֵ�ļ�
	 */
	public  void CalcFeaturesForAllFiles(FeatureMaker FM,FeatureCounter FC,String dicPath,String libPath,String resultFilePath) {
		CategorizingDataManager CDM=new CategorizingDataManager(FC,dicPath, libPath,false);
		Dictionary dicEx=new Dictionary();
		String[] docs=CDM.getDocs();
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			int docID=0;
			for(int i=0;i<docs.length;i++){//��ÿһ�����
				for(String doc:CDM.getFilesPath(docs[i])){//��ÿһƪ�ĵ�
					//д����������
					String temp=FeatureVectorOfDoc(FM,dicEx.readAll(doc),docID++,CDM);
					fw.write(1+" "+temp+"\n");
				}
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * �����ĵ�����������
	 * @param docStr �ĵ��ַ���
	 * @param docID �ĵ����
	 * @param CDM ���ݴ�����
	 * @return
	 */
	public  String FeatureVectorOfDoc(FeatureMaker FM,String docStr,int docID,CategorizingDataManager CDM){
		StringBuilder sb=new StringBuilder();
		String[] features=FM.getFeatureFromDoc(docStr);
		Map<Integer, Number> FeatureVector = new HashMap<Integer, Number>();
		Map<String,Integer> dic=CDM.dic;
		for(String word:features){
			Integer wordID=dic.get(word);
			if(wordID!=null){//Ϊ�����ʵ��к��еĴ���
				if(!FeatureVector.containsKey(wordID))//�����û��Ϊ�˴������Ȩ��
					FeatureVector.put(wordID,getWight(docID, wordID, CDM));
			}
		}
		
		//�Ա�Ž�������libSVM��ʽҪ��
		Integer[] keys=new Integer[]{};
		keys=FeatureVector.keySet().toArray(keys);
		Arrays.sort(keys,new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// TODO Auto-generated method stub
				return	 o1-o2;
			}
		});
		
		for(Integer wordID:keys){
			sb.append(wordID+":"+FeatureVector.get(wordID)+" ");
		}
		return sb.toString();
		
	}
	
	/**
	 * ����a[docID][wordID]Ȩ��
	 * @param docID
	 * @param wordID
	 * @param CDM
	 * @return
	 */
	public abstract Number getWight(int docID,int wordID,CategorizingDataManager CDM);
	
}
