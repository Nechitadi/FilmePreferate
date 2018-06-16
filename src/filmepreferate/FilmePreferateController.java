package filmepreferate;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FilmePreferateController {

    Connection cnx;

    int gen;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Film, Integer> cAn;

    @FXML
    private TableColumn<Film, String> cTitlu;

    @FXML
    private TableColumn<Film, Double> cNota;

    @FXML
    private ListView<String> genuri;

    @FXML
    private TableView<Film> tabel;

    @FXML
    private TextField titlu;

    @FXML
    private TextField an;

    @FXML
    private TextField nota;

    @FXML
    void adauga(ActionEvent event) {
//        String titlulFilmului;
//        titlulFilmului = titlu.getText();
//        int anulAparitiei = Integer.parseInt(an.getText());
//        double notaFilmului = Double.parseDouble(nota.getText());
//        // Inserez in baza de date
//        String cda = "insert into filme (`id`, `gen`, `titlu`, `an`, `nota`) VALUES (NULL, '"
//                + gen + "', '" + titlulFilmului + "', '"
//                + anulAparitiei + "', '" + notaFilmului + "');";
//        System.out.println(cda);
        
//        INSERT INTO `filme` (`id`, `titlu`, `an`, `nota`, `gen`) VALUES (NULL, 'Ion', '1970', '2', '0');

        try {
            String titlulFilmului;
            titlulFilmului = titlu.getText();
            int anulAparitiei = Integer.parseInt(an.getText());
            double notaFilmului = Double.parseDouble(nota.getText());
            // Inserez in baza de date
            String cda = "insert into filme (`id`, `gen`, `titlu`, `an`, `nota`) VALUES (NULL, '"
                + gen + "', '" + titlulFilmului + "', '"
                + anulAparitiei + "', '" + notaFilmului + "');";
            System.out.println(cda);
            try (Statement stm = cnx.createStatement()) {
                stm.executeUpdate(cda);
            }
            incarc(gen);
        } catch (SQLException ex) {

            Logger.getLogger(FilmePreferateController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @FXML
    void corecteaza(ActionEvent event) {
        try {
            // Preiau filmul curent, selectat in TableView
            int poz = (int) tabel.getSelectionModel().getSelectedIndex();
            // Preiau din lista de filme filmul curent
            Film f = tabel.getItems().get(poz);
            // Creez comanda update
            String cda = "UPDATE filme "
                    + "SET titlu = '" + titlu.getText()
                    + "', an = '" + String.valueOf(an.getText())
                    + "', nota = '" + String.valueOf(nota.getText())
                    + "' WHERE filme.id = '" + f.id + "'";

//            UPDATE `filme` SET `titlu` = 'Spiderman 2', `an` = '2006', `nota` = '9' WHERE `filme`.`id` = 1;
            System.out.println("cda: " + cda);
            Statement stm;
            stm = cnx.createStatement();
            stm.executeUpdate(cda);
            stm.close();

        } catch (SQLException ex) {

            Logger.getLogger(FilmePreferateController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        incarc(gen);
    }

    @FXML
    void sterge(ActionEvent event) {
        try {
            // Preiau filmul curent, selectat in TableView
            int poz = (int) tabel.getSelectionModel().getSelectedIndex();
            Film f = tabel.getItems().get(poz);
            int id = f.id;
            String cda = "delete from filme where id = " + id;
            try (Statement stm = cnx.createStatement()) {
                stm.executeUpdate(cda);
            }
            // Reincarc controlul tabel
            incarc(gen);
        } catch (SQLException ex) {

            Logger.getLogger(FilmePreferateController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void initialize() {

        try {
            // Se adauga categoriile
            genuri.getItems().add("Actiune");
            genuri.getItems().add("Animatie");
            genuri.getItems().add("Aventura");
            genuri.getItems().add("Comedie");
            genuri.getItems().add("Dragoste");
            genuri.getItems().add("Drama");
            genuri.getItems().add("Documentar");
            genuri.getItems().add("Familie");
            genuri.getItems().add("Istoric");
            genuri.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            // Se impune elementul 0 din listă ca element deja selectat.
            genuri.getSelectionModel().select(0);

            cTitlu.setCellValueFactory(cellData
                    -> cellData.getValue().titlu);
            cAn.setCellValueFactory(cellData
                    -> cellData.getValue().an.asObject());
            cNota.setCellValueFactory(cellData
                    -> cellData.getValue().nota.asObject());

            gen = genuri.getSelectionModel().getSelectedIndex();

            // Realizez conexiunea cu baza de date
            Class.forName("com.mysql.jdbc.Driver");
            cnx = DriverManager.getConnection("jdbc:mysql://localhost/filme_preferate?characterEncoding=utf8", "root", "");

            incarc(gen);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FilmePreferateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conectare cu succes");

        // Selecteaza filmele din baza de date dupa gen
        genuri.getSelectionModel().selectedIndexProperty().
                addListener((ov, valVeche, valNoua) -> {
                    // Se trateaza schimbarea starii.
                    // valNoua da pozitia noii valori selectate în tabel
                    gen = (int) valNoua;
                    incarc(gen);
                });

        // Selecteaza linia
        tabel.getSelectionModel().selectedIndexProperty().
                addListener((ov, valVeche, valNoua) -> {
                    // valNoua da pozitia liniei selectate în tabel
                    int linie = (int) valNoua;
                    if (linie >= 0) {
                        Film f = tabel.getItems().get(linie);
                        // Campurile lui f se transfera in controale:
                        titlu.setText(f.titlu.get());
                        an.setText(String.valueOf(f.an.get()));
                        nota.setText(String.valueOf(f.nota.get()));
                    }
                });

    }

    private void incarc(int gen) {
        tabel.getItems().clear();
        String cda = "select * from filme where gen = "
                + gen + " order by titlu";
        System.out.println("cda: " + cda);
        ResultSet rs;
        try {
            Statement stm;
            stm = cnx.createStatement();
            rs = stm.executeQuery(cda);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String titlulFilmului = rs.getString("TITLU");
                int anulAparitiei = rs.getInt("AN");
                double notaFilmului = rs.getDouble("NOTA");
                // Creez un obiect din clasa Film
                Film f;
                f = new Film(id, titlulFilmului, anulAparitiei, notaFilmului);
                tabel.getItems().add(f);
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(FilmePreferateController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String apostrof(String text) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
