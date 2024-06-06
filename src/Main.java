import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter k: ");
        //Number of Neighbours
        int k = scanner.nextInt();

        double correctlyClassified = 0;
        double totalNumberOfInstances = 0;
        double accuracy;

        //Read training set from file
        List<List<String>> trainingSet = readDataset("datasets/iris.data");

        //Read test set from file
        List<List<String>> testSet = readDataset("datasets/iris.test.data");

        //List to store distances for every test instance
        List<Double> distances = new ArrayList<>();

        //Iterate through each item in the test set
        for (int i = 0; i < testSet.size(); i++) {
            distances = new ArrayList<>();

            //Classify the item using kNN algorithm
            String classified = knn(trainingSet, testSet.get(i), distances, k);

            //Check if the classification is correct
            if (classified.equals(testSet.get(i).get(testSet.get(i).size() - 1))) {
                correctlyClassified++;
            }
            totalNumberOfInstances++;
        }

        //Calculate accuracy
        accuracy = correctlyClassified / totalNumberOfInstances;
        System.out.println("Accuracy: " + accuracy);

        //Allow user to input new vectors
        System.out.println("Do you want to input a new vector? [Y/N]");
        String input = scanner.next();
        while (!(input.equals("N"))) {
            if (input.equals("Y")) {
                List<String> newVector = new ArrayList<>();
                for (int i = 0; i < trainingSet.get(0).size(); i++) {
                    System.out.print("Enter value for attribute " + (i + 1) + ": ");

                    //Add the new vector to the test set
                    newVector.add(scanner.next());
                }

                //Classify new vector
                String classified = knn(trainingSet, newVector, distances, k);

                //Check if the classification is correct
                if (classified.equals(newVector.get(newVector.size() - 1))) {
                    correctlyClassified++;
                }
                totalNumberOfInstances++;

                //Calculate accuracy
                accuracy = correctlyClassified / totalNumberOfInstances;
                System.out.println("Accuracy: " + accuracy);
                System.out.println("Do you want to input a new vector?");
                input = scanner.next();
            }
        }
    }

    //Calculate Euclidean distance between two instances
    public static double calculateEuclideanDistance(List<String> trainingInstance, List<String> testInstance) {
        double distance = 0;
        for (int i = 0; i < trainingInstance.size() - 1; i++) {
            distance += Math.pow(Double.parseDouble(trainingInstance.get(i)) - Double.parseDouble(testInstance.get(i)), 2);
        }
        return Math.sqrt(distance);
    }

    //Find the most frequent element in an array
    public static String findMostFrequentElement(String[] names) {
        int maxCount = 0;
        String maxElement = null;
        for (String name : names) {
            int count = 0;
            for (String otherName : names) {
                if (name.equals(otherName)) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxElement = name;
            }
        }
        return maxElement;
    }

    //k-Nearest Neighbours algorithm
    public static String knn(List<List<String>> trainingSet, List<String> testInstance, List<Double> distances, int k) {

        //Calculate distances between the test instance and all instances in the training set
        for (List<String> trainingInstance : trainingSet) {
            distances.add(calculateEuclideanDistance(trainingInstance, testInstance));
        }

        //Array to store classes of nearest neighbours
        String[] names = new String[k];

        //Select k nearest neighbours
        for (int j = 0; j < k; j++) {
            int minIndex = 0;
            for (int l = 1; l < distances.size(); l++) {
                if (distances.get(minIndex) > distances.get(l)) {
                    minIndex = l;
                }
            }

            //Taking nearest neighbour and removing it from distances
            names[j] = trainingSet.get(minIndex).get(trainingSet.get(minIndex).size() - 1);
            distances.remove(minIndex);
        }

        //Find the most frequent class in k nearest neighbours
        String classified = findMostFrequentElement(names);

        //Print the test instance and its classification
        for (String attribute : testInstance) {
            System.out.print(attribute + " ");
        }
        System.out.println("Classified as: " + classified);

        return classified;
    }

    // Method to read dataset from file
    public static List<List<String>> readDataset(String fileName) throws IOException {
        List<List<String>> dataset = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                dataset.add(Arrays.asList(values));
            }
        }
        return dataset;
    }
}
