package com.openlab.publicopinion.DataClassify.FeatureWeighting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.openlab.publicopinion.DataClassify.Dictionary;
import com.openlab.publicopinion.DataClassify.FeatureCounter;
public class CategorizingDataManager {
	/**���Ͽ�Ŀ¼*/
	public  final String catPath;
	/**�ֵ�Ŀ¼*/
	public  final String dicPath;
	public  final Map<String, Integer> dic;
	public  String[] docs;
	
	private  Map<Integer, Map<Integer, Integer>> cp;
//	/**��j���ĵ�i�еĳ���Ƶ��*/
//	public  int tf[][];
//	/**��j���ĵ�Ƶ��*/
//	public  int n[];
//	/**����*/
//	public  Integer key[];
//	/**�ĵ������ĵ�����*/
	public  int N;
	/**�ĵ����Ĵ�����*/
	public  int M;
	
	private Dictionary dicEx=new Dictionary();
	
	/**
	 * 
	 * @param dicPath �ֵ�Ŀ¼
	 * @param catPath �������ĵ���Ŀ¼
	 */
	public CategorizingDataManager(FeatureCounter FC,String dicPath,String catPath,boolean isTraining){
		this.dicPath=dicPath;
		this.catPath=catPath;
		
		dic=dicEx.loadDictionary(dicPath);
		File catTextDir=new File(catPath);
		if (!catTextDir.isDirectory()) 
			throw new IllegalArgumentException("���������Ͽ�����ʧ�ܣ� [" +catPath + "]");
		if(isTraining){
			docs=catTextDir.list();//��¼���
			ArrayList<String> docAry=new ArrayList<String>();
			for(String cls:docs){
				for(String cl:getFilesPath(cls))
					docAry.add(cl);
			}
			docs=new String[docAry.size()];
			docs=docAry.toArray(docs);
		}else{
			docs=catTextDir.list();
			for(int i=0;i<docs.length;i++){
				docs[i]=catPath+File.separator+docs[i];
			}
		}
		ConstructMap(FC, this.catPath);
	}
	
	private void ConstructMap(FeatureCounter FC,String catPath){
		cp=new HashMap<Integer, Map<Integer,Integer>>(dic.size());
		
		for(int i=0;i<docs.length;i++){//��ÿƪ�ĵ�ͳ������Ƶ��������������ֻ࣬ͳ������������������
			Map<String,Integer> fc=FC.Count(dic, dicEx.readAll(docs[i]));
			for(String sf : fc.keySet()){//��ÿһ������
				Integer wordID=dic.get(sf);//������ֵ���
				if(wordID!=null){
					addWordToMap(wordID, fc.get(sf),i, cp);
				}
			}
		}
		N=docs.length;M=cp.size();
	}
	
	private void addWordToMap(int wordID,int freq,int docID,Map<Integer, Map<Integer, Integer>> cp){
		if(cp.containsKey(wordID)){//����¼�˴���
			if(cp.get(wordID).containsKey(docID)){//�Ժ��д��ĵ�
				Map<Integer, Integer> tmpDc=cp.get(wordID);
				tmpDc.put(docID, tmpDc.get(docID)+freq);//ԭ�м���freq
			}else{
				cp.get(wordID).put(docID, freq);
			}
		}else{//û��¼�˴�����˴�����¼����
			Map<Integer, Integer> dc=new HashMap<Integer, Integer>();
			dc.put(docID, freq);
			cp.put(wordID,dc);
		}
	}
	
	public Map<Integer, Map<Integer, Integer>> getMap(){
		return cp;
	}
	
	/**
	* �������д������ı�·��(ѵ��ģʽ��Ϊ���)
	*/
	public String[] getDocs() 
	{
		return (new File(catPath)).list();
	}
	
	/**
	* ����ѵ���ı���𷵻��������µ�����ѵ���ı�·����full path��
	* @param classification �����ķ���
	* @return ���������������ļ���·����full path��
	*/
	public String[] getFilesPath(String classification) 
	{
		File classDir = new File(catPath +File.separator +classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) 
		{
			ret[i] = catPath +File.separator +classification +File.separator +ret[i];
		}
		return ret;
	}
	
	public String[] getFiles(String classification) {
		File classDir = new File(catPath +File.separator +classification);
		String[] ret = classDir.list();
		return ret;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//CategorizingDataManager CDM=new CategorizingDataManager("Test\\FeatureDictionary.txt", "CategoringData\\",false);
		
		
//		System.out.println("M="+CDM.M);
	}

}
