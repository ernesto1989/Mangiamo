package com.conciencia.main;

import com.conciencia.utilities.GeneralUtilities;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Mangiamo Restaurant Software. Clase principal de arranque
 *
 * @author Ernesto Cantu Valle Conciencia 2018 ernesto.cantu1989@live.com
 * Versión 0.9 06 diciembre 2019
 */
public class MainApp extends Application {

    /**
     * Método ejecutado por JavaFX para iniciar la aplicación.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnHiding(evt->{
            GeneralUtilities.cierraMangiamo();
        });
        GeneralUtilities.abrirVentana(primaryStage, "/fxml/MainUI.fxml",
                "/styles/addbook.css", "Mangiamo");
    }

    /**
     * Método main. Punto de entrada a la aplicación
     *
     * @param args argumentos de entrada pasados por consola
     */
    public static void main(String[] args) {
        launch(args);
    }
}
