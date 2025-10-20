package defaultpackage;

import data.*;
import mining.*;
import keyboardinput.*;

import java.io.IOException;

public class MainTest {

    /**
     * @param args
     */
    public static void main(String[] args) throws EmptyDatasetException {
        do {
            //int numIter=0;
            double radius = 0.0;
            Data data = new Data("playtennis");
            QTMiner qt = null;
            int choice = 0;
            System.out.println("Scegli una opzione \n (1) Carica Cluster da File \n (2) Carica dati \n Risposta: ");
            choice = Keyboard.readInt();


            if (choice == 1) {
                System.out.println("Nome archivio:radius ");
                radius = Keyboard.readDouble();
                qt = new QTMiner(radius);
                try {
                    qt.compute(data);
                    qt.salva("/home/paprika/dataSet.txt");
                } catch (ClusteringRadiusException | IOException e) {
                    e.printStackTrace();
                }
                try {
                    QTMiner qtminer = new QTMiner("/home/paprika/dataSet.txt");
                    if (qtminer != null) {
                        System.out.println(qtminer.getC().toString());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }


            } else {
                System.out.println(data);
                do {
                    do {
                        System.out.println("Insert radius (>0)= ");
                        radius = Keyboard.readDouble();
                        qt = new QTMiner(radius);
                    } while (radius <= 0 || radius > 4);

                    try {
                        int numIter = qt.compute(data);
                        System.out.println("Number of clusters:" + numIter);
                    } catch (ClusteringRadiusException e) {
                        System.out.println(e.getMessage());
                    }


                    System.out.println(qt.getC().toString(data));
                    System.out.println("Backup file name:radius" +(int) radius+ "\nSaving clusters in radius"+
                            (int) radius+".dmp\nSaving transaction ended!");
                    System.out.println("New execution?(y/n)");
                } while (Keyboard.readChar() == 'y');


            }
            System.out.println("Would you choose another option from the menu?(y/n)");
        } while (Keyboard.readChar() == 'y');
    }


}





/**
 * metodo che dice che cecilia è scema.
*/
/*
public cecilia getScema() {
    return scema;
}
*/