import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.trees.RandomForest;
//import weka.classifiers.functions.VotedPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaModelCreator {
//	"edge","cloud_rsu","cloud_gsm"
	private static final String[] targets = {"edge","cloud_rsu","cloud_gsm"};
	private static final String[] targets_balance = {"edge_balance","cloud_balance"};
	private static final String[] methods = {"RandomForest"};
	
	public static void main(String[] args) throws Exception {
		String dataPath = "";
		String classifier = "";
		String regressor = "";
		String regressor_balance = "";
		String train_balance = "";
		String train_selector = "";
		String evaluate_balance = "";
		String evaluate_selector = "";
		
		JSONParser parser = new JSONParser();
        try
        {
            Object object = parser.parse(new FileReader(args[0]));
            
            //convert Object to JSONObject
            JSONObject jsonObject = (JSONObject)object;
            
            //Reading the String
            dataPath = (String) jsonObject.get("sim_result_folder");
            classifier = (String) jsonObject.get("classifier");
            regressor = (String) jsonObject.get("regressor");
            regressor_balance = (String) jsonObject.get("balance_regressor");
            train_balance = (String) jsonObject.get("train_balance");
            train_selector = (String) jsonObject.get("train_selector");
            evaluate_balance = (String) jsonObject.get("evaluate_balance");
            evaluate_selector = (String) jsonObject.get("evaluate_selector");      
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        
		System.out.println("######### TRAINING FOR " + dataPath + " #########");
		
		if (train_selector.equals("True")){
			for(int i=0; i<targets.length; i++) {
				handleClassify("train", targets[i], classifier, dataPath);
				handleRegression("train", targets[i], regressor, dataPath);
			}
		}
		
		if (train_balance.equals("True")) {
			for(int j=0; j<methods.length; j++) {
				for(int i=0; i<targets_balance.length; i++) {
					handleRegression("train", targets_balance[i], methods[j], dataPath);
				}
			}
		}
		
		System.out.println("######### EVALUATION FOR " + dataPath + " #########");
		
		if (evaluate_selector.equals("True")) {
			for(int i=0; i<targets.length; i++) {
				handleClassify("evaluate", targets[i], classifier, dataPath);
				handleRegression("evaluate", targets[i], regressor, dataPath);
			}
		}
		
		if (evaluate_balance.equals("True")) {
			for(int j=0; j<methods.length; j++) {
				for(int i=0; i<targets_balance.length; i++) {
					handleRegression("evaluate", targets_balance[i], methods[j], dataPath);
				}
			}
		}
		
	}
	
	public static void handleRegression(String action, String target, String method, String dataFolder) throws Exception {
		if(action.equals("train")) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date startDate = Calendar.getInstance().getTime();
			String now = df.format(startDate);
			System.out.println("Training " + method + " for "  + target + " started at " + now);
			
			DataSource edgeRegressionSource = new DataSource(dataFolder + "/" + target + "_regression_train.arff");
			Instances edgeRegressionDataset = edgeRegressionSource.getDataSet();
			edgeRegressionDataset.setClassIndex(edgeRegressionDataset.numAttributes()-1);

			if(method.equals("LinearRegression")) {
				LinearRegression lr = new LinearRegression();
				lr.buildClassifier(edgeRegressionDataset);
				weka.core.SerializationHelper.write(dataFolder + "/lr_" + target + ".model", lr);
			}
			else if(method.equals("SMOreg")) {
				SMOreg smoreg = new SMOreg();
				smoreg.buildClassifier(edgeRegressionDataset);
				weka.core.SerializationHelper.write(dataFolder + "/smoreg_" + target + ".model", smoreg);
			}
			else if(method.equals("MultilayerPerceptron")) {
				MultilayerPerceptron mlp = new MultilayerPerceptron();
				mlp.setLearningRate(0.1);
				mlp.setMomentum(0.2);
				mlp.setTrainingTime(1000);
				mlp.setHiddenLayers("3");
				mlp.buildClassifier(edgeRegressionDataset);
				weka.core.SerializationHelper.write(dataFolder + "/mlp_" + target + ".model", mlp);
			}
			else if(method.equals("RandomForest")) {
				
				RandomForest rf = new RandomForest();
//				rf.setMaxDepth(1000);
				rf.setNumIterations(10);
				rf.buildClassifier(edgeRegressionDataset);
				weka.core.SerializationHelper.write(dataFolder + "/rf_" + target + ".model", rf);
			}
			
			Date endDate = Calendar.getInstance().getTime();
			now = df.format(endDate);
			System.out.println("Training " + method + " for "  + target + " finished at " + now + ". It took " + getTimeDifference(startDate, endDate));
		}
		else if(action.equals("evaluate")) {
			System.out.println("Evaluation " + method + " for "  + target + " started");
			
			DataSource edgeRegressionSource = new DataSource(dataFolder + "/" + target + "_regression_test.arff");
			Instances edgeRegressionDataset = edgeRegressionSource.getDataSet();
			edgeRegressionDataset.setClassIndex(edgeRegressionDataset.numAttributes()-1);

			if(method.equals("LinearRegression")) {
				LinearRegression lr = (LinearRegression) weka.core.SerializationHelper.read(dataFolder + "/lr_" + target + ".model");
				Evaluation lrEval = new Evaluation(edgeRegressionDataset);
				lrEval.evaluateModel(lr, edgeRegressionDataset);
				System.out.println("LinearRegression");
				System.out.println(lrEval.toSummaryString());
			}
			else if(method.equals("SMOreg")) {
				SMOreg smoreg = (SMOreg) weka.core.SerializationHelper.read(dataFolder + "/smoreg_" + target + ".model");
				Evaluation svmregEval = new Evaluation(edgeRegressionDataset);
				svmregEval.evaluateModel(smoreg, edgeRegressionDataset);
				System.out.println("SMOreg");
				System.out.println(svmregEval.toSummaryString());
			}
			else if(method.equals("MultilayerPerceptron")) {
				MultilayerPerceptron mlp = (MultilayerPerceptron) weka.core.SerializationHelper.read(dataFolder + "/mlp_" + target + ".model");
				Evaluation mlpEval = new Evaluation(edgeRegressionDataset);
				mlpEval.evaluateModel(mlp, edgeRegressionDataset);
				System.out.println("MultilayerPerceptron");
				System.out.println(mlpEval.toSummaryString());
			}
			else if(method.equals("RandomForest")) {
				RandomForest rf = (RandomForest) weka.core.SerializationHelper.read(dataFolder + "/rf_" + target + ".model");
				Evaluation rfEval = new Evaluation(edgeRegressionDataset);
				rfEval.evaluateModel(rf, edgeRegressionDataset);
				System.out.println(rfEval.toSummaryString());
			}
			

			System.out.println("Evaluation " + method + " for "  + target + " finished");
			System.out.println("");
		}
	}
	
	public static void handleClassify(String action, String target, String method, String dataFolder) throws Exception {
		if(action.equals("train")) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date startDate = Calendar.getInstance().getTime();
			String now = df.format(startDate);
			System.out.println("Training " + method + " for "  + target + " started at " + now);
			
			DataSource classifierSource = new DataSource(dataFolder + "/" + target + "_classifier_train.arff");
			Instances classifierDataset = classifierSource.getDataSet();
			classifierDataset.setClassIndex(classifierDataset.numAttributes()-1);
			
			if(method.equals("NaiveBayes")) {
				NaiveBayes nb = new NaiveBayes();
				nb.buildClassifier(classifierDataset);
				weka.core.SerializationHelper.write(dataFolder + "/nb_" + target + ".model", nb);
			}
			else if(method.equals("SMO")) {
				SMO smo = new SMO();
				smo.buildClassifier(classifierDataset);
				weka.core.SerializationHelper.write(dataFolder + "/smo_" + target + ".model", smo);
			}
			else if(method.equals("MultilayerPerceptron")) {
				MultilayerPerceptron mlp = new MultilayerPerceptron();
				mlp.setLearningRate(0.1);
//				mlp.setMomentum(0.2);
				mlp.setTrainingTime(1000);
//				mlp.setHiddenLayers("2");
				mlp.buildClassifier(classifierDataset);
				weka.core.SerializationHelper.write(dataFolder + "/mlp_" + target + ".model", mlp);
			}
//			else if(method.equals("VotedPerceptron")) {
//				VotedPerceptron vp = new VotedPerceptron();
//				vp.buildClassifier(classifierDataset);
//				weka.core.SerializationHelper.write(dataFolder + "/vp_" + target + ".model", vp);
//			}
			else if(method.equals("RandomForest")) {
				RandomForest rf = new RandomForest();
				rf.setMaxDepth(100);
				rf.setNumIterations(10);
				rf.buildClassifier(classifierDataset);
				weka.core.SerializationHelper.write(dataFolder + "/rf_" + target + ".model", rf);
			}
			
			
			Date endDate = Calendar.getInstance().getTime();
			now = df.format(endDate);
			System.out.println("Training " + method + " for "  + target + " finished at " + now + ". It took " + getTimeDifference(startDate, endDate));
		}
		else if(action.equals("evaluate")) {
			System.out.println("Evaluation " + method + " for "  + target + " started");
			
			DataSource edgeClassifierSource = new DataSource(dataFolder + "/" + target + "_classifier_test.arff");
			Instances classifierDataset = edgeClassifierSource.getDataSet();
			classifierDataset.setClassIndex(classifierDataset.numAttributes()-1);
			
			if(method.equals("NaiveBayes")) {
				NaiveBayes nb = (NaiveBayes) weka.core.SerializationHelper.read(dataFolder + "/nb_" + target + ".model");
				Evaluation nbEval = new Evaluation(classifierDataset);
				nbEval.evaluateModel(nb, classifierDataset);
				System.out.println(nbEval.toSummaryString());
				System.out.println(nbEval.toMatrixString());
				System.out.println(nbEval.toClassDetailsString());
			}
			else if(method.equals("SMO")) {
				SMO smo = (SMO) weka.core.SerializationHelper.read(dataFolder + "/smo_" + target + ".model");
				Evaluation smoEval = new Evaluation(classifierDataset);
				smoEval.evaluateModel(smo, classifierDataset);
				System.out.println(smoEval.toSummaryString());
				System.out.println(smoEval.toMatrixString());
				System.out.println(smoEval.toClassDetailsString());
			}
			else if(method.equals("MultilayerPerceptron")) {
				MultilayerPerceptron mlp = (MultilayerPerceptron) weka.core.SerializationHelper.read(dataFolder + "/mlp_" + target + ".model");
				Evaluation mlpEval = new Evaluation(classifierDataset);
				mlpEval.evaluateModel(mlp, classifierDataset);
				System.out.println(mlpEval.toSummaryString());
				System.out.println(mlpEval.toMatrixString());
				System.out.println(mlpEval.toClassDetailsString());
			}
//			else if(method.equals("VotedPerceptron")) {
//				VotedPerceptron vp = (VotedPerceptron) weka.core.SerializationHelper.read(dataFolder + "/vp_" + target + ".model");
//				Evaluation vpEval = new Evaluation(classifierDataset);
//				vpEval.evaluateModel(vp, classifierDataset);
//				System.out.println(vpEval.toSummaryString());
//				System.out.println(vpEval.toMatrixString());
//				System.out.println(vpEval.toClassDetailsString());
//			}
			else if(method.equals("RandomForest")) {
				RandomForest rf = (RandomForest) weka.core.SerializationHelper.read(dataFolder + "/rf_" + target + ".model");
				Evaluation rfEval = new Evaluation(classifierDataset);
				rfEval.evaluateModel(rf, classifierDataset);
				System.out.println(rfEval.toSummaryString());
				System.out.println(rfEval.toMatrixString());
				System.out.println(rfEval.toClassDetailsString());
			}
			
			System.out.println("Evaluation " + method + " for "  + target + " finished");
			System.out.println("");
		}
	}
	
	private static String getTimeDifference(Date startDate, Date endDate){
		String result = "";
		long duration  = endDate.getTime() - startDate.getTime();

		long diffInMilli = TimeUnit.MILLISECONDS.toMillis(duration);
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
		
		if(diffInDays>0)
			result += diffInDays + ((diffInDays>1 == true) ? " Days " : " Day ");
		if(diffInHours>0)
			result += diffInHours % 24 + ((diffInHours>1 == true) ? " Hours " : " Hour ");
		if(diffInMinutes>0)
			result += diffInMinutes % 60 + ((diffInMinutes>1 == true) ? " Minutes " : " Minute ");
		if(diffInSeconds>0)
			result += diffInSeconds % 60 + ((diffInSeconds>1 == true) ? " Seconds" : " Second");
		if(diffInMilli>0 && result.isEmpty())
			result += diffInMilli + ((diffInMilli>1 == true) ? " Milli Seconds" : " Milli Second");
		
		return result;
	}
}
