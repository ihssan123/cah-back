package org.example.controllers;
import org.example.Services.CAH;
import org.example.Services.Cluster;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

@RestController
@CrossOrigin("*")
@RequestMapping("/Cah")
public class Controller {
    private CAH _cah;
    private Cluster Cls;
    public Controller(CAH cah,Cluster Cls){
        _cah=cah;
        this.Cls=Cls;
    }
    int[][] matrice;


    @PostMapping("/Matrice_similarite")
    public Map<String, Object> MatriceSimilarite(@RequestParam("file") MultipartFile file) {
        ArrayList<String> cellValues = new ArrayList<>();
        String filePath = "C:/Users/youss/Desktop/backCAH/src/main/java/org/example/FilesData"; // Replace with your desired static path
        String savedFilePath = null;

        String output = null;
        try {
            savedFilePath = filePath + "/" + file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            FileUtils.writeByteArrayToFile(new File(savedFilePath), fileBytes);

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Perform your desired operations with the cell value
                    String cellValue = cell.toString();
                    cellValues.add(cellValue);
                }
            }

            // Define the Python script path
            String pythonScript = "C:\\Users\\youss\\Desktop\\backCAH\\src\\main\\java\\org\\example\\python\\pythonCAH.py";

            // Define the function name to call
            String functionName = "process_data";

            // Define the file path to pass as an argument
            String filePath2 = savedFilePath;

            // Create the process builder with the Python interpreter and script
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScript, functionName, filePath2);

            // Start the process
            Process process = processBuilder.start();
            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = reader.readLine();

            // Print the output
            System.out.println("Path of the saved image: " + output);
            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code: " + exitCode);


            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the file processing error
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Cls.setVilles(cellValues);
        matrice = _cah.MatriceDistance(cellValues);

        Map<String, Object> result = new HashMap<>();
        result.put("matrice", matrice);
        result.put("cellValues", cellValues);
        result.put("output", output);
        return result;
    }


    @GetMapping("/classes_CAH")
    public ArrayList<ArrayList<String>> MethodeHiarchiqueAscendante(){

        ArrayList<ArrayList<String>> classes;
        classes=_cah.MHierarchiqueAscendent(matrice,Cls);
        return classes;
    }
}
