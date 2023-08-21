import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Integer;
import java.io.*;

class Main {
    static boolean typeDataString = true;
    static boolean typeSortingAscending = true;

    public static boolean comparing(String lEl, String rEl) {
        if (typeDataString) {
            if (typeSortingAscending) {
                return lEl.compareTo(rEl) < 1;
            } else {
                return lEl.compareTo(rEl) > -1;
            }
        } else {
            try {
                if (typeSortingAscending) {
                    return Integer.parseInt(lEl) < Integer.parseInt(rEl);
                } else {
                    return Integer.parseInt(lEl) > Integer.parseInt(rEl);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }

    public static ArrayList<String> myMerge(ArrayList<String> left, ArrayList<String> right) {
        ArrayList<String> mergeResult = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparing(left.get(i), right.get(j))) {
                mergeResult.add(left.get(i));
                i++;
            } else {
                mergeResult.add(right.get(j));
                j++;
            }
        }
        while (i < left.size()) {
            mergeResult.add(left.get(i));
            i++;
        }
        while (j < right.size()) {
            mergeResult.add(right.get(j));
            j++;
        }
        return mergeResult;
    }

    public static ArrayList<String> myMergeSort(ArrayList<String> myList) {
        if (myList.size() == 1 || myList.isEmpty()) {
            return myList;
        }
        int middleList = myList.size() / 2;
        ArrayList<String> left = new ArrayList<>(myList.subList(0, middleList));
        ArrayList<String> right = new ArrayList<>(myList.subList(middleList, myList.size()));
        left = myMergeSort(left);
        right = myMergeSort(right);
        return myMerge(left, right);
    }

    public static ArrayList<String> readFile(String fileName) { //Функция считывает файл в ArrayList. Пропускает пустые строки, строки с пробелами. В случае режима '-i' пропускает строки, которые невозможно конвертировать в int.
        ArrayList<String> result = new ArrayList<>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String data = myReader.nextLine();
                if (!data.contains(" ") && !data.isEmpty()) {
                    if (!typeDataString) {
                        try {
                            Integer.parseInt(data);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                    result.add(data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(fileName + ": File's not found.");
        }
        return result;
    }

    public static boolean checkSort(ArrayList<String> myList) {
        for (int i = 0; i < (myList.size() - 1); i++) {
            if (!comparing(myList.get(i), myList.get(i + 1))) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        if (args.length > 2) {
            String fileKind = "";
            String sortKind = "";
            int i = 0;
            if (args[i].equals("-d") || args[i].equals("-a")) {
                sortKind = args[i];
                i++;
                if (args[i].equals("-i") || args[i].equals("-s")) {
                    fileKind = args[i];
                    i++;
                } else {
                    System.out.println("Incorrect type of input files.It should be a type of input file(s) '-s' or '-i'.");
                    System.exit(0);
                }
            } else {
                if (args[i].equals("-i") || args[i].equals("-s")) {
                    fileKind = args[i];
                    sortKind = "-a";
                    i++;
                    if (args[i].equals("-i") || args[i].equals("-s")) {
                        System.out.println("Incorrect second argument. It should be name of outout file.");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Incorrect type of input files. Use '-s' or '-i'.");
                    System.exit(0);
                }
            }
            if (fileKind.equals("-i")) {
                typeDataString = false;
            }
            if (sortKind.equals("-d")) {
                typeSortingAscending = false;
            }
            String outFileName = args[i];
            i++;
            ArrayList<String> resultTotal = new ArrayList<>();
            for (int temp = i; temp < args.length; temp++) {
                ArrayList<String> myFile = readFile(args[temp]);
                if (!checkSort(myFile)) {
                    myFile = myMergeSort(myFile);
                }
                resultTotal = myMerge(resultTotal, myFile);
            }
            try {
                BufferedWriter outFile = new BufferedWriter(new FileWriter(outFileName));
                for (int t = 0; t < resultTotal.size(); t++) {
                    outFile.write(resultTotal.get(t));
                    if (t != resultTotal.size() - 1) {
                        outFile.newLine();
                    }
                }
                outFile.close();
                System.out.println(outFileName + " is saved.");
            } catch (IOException e) {
                System.out.println("Can't write file.");
            }
        } else {
            System.out.println("Not enough arguments. Please, enter a type of sorting '-a' or '-d' (it's not required, '-a' by default), a type of input files '-s' or '-i', a name of output file, names of input files.");
        }
    }
}