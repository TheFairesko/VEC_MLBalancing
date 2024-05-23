package edu.boun.edgecloudsim.applications.sample_app5;

import java.util.ArrayList;

import edu.boun.edgecloudsim.utils.SimLogger;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;

import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SGD;
import weka.classifiers.functions.VotedPerceptron;
import weka.classifiers.trees.RandomForest;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class WekaWrapper {
	public static final double MAX_WLAN_DELAY = 9; //sec
	public static final double MAX_WAN_DELAY = 7; //sec
	public static final double MAX_GSM_DELAY = 8; //sec
	
	private String[] EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("edge_balance_regression_train.txt");
	private double[] EDGE_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",2,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
	private double[] EDGE_REGRESSION_STD_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",3,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
	
	private String[] CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("cloud_balance_regression_train.txt");
	private double[] CLOUD_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",2,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
	private double[] CLOUD_REGRESSION_STD_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",3,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
	

	private String[] EDGE_REGRESSION_ATTRIBUTES = getParams("edge_regression_train.txt");
	private double[] EDGE_REGRESSION_MEAN_VALS = getValues("edge_regression_train.txt",2,EDGE_REGRESSION_ATTRIBUTES,1);
	private double[] EDGE_REGRESSION_STD_VALS = getValues("edge_regression_train.txt",3,EDGE_REGRESSION_ATTRIBUTES,1);

//	private String[] EDGE_CLASSIFIER_ATTRIBUTES = getParams("edge_classifier_train.txt");
//	private double[] EDGE_CLASSIFIER_MEAN_VALS = getValues("edge_classifier_train.txt",2,EDGE_CLASSIFIER_ATTRIBUTES,0);
//	private double[] EDGE_CLASSIFIER_STD_VALS = getValues("edge_classifier_train.txt",3,EDGE_CLASSIFIER_ATTRIBUTES,0);


	private String[] CLOUD_RSU_REGRESSION_ATTRIBUTES = getParams("cloud_rsu_regression_train.txt");
	private double[] CLOUD_RSU_REGRESSION_MEAN_VALS = getValues("cloud_rsu_regression_train.txt",2,CLOUD_RSU_REGRESSION_ATTRIBUTES,1);
	private double[] CLOUD_RSU_REGRESSION_STD_VALS = getValues("cloud_rsu_regression_train.txt",3,CLOUD_RSU_REGRESSION_ATTRIBUTES,1);

//	private String[] CLOUD_RSU_CLASSIFIER_ATTRIBUTES = getParams("cloud_rsu_classifier_train.txt");
//	private double[] CLOUD_RSU_CLASSIFIER_MEAN_VALS = getValues("cloud_rsu_classifier_train.txt",2,CLOUD_RSU_CLASSIFIER_ATTRIBUTES,0);
//	private double[] CLOUD_RSU_CLASSIFIER_STD_VALS = getValues("cloud_rsu_classifier_train.txt",3,CLOUD_RSU_CLASSIFIER_ATTRIBUTES,0);


	private String[] CLOUD_GSM_REGRESSION_ATTRIBUTES = getParams("cloud_gsm_regression_train.txt");
	private double[] CLOUD_GSM_REGRESSION_MEAN_VALS = getValues("cloud_gsm_regression_train.txt",2,CLOUD_GSM_REGRESSION_ATTRIBUTES,1);
	private double[] CLOUD_GSM_REGRESSION_STD_VALS = getValues("cloud_gsm_regression_train.txt",3,CLOUD_GSM_REGRESSION_ATTRIBUTES,1);

//	private String[] CLOUD_GSM_CLASSIFIER_ATTRIBUTES = getParams("cloud_gsm_classifier_train.txt");
//	private double[] CLOUD_GSM_CLASSIFIER_MEAN_VALS = getValues("cloud_gsm_classifier_train.txt",2,CLOUD_GSM_CLASSIFIER_ATTRIBUTES,0);
//	private double[] CLOUD_GSM_CLASSIFIER_STD_VALS = getValues("cloud_gsm_classifier_train.txt",3,CLOUD_GSM_CLASSIFIER_ATTRIBUTES,0);

	
//	private static final String[] EDGE_REGRESSION_ATTRIBUTES = {"TaskLength","AvgEdgeUtilization","ServiceTime"};
//	private static final double[] EDGE_REGRESSION_MEAN_VALS = {5756, 18};
//	private static final double[] EDGE_REGRESSION_STD_VALS = {4737, 7};

	private static final String[] EDGE_CLASSIFIER_ATTRIBUTES = {"NumOffloadedTask","TaskLength","WLANUploadDelay","WLANDownloadDelay","AvgEdgeUtilization"};
	private static final double[] EDGE_CLASSIFIER_MEAN_VALS = {130, 5756, 0.024192, 0.022952, 18};
	private static final double[] EDGE_CLASSIFIER_STD_VALS = {47, 4737, 0.025628, 0.010798, 7};


//	private static final String[] CLOUD_RSU_REGRESSION_ATTRIBUTES = {"TaskLength","WANUploadDelay","WANDownloadDelay","ServiceTime"};
//	private static final double[] CLOUD_RSU_REGRESSION_MEAN_VALS = {9718, 0.171074, 0.164998};
//	private static final double[] CLOUD_RSU_REGRESSION_STD_VALS = {5873, 0.096208, 0.068551};
//
	private static final String[] CLOUD_RSU_CLASSIFIER_ATTRIBUTES = {"NumOffloadedTask","WANUploadDelay","WANDownloadDelay"};
	private static final double[] CLOUD_RSU_CLASSIFIER_MEAN_VALS = {111, 0.171074, 0.164998};
	private static final double[] CLOUD_RSU_CLASSIFIER_STD_VALS = {40, 0.096208, 0.068551};
//
//
//	private static final String[] CLOUD_GSM_REGRESSION_ATTRIBUTES = {"TaskLength","GSMUploadDelay","GSMDownloadDelay","ServiceTime"};
//	private static final double[] CLOUD_GSM_REGRESSION_MEAN_VALS = {9718, 0.205794, 0.197476};
//	private static final double[] CLOUD_GSM_REGRESSION_STD_VALS = {5873, 0.179551, 0.148067};
//
	private static final String[] CLOUD_GSM_CLASSIFIER_ATTRIBUTES = {"NumOffloadedTask","GSMUploadDelay","GSMDownloadDelay"};
	private static final double[] CLOUD_GSM_CLASSIFIER_MEAN_VALS = {50, 0.205794, 0.197476};
	private static final double[] CLOUD_GSM_CLASSIFIER_STD_VALS = {18, 0.179551, 0.148067};
	
	
	private static final String[] CLASSIFIER_CLASSES = {"fail","success"};

	private AbstractClassifier classifier_edge, classifier_cloud_rsu, classifier_cloud_gsm;
	private AbstractClassifier regression_edge, regression_cloud_rsu, regression_cloud_gsm;
	private AbstractClassifier regression_edge_balance, regression_cloud_balance;
	
	private String BalanceRegressionType;

	private static WekaWrapper singleton = new WekaWrapper();

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private WekaWrapper() {

	}

	/* Static 'instance' method */
	public static WekaWrapper getInstance() {
		return singleton;
	}

	/*
	 * Possible Values for ClassifierType:
	 * - NaiveBayes
	 * - SMO
	 * - LibSVM
	 * - MultilayerPerceptron
	 * 
	 * Possible Values for RegressionType:
	 * - LinearRegression
	 * - SMOreg
	 */
	public void initialize(String ClassifierType, String RegressionType, String RegressionBalanceType, String ModelFolder) {
		try {
//			SimLogger.printLine(EDGE_REGRESSION_MEAN_VALS_FOR_BALANCE);
//			for(int i=0; i<EDGE_CLASSIFIER_ATTRIBUTES.length; i++) {
//				SimLogger.printLine(EDGE_CLASSIFIER_ATTRIBUTES[i]);
//				SimLogger.printLine(Double.toString(EDGE_REGRESSION_MEAN_VALS[i]));
//				SimLogger.printLine(Double.toString(EDGE_REGRESSION_STD_VALS[i]));
//			}
			if(ClassifierType.equals("NaiveBayes")) {
				classifier_edge = (NaiveBayes) weka.core.SerializationHelper.read(ModelFolder + "nb_edge.model");
				classifier_cloud_rsu = (NaiveBayes) weka.core.SerializationHelper.read(ModelFolder + "nb_cloud_rsu.model");
				classifier_cloud_gsm = (NaiveBayes) weka.core.SerializationHelper.read(ModelFolder + "nb_cloud_gsm.model");
			}
			else if(ClassifierType.equals("SMO")){
				classifier_edge = (SMO) weka.core.SerializationHelper.read(ModelFolder + "smo_edge.model");
				classifier_cloud_rsu = (SMO) weka.core.SerializationHelper.read(ModelFolder + "smo_cloud_rsu.model");
				classifier_cloud_gsm = (SMO) weka.core.SerializationHelper.read(ModelFolder + "smo_cloud_gsm.model");
			}
			else if(ClassifierType.equals("MultilayerPerceptron")){
				classifier_edge = (MultilayerPerceptron) weka.core.SerializationHelper.read(ModelFolder + "mlp_edge.model");
				classifier_cloud_rsu = (MultilayerPerceptron) weka.core.SerializationHelper.read(ModelFolder + "mlp_cloud_rsu.model");
				classifier_cloud_gsm = (MultilayerPerceptron) weka.core.SerializationHelper.read(ModelFolder + "mlp_cloud_gsm.model");
			}
			
			else if(ClassifierType.equals("Logistic")){
				classifier_edge = (Logistic) weka.core.SerializationHelper.read(ModelFolder + "log_edge.model");
				classifier_cloud_rsu = (Logistic) weka.core.SerializationHelper.read(ModelFolder + "log_cloud_rsu.model");
				classifier_cloud_gsm = (Logistic) weka.core.SerializationHelper.read(ModelFolder + "log_cloud_gsm.model");
			}
			else if(ClassifierType.equals("SGD")){
				classifier_edge = (SGD) weka.core.SerializationHelper.read(ModelFolder + "sgd_edge.model");
				classifier_cloud_rsu = (SGD) weka.core.SerializationHelper.read(ModelFolder + "sgd_cloud_rsu.model");
				classifier_cloud_gsm = (SGD) weka.core.SerializationHelper.read(ModelFolder + "sgd_cloud_gsm.model");
			}
			else if(ClassifierType.equals("VotedPerceptron")){
				classifier_edge = (VotedPerceptron) weka.core.SerializationHelper.read(ModelFolder + "vp_edge.model");
				classifier_cloud_rsu = (VotedPerceptron) weka.core.SerializationHelper.read(ModelFolder + "vp_cloud_rsu.model");
				classifier_cloud_gsm = (VotedPerceptron) weka.core.SerializationHelper.read(ModelFolder + "vp_cloud_gsm.model");
			}
			else if(ClassifierType.equals("RandomForest")){
				classifier_edge = (RandomForest) weka.core.SerializationHelper.read(ModelFolder + "rf_edge.model");
				classifier_cloud_rsu = (RandomForest) weka.core.SerializationHelper.read(ModelFolder + "rf_cloud_rsu.model");
				classifier_cloud_gsm = (RandomForest) weka.core.SerializationHelper.read(ModelFolder + "rf_cloud_gsm.model");
			}

			
			
			if(RegressionType.equals("LinearRegression")){
				regression_edge = (LinearRegression) weka.core.SerializationHelper.read(ModelFolder + "lr_edge.model");
				regression_cloud_rsu = (LinearRegression) weka.core.SerializationHelper.read(ModelFolder + "lr_cloud_rsu.model");
				regression_cloud_gsm = (LinearRegression) weka.core.SerializationHelper.read(ModelFolder + "lr_cloud_gsm.model");
			}
			else if(RegressionType.equals("SMOreg")){
				regression_edge = (SMOreg) weka.core.SerializationHelper.read(ModelFolder + "smoreg_edge.model");
				regression_cloud_rsu = (SMOreg) weka.core.SerializationHelper.read(ModelFolder + "smoreg_cloud_rsu.model");
				regression_cloud_gsm = (SMOreg) weka.core.SerializationHelper.read(ModelFolder + "smoreg_cloud_gsm.model");
			}
			
			
			if(RegressionBalanceType.equals("LinearRegression")){
				regression_edge_balance = (LinearRegression) weka.core.SerializationHelper.read(ModelFolder + "lr_edge_balance.model");
				regression_cloud_balance = (LinearRegression) weka.core.SerializationHelper.read(ModelFolder + "lr_cloud_balance.model");
				
				EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("edge_balance_regression_train.txt");
				EDGE_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",2,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				EDGE_REGRESSION_STD_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",3,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				
				CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("cloud_balance_regression_train.txt");
				CLOUD_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",2,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				CLOUD_REGRESSION_STD_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",3,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				BalanceRegressionType="with_z_norm";
			}
			else if(RegressionBalanceType.equals("MultilayerPerceptron")){
				regression_edge_balance = (MultilayerPerceptron) weka.core.SerializationHelper.read(ModelFolder + "mlp_edge_balance.model");
				regression_cloud_balance = (MultilayerPerceptron) weka.core.SerializationHelper.read(ModelFolder + "mlp_cloud_balance.model");
				
				EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("edge_balance_regression_train.txt");
				EDGE_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",2,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				EDGE_REGRESSION_STD_VALS_FOR_BALANCE = getValues("edge_balance_regression_train.txt",3,EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				
				CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE = getParams("cloud_balance_regression_train.txt");
				CLOUD_REGRESSION_MEAN_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",2,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				CLOUD_REGRESSION_STD_VALS_FOR_BALANCE = getValues("cloud_balance_regression_train.txt",3,CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,1);
				BalanceRegressionType="with_z_norm";
			}
			else if(RegressionBalanceType.equals("RandomForest")){
				regression_edge_balance = (RandomForest) weka.core.SerializationHelper.read(ModelFolder + "rf_edge_balance.model");
				regression_cloud_balance = (RandomForest) weka.core.SerializationHelper.read(ModelFolder + "rf_cloud_balance.model");
				
				BalanceRegressionType="without_z_norm";
			}

			
		}
		catch (Exception e) {
			SimLogger.printLine("cannot serialize weka objects!");
			System.exit(1);
		}
	}

	public double handleRegression(int targetDatacenter, double[] values) {
		double result = 0;

		try {
			if(targetDatacenter == VehicularEdgeOrchestrator.EDGE_DATACENTER) {
				Instance data = getRegressionData("edge", values,
						EDGE_REGRESSION_ATTRIBUTES,
						EDGE_REGRESSION_MEAN_VALS,
						EDGE_REGRESSION_STD_VALS);
				result = regression_edge.classifyInstance(data);
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.CLOUD_DATACENTER_VIA_RSU) {
				Instance data = getRegressionData("cloud_rsu", values,
						CLOUD_RSU_REGRESSION_ATTRIBUTES,
						CLOUD_RSU_REGRESSION_MEAN_VALS,
						CLOUD_RSU_REGRESSION_STD_VALS);
				result = regression_cloud_rsu.classifyInstance(data);
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.CLOUD_DATACENTER_VIA_GSM) {
				Instance data = getRegressionData("cloud_gsm", values,
						CLOUD_GSM_REGRESSION_ATTRIBUTES,
						CLOUD_GSM_REGRESSION_MEAN_VALS,
						CLOUD_GSM_REGRESSION_STD_VALS);
				result = regression_cloud_gsm.classifyInstance(data);
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.EDGE_DATACENTER_BALANCE) {
				
				Instance data = getBalanceRegressionData("edge_balance", values,
						EDGE_REGRESSION_ATTRIBUTES_FOR_BALANCE,
						EDGE_REGRESSION_MEAN_VALS_FOR_BALANCE,
						EDGE_REGRESSION_STD_VALS_FOR_BALANCE);
				
				result = regression_edge_balance.classifyInstance(data);
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.CLOUD_DATACENTER_BALANCE) {
				
				Instance data = getBalanceRegressionData("cloud_balance", values,
						CLOUD_REGRESSION_ATTRIBUTES_FOR_BALANCE,
						CLOUD_REGRESSION_MEAN_VALS_FOR_BALANCE,
						CLOUD_REGRESSION_STD_VALS_FOR_BALANCE);
				
				result = regression_cloud_balance.classifyInstance(data);
			}
			
		}
		catch (Exception e) {
			SimLogger.printLine("cannot handle regression!");
			System.exit(1);
		}

		return result;
	}

	public boolean handleClassification(int targetDatacenter, double[] values) {
		boolean result = false;

		try {
			if(targetDatacenter == VehicularEdgeOrchestrator.EDGE_DATACENTER) {
				Instance data = getClassificationData("edge", values,
						EDGE_CLASSIFIER_ATTRIBUTES,
						EDGE_CLASSIFIER_MEAN_VALS,
						EDGE_CLASSIFIER_STD_VALS);
				result = (classifier_edge.classifyInstance(data) == 1) ? true : false;
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.CLOUD_DATACENTER_VIA_RSU) {
				Instance data = getClassificationData("cloud_rsu", values,
						CLOUD_RSU_CLASSIFIER_ATTRIBUTES,
						CLOUD_RSU_CLASSIFIER_MEAN_VALS,
						CLOUD_RSU_CLASSIFIER_STD_VALS);
				result = (classifier_cloud_rsu.classifyInstance(data) == 1) ? true : false;
			}
			else if(targetDatacenter == VehicularEdgeOrchestrator.CLOUD_DATACENTER_VIA_GSM) {
				Instance data = getClassificationData("cloud_gsm", values,
						CLOUD_GSM_CLASSIFIER_ATTRIBUTES,
						CLOUD_GSM_CLASSIFIER_MEAN_VALS,
						CLOUD_GSM_CLASSIFIER_STD_VALS);
				result = (classifier_cloud_gsm.classifyInstance(data) == 1) ? true : false;
			}
		}
		catch (Exception e) {
			SimLogger.printLine("cannot handle classification!");
			System.exit(1);
		}

		return result;
	}

	private Instance getRegressionData(String relation, double[] values, String[] attributes, double[] meanVals, double[] stdVals) {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		for(int i=0; i<attributes.length; i++)
			atts.add(new Attribute(attributes[i]));
		Instances dataRaw = new Instances(relation,atts,0);
		double[] instanceValue1 = new double[dataRaw.numAttributes()];
		for(int i=0; i<values.length; i++) {
			instanceValue1[i] = (values[i] - meanVals[i]) / stdVals[i];
		}

		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		dataRaw.setClassIndex(dataRaw.numAttributes()-1);

		return dataRaw.get(0);
	}
	
	private Instance getBalanceRegressionData(String relation, double[] values, String[] attributes, double[] meanVals, double[] stdVals) {
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		for(int i=0; i<attributes.length; i++)
			atts.add(new Attribute(attributes[i]));
		Instances dataRaw = new Instances(relation,atts,0);
		double[] instanceValue1 = new double[dataRaw.numAttributes()];

		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		dataRaw.setClassIndex(dataRaw.numAttributes()-1);

		return dataRaw.get(0);
	}

	public Instance getClassificationData(String relation, double[] values, String[] attributes, double[] meanVals, double[] stdVals) {		
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		ArrayList<String> classVal = new ArrayList<String>();
		for(int i=0; i<CLASSIFIER_CLASSES.length; i++)
			classVal.add(CLASSIFIER_CLASSES[i]);

		for(int i=0; i<attributes.length; i++)
			atts.add(new Attribute(attributes[i]));

		atts.add(new Attribute("class",classVal));

		Instances dataRaw = new Instances(relation,atts,0);

		double[] instanceValue1 = new double[dataRaw.numAttributes()];
		for(int i=0; i<values.length; i++)
			instanceValue1[i] = (values[i] - meanVals[i]) / stdVals[i];

		dataRaw.add(new DenseInstance(1.0, instanceValue1));
		dataRaw.setClassIndex(dataRaw.numAttributes()-1);

		return dataRaw.get(0);
	}
	
	public String[] getParams(String fileName){
		String path="scripts/sample_app5/config/weka/";
		String[] result=new String[0];
		try (BufferedReader br = new BufferedReader(new FileReader(path+fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] parts = line.split(",");
            	if (parts[0].length()!=0) {
            		String[] newArray = new String[result.length + 1];
            		for (int i = 0; i < result.length; i++) {
                        newArray[i] = result[i];
                    }
            		newArray[result.length]=parts[0];
            		result=newArray;
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return result;
	}
	
	public double[] getValues(String fileName, int index,String[] params, int minus){
		int ind=0;
		String path="scripts/sample_app5/config/weka/";
		double[] result=new double[0];
		try (BufferedReader br = new BufferedReader(new FileReader(path+fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] parts = line.split(",");
            	if (params[ind].equals(parts[0])) {
            		if (parts[index].length()!=0) {
	            		double[] newArray = new double[result.length + 1];
	            		for (int i = 0; i < result.length; i++) {
	                        newArray[i] = result[i];
	                    }
	            		newArray[result.length]=Double.parseDouble(parts[index]);
	            		result=newArray;
	            		ind+=1;
	            	}
            	}
            	if (ind==params.length-minus) {
            		break;	
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return result;
	}
}
