package com.openlab.publicopinion.DataClassify.FeatureSelection;

import java.io.*;
import java.util.*;

import com.openlab.publicopinion.DataClassify.FeatureMaker;
import com.openlab.publicopinion.DataClassify.Sentiment.SemanticFeatureMaker;
import com.openlab.publicopinion.DataClassify.Sentiment.SentiDictionary;
import com.openlab.publicopinion.DataClassify.Subject.SubjectFeatureMaker;
/**
 * <p>����������ѡȡ�㷨������ѡȡ����
 * <p>1.�����ѡȡ
 * <p>2.��ƽ������ֵѡȡ
 * <p>3.����󿨷�ֵѡȡ
 * @author HuHaixiao
 *
 */
public class ChiSqrWordSelection  extends FeatureSelection{
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ChiSqr";
	}
	
	@Override
	public void ConstructFeatureDictionary(FeatureMaker feature, String libPath, String resultFilePath,int threshold) {
		// TODO Auto-generated method stub
		TrainingDataManager TDM=new TrainingDataManager(feature, libPath);
		Set<String> FinalFeature=new HashSet<String>(threshold);
		for(int i=0;i<TDM.getClsNum();i++){//��ÿһ�����
				int thres=(int)((double)threshold/TDM.getTotalDocNum()*TDM.getDocNumOfCls(i));//ȡ������ȡ���ڴ�������ĵ�ռ���ĵ����ı���
				ChiSquare_AllTermsForclass(i, TDM, FinalFeature, thres);
			}
		try{
			//������ļ���
			FileWriter fw=new FileWriter(resultFilePath);
			int m=0;//�ʵ���㿪ʼ
			//System.out.println((threshold>FinalFeature.size()?FinalFeature.size():threshold));
			fw.write((threshold>FinalFeature.size()?FinalFeature.size():threshold)+"\n");//д��ʵ��С
			
			for(Iterator<String> it=FinalFeature.iterator();it.hasNext();){
				String tmp=it.next();
				//System.out.println(dicInv[tmp]+","+m);
				fw.write(tmp+","+m+"\n");
				m++;
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Number getFeatureForTermInClass(String wordID, int clsID,
			TrainingDataManager TDM) {
		// TODO Auto-generated method stub
		return ChiSquare_TermForClass(wordID, clsID, TDM);//�����Ŀ���ֵ
	}

	
	/**
	 * ����ƽ������ֵ����������ѡȡ
	 * @param dicPath �ֵ�Ŀ¼
	 * @param libPath ѵ�����Ͽ�Ŀ¼�����´�ŷֺ�������ϡ�
	 * @param resultFilePath �����������ļ���·��
	 * @param k ѡȡ������������
	 */
	public void ChiSqrFSAvg(String dicPath,String libPath,String resultFilePath,int k){
		TrainingDataManager TDM=new TrainingDataManager(new SubjectFeatureMaker("Test\\dictionary.txt"), libPath);
		Map<String, int[]> mp=TDM.getMap();
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			final HashMap<String, Double> chisquareInfo=new HashMap<String, Double>();
			
			for(String wordID:mp.keySet()){//�����дʼ���ƽ������ֵ
				chisquareInfo.put(wordID, ChiSquare_TermAvg(wordID, TDM));
			}
			
			//����ƽ������ֵ��������
			Object[] keys=chisquareInfo.keySet().toArray();
			Arrays.sort(keys,new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					return	(int)Math.signum(chisquareInfo.get(o2)-chisquareInfo.get(o1));
					}
				});
			//������ļ���
			int m=0;
			for(Object i:keys){
				if(m>=k)break;//�Ѿ�ȡ��threshold��������
				//System.out.println(i+","+chisquareInfo.get(i));
				fw.write(i+","+chisquareInfo.get(i)+"\n");
				m++;
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ������󿨷�ֵ����������ѡȡ
	 * @param dicPath �ֵ�Ŀ¼
	 * @param libPath ѵ�����Ͽ�Ŀ¼�����´�ŷֺ�������ϡ�
	 * @param resultFilePath �����������ļ���·��
	 * @param k ѡȡ������������
	 */
	public void ChiSqrFSMax(String dicPath,String libPath,String resultFilePath,int k){
		TrainingDataManager TDM=new TrainingDataManager(new SubjectFeatureMaker("Test\\dictionary.txt"), libPath);
		Map<String, int[]> mp=TDM.getMap();
		try{
			FileWriter fw=new FileWriter(resultFilePath);
			final Map<String, Double> chisquareInfo=new HashMap<String, Double>();
			
			for(String wordID:mp.keySet()){//�����дʼ�����󿨷�ֵ
				chisquareInfo.put(wordID, ChiSquare_TermMax(wordID,TDM));
			}
			
			//������󿨷�ֵ��������
			Object[] keys=chisquareInfo.keySet().toArray();
			Arrays.sort(keys,new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					return	(int)Math.signum(chisquareInfo.get(o2)-chisquareInfo.get(o1));
					}
				});
			//������ļ���
			int m=0;
			for(Object i:keys){
				if(m>=k)break;//�Ѿ�ȡ��threshold��������
				//System.out.println(i+","+chisquareInfo.get(i));
				fw.write(i+","+chisquareInfo.get(i)+"\n");
				m++;
			}
			fw.flush();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ĳ���ʵ�ƽ������ֵ
	 * @param mp ӳ����������Ͽ�������Ϣ
	 * @param wordID ����ID
	 * @param classification �������
	 * @param N ���Ͽ������ĵ���
	 * @return
	 */
	public	 double  ChiSquare_TermAvg(String wordID,TrainingDataManager TDM){
		double chiAvg=0.0,p;
		for(int i=0;i<TDM.getClsNum();i++){
			p=TDM.getDocNumOfCls(i)/(double)TDM.getTotalDocNum();
			chiAvg+=p*ChiSquare_TermForClass(wordID, i, TDM);
		}
		return chiAvg;
	}
	
	/**
	 * ����ĳ���ʵ���󿨷�ֵ
	 * @param mp ӳ����������Ͽ�������Ϣ
	 * @param wordID ����ID
	 * @param classification �������
	 * @param N ���Ͽ������ĵ���
	 * @return
	 */
	public 	 double  ChiSquare_TermMax(String wordID,TrainingDataManager TDM){
		double temp,chiMax=ChiSquare_TermForClass(wordID, 0, TDM);
		for(int i=1;i<TDM.getClsNum();i++){
			temp=ChiSquare_TermForClass(wordID, i, TDM);
			if(temp>chiMax)
				chiMax=temp;
		}
		return chiMax;
	}
		
	/**
	 * �������дʶԴ����Ŀ���ֵ����������ֵ�������У����ǰthreshold��
	 * @param clsID���
	 * @param TDM ����������
	 * @param FinalFeature �洢���������ʵļ���
	 * @param threshold �����ȡ�ʵ�����
	 */
public void ChiSquare_AllTermsForclass(int clsID,TrainingDataManager TDM,Set<String> FinalFeature,int threshold){  
	final Map<String, Double> chisquareInfo=new HashMap<String, Double>();
	Map<String, int[]> mp=TDM.getMap();
	Double chivalue;
	//���㿨��ֵ
	for(String wordID:mp.keySet()){//��ÿһ������
		chivalue=ChiSquare_TermForClass(wordID, clsID, TDM);
		if(chivalue!=null)	chisquareInfo.put(wordID,chivalue );
	}
	//������ֵ��������
	Object[] keys=chisquareInfo.keySet().toArray();
	Arrays.sort(keys,new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			return	(int)Math.signum(chisquareInfo.get(o2)-chisquareInfo.get(o1));
		}
	});
	threshold=Math.min(threshold, keys.length);
	//�ظ���ֻ����һ��
	for(int i=0;i<threshold;i++){
		
		try {
			FinalFeature.add((String)keys[i]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}



/**
 * ����ĳһ���ʶ�ĳһ���Ŀ���ֵ
 * @param mp ӳ����������Ͽ�������Ϣ
 * @param wordID ����ID
 * @param classLabel ����ǩ
 * @param N ���Ͽ������ĵ���
 * @param Nc ĳһ����е��ĵ�����
 * @return ����ֵ�����ӳ����в����ڴ˴���򷵻�null
 */
public 	 Double  ChiSquare_TermForClass(String wordID,int clsID,TrainingDataManager TDM){ 
	Map<String, int[]> mp=TDM.getMap();
	
	if(!mp.containsKey(wordID))	return null;//���ӳ����в����ڴ˴���򷵻�null
	
	int[] cls=mp.get(wordID);
	
	double N11=cls[clsID];											//�ĵ����������˴��������classLabel��
	double N01=TDM.getDocNumOfCls(clsID)-N11;	//�ĵ������������˴��������classLabel��
	double N10=0;															//�ĵ����������˴���Ҳ�����classLabel��
	for(int i=0;i<cls.length;i++){
		if(i!=clsID)
			N10+=cls[i];
	}
	double N00=TDM.getTotalDocNum()-TDM.getDocNumOfCls(clsID)-N10;	//�ĵ������������˴���Ҳ�����classLabel��
	return CalChiSquareValue(N11,N10,N01,N00);
}
	
	/**
	 *  ����CHI-square ֵ   
	 * @param N11 ��������ж���ƪ���º��иô�
	 * @param N10 �ôʳ��ֵ������ж���ƪ���ٸ�����
	 * @param N01 ��������ж���ƪ���²����иô�
	 * @param N00 ѵ�����Ͽ��й��ж���ƪ���¼������ôʣ�Ҳ�������ڸ�����
	 * @return
	 * 
	 */
private double CalChiSquareValue(double N11,double N10,double N01,double N00){
	    double chiSquare=0;
	    chiSquare=(N11+N10+N01+N00)*(N11*N00-N10*N01)*(N11*N00-N10*N01)/((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00));
	    return chiSquare;
	}
	
	public static void main(String[] args){
		ChiSqrWordSelection CSWS=new ChiSqrWordSelection();
		//CSWS.ChiSqrFSAvg("Test\\dictionary.txt","TanCorpMinTrain\\","keywordsInfo.txt", 5000);
		//CSWS.ChiSqrFSMax("Test\\dictionary.txt","TrainingData\\","keywordsInfo.txt", 5000);
//		long start=System.currentTimeMillis();
//		//CSWS.CalcFeaturesForAllFiles("Test\\dictionary.txt", "TrainingData\\", "Test\\Tdata.txt");
//		CSWS.CalcFeaturesForAllFiles("Test\\FeatureDictionary.txt", "TrainingData\\", "Test\\Tdata");
//		long end=System.currentTimeMillis();
//		System.out.println("����ִ�У�"+(end-start)/60.0+"s");

		long start=System.currentTimeMillis();
		
		//DFWS.GlobalDFFS("Test\\dictionary.txt","TanCorpMinTrain\\","Test\\FeatureDictionary.txt",500);
//		CSWS.ConstructFeatureDictionary(new SubjectFeature("Test\\dictionary.txt"),"TrainingData\\","Test\\FeatureDictionary.txt",5000);
		CSWS.ConstructFeatureDictionary(new SemanticFeatureMaker(new SentiDictionary()),"TrainingData\\","Test\\FeatureDictionary.txt",5000);
		long end=System.currentTimeMillis();
		System.out.println("����ִ�У�"+(end-start)/1000.0+"s");
	}


	


	

}
