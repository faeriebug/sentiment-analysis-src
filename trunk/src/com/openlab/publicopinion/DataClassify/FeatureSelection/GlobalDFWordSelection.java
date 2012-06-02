package com.openlab.publicopinion.DataClassify.FeatureSelection;

import java.io.*;
import java.util.*;

import com.openlab.publicopinion.DataClassify.FeatureMaker;

/**
 * ȫ��DF( �ĵ�Ƶ��)������ѡȡ
 * @author HuHaixiao
 *
 */
public class GlobalDFWordSelection extends FeatureSelection{
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "GlobalDF";
	}
	
	@Override
	public Number getFeatureForTermInClass(String wordID, int clsID,TrainingDataManager TDM) {
		// TODO Auto-generated method stub
		return GlobalDFFS_TermForAllClass(wordID, TDM);
	}
	
	@Override
	public void ConstructFeatureDictionary(FeatureMaker feature, String libPath,	String resultFilePath,int threshold) {
		// TODO Auto-generated method stub
		TrainingDataManager TDM=new TrainingDataManager(feature,libPath);
		Map<String, int[]> mp=TDM.getMap();
		final Map<String, Integer> df=new HashMap<String, Integer>();
		
		for(String wordID:mp.keySet()){//��ÿһ������
			df.put(wordID,GlobalDFFS_TermForAllClass(wordID, TDM));
		}
		//����DF��������
		Object[] keys=df.keySet().toArray();
		Arrays.sort(keys,new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return df.get(o2)-df.get(o1);
			}
		});
			
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			int m=0;//�ʵ���㿪ʼ
			fw.write((threshold>df.size()?df.size():threshold)+"\n");//д��ʵ��С
			for(Object i:keys){
				if(m>=threshold)break;//�Ѿ�ȡ��threshold��������
				//System.out.println(i+","+m);
				fw.write(i+","+m+"\n");
				m++;
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
				e.printStackTrace();
		}
	}
	/**
	 * ѡȡ����k�������ʣ�����������ڱ����ļ���
	 * @param dicPath �ֵ�Ŀ¼
	 *@param libPath ѵ�����Ͽ�Ŀ¼�����´�ŷֺ�������ϡ�
	 *@param resultFilePath �����������ļ���·��
	 *@param k ѡȡ������������
	 */
	public void GlobalDFFS(FeatureMaker feature,String libPath,String resultFilePath,int k){
		TrainingDataManager TDM=new TrainingDataManager(feature,libPath);
		Map<String, int[]> mp=TDM.getMap();
		final Map<String, Integer> df=new HashMap<String, Integer>();
		
		for(String wordID:mp.keySet()){//��ÿһ������
			df.put(wordID,GlobalDFFS_TermForAllClass(wordID, TDM));
		}
		//����DF��������
		Object[] keys=df.keySet().toArray();
		Arrays.sort(keys,new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return df.get(o2)-df.get(o1);
			}
		});
			
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			int m=1;
			for(Object i:keys){
				if(m>k)break;//�Ѿ�ȡ��k��������
				//System.out.println(i+","+m+":"+df.get(i));
				fw.write(i+","+m+"\n");
				m++;
			}
			fw.flush();
			fw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		 
	}
	
	/**
	 * ĳһ�������������DF
	 * @param mp
	 * @param wordID
	 * @param classLabel
	 * @return
	 */
	public int GlobalDFFS_TermForAllClass(String wordID,TrainingDataManager TDM){
		int df=0;
		Map<String, int[]> mp=TDM.getMap();
		for(int i=0;i<TDM.getClsNum();i++)//��ÿ�����
			df+=mp.get(wordID)[i];
		return df;
	}

	public static void main(String[] args) {
		//GlobalDFWordSelection DFWS=new GlobalDFWordSelection();
//		long start=System.currentTimeMillis();
//		
//		//DFWS.GlobalDFFS("Test\\dictionary.txt","TanCorpMinTrain\\","Test\\FeatureDictionary.txt",500);
//		DFWS.ConstructFeatureDictionary("Test\\dictionary.txt","TrainingData\\","Test\\FeatureDictionary.txt",500);
//		long end=System.currentTimeMillis();
//		System.out.println("����ִ�У�"+(end-start)/1000.0+"s");
		long start=System.currentTimeMillis();
		long end=System.currentTimeMillis();
		System.out.println("����ִ�У�"+(end-start)/1000.0+"s");
	}
	
}
