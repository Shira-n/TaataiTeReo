package application.model.admin;

import application.TataiApp;
import application.controller.MainPageController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class manages the write and read of custom questions
 */
public class CustomManager {
    private File _public;
    private File _private;
    private String _publicPath;
    private String _privatePath;
    private Map<String, ArrayList<String>> _publicSuites = new HashMap<>();
    private Map<String, ArrayList<String>> _privateSuites = new HashMap<>();
    private User _usr;

    public CustomManager(){
        _usr = MainPageController.getUser();
        _publicPath = TataiApp.getPublicCustomDir();
        _privatePath = TataiApp.getCustomDir()+ _usr.getName() + "/";
        _public = new File(_publicPath);
        _private = new File(_privatePath);
        readStorage(_public, _publicSuites);
        readStorage(_private, _privateSuites);
    }

    /**
     * Read the specified question storage directory and store information into the corresponding field.
     */
    private void readStorage(File dir, Map<String, ArrayList<String>> suite ){
        if (!dir.exists()){
            dir.mkdir();
        }
        //Read private storage
        if (dir.listFiles().length < 1){
            suite = null;
        }else{
            for (File f : dir.listFiles()){
                //Create a scanner
                Scanner sc = null;
                try {
                    sc = new Scanner(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Read the question suite file
                String id = f.getName().substring(0, 7);
                //Read abstraction
                ArrayList<String> abs = new ArrayList<>();
                String author = sc.nextLine().split("#")[1];
                String desp = sc.nextLine().split("#")[1];
                String total = sc.nextLine().split("#")[1];
                abs.add(author);
                abs.add(desp);
                abs.add(total);
                sc.close();
                suite.put(id, abs);
            }
        }
    }

    /**
     * Read a specified question suite and return the question suite in a standard format
     */
    public Map<Integer, String> readQuestionSuite(String id, boolean isPublic){
        String path;
        if (isPublic){
            path = _publicPath + id + ".txt";
        }else{
            path = _privatePath + id + ".txt";
        }

        //Create a scanner
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Ignore first three line
        sc.nextLine();
        sc.nextLine();
        sc.nextLine();

        //Read the Record
        Map<Integer, String> questionSuite = new HashMap<>();
        int index = 0;
        while (sc.hasNextLine()) {
            questionSuite.put(index, sc.nextLine());
            index++;
        }
        sc.close();
        return questionSuite;
    }

    /**
     * Write a new question Suite
     */
    public void writeCustomSuite(String id, String desp, ArrayList<String> qs, boolean isPublic){
        File newSuite;
        if (isPublic){
            newSuite = new File(_public.getPath() + "/" + id + ".txt" );
        }else{
            newSuite = new File(_private.getPath() + "/" + id + ".txt" );
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(newSuite, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println( "author#" + _usr.getName());
        writer.println( "disp#" + desp);
        writer.println( "total#" + qs.size());
        for (String s : qs){
            writer.println(s);
        }
        writer.close();
    }


    public void deleteQuestionSuite(String id, boolean isPublic){
        File newSuite;
        if (isPublic){
            newSuite = new File(_public.getPath() + "/" + id + ".txt" );
        }else{
            newSuite = new File(_private.getPath() + "/" + id + ".txt" );
        }
        newSuite.delete();
    }


    public Map<String, ArrayList<String>> getPublicSuites() {
        return _publicSuites;
    }


    public Map<String, ArrayList<String>> getPrivateSuites() {
        return _privateSuites;
    }
}
