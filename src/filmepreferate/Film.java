/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filmepreferate;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Film {
 int id;
 SimpleStringProperty titlu;
 SimpleIntegerProperty an;
 SimpleDoubleProperty nota;
 
 public Film(int id, String titlu, int an, double nota) {
 this.id = id;
 this.titlu = new SimpleStringProperty(titlu);
 this.an = new SimpleIntegerProperty(an);
 this.nota = new SimpleDoubleProperty(nota);
 }
}

