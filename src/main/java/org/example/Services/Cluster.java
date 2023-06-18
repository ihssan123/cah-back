package org.example.Services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class Cluster {
    private ArrayList<String> elements;
    public Cluster(String s) {
        elements = new ArrayList<>();
        elements.add(s);
    }

    public Cluster(ArrayList<String> elements) {
        this.elements = elements;
    }

    public ArrayList<String> getElements() {
        return elements;
    }

    public void setElements(ArrayList<String> elements) {
        this.elements = elements;
    }


    public Cluster() {

    }
}
