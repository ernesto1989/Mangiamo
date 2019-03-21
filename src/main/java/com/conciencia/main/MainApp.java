package com.conciencia.main;

import com.conciencia.controllers.AdminController;
import com.conciencia.vertx.VertxConfig;
import com.conciencia.repositories.MenuRepository;
import com.conciencia.utilities.GeneralUtilities;
import java.math.BigDecimal;
import java.util.Optional;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Mangiamo Restaurant Software. Clase principal de arranque
 *
 * 1.- Se arranca y configura vertx 2.- Se inicia el Javafx API. 3.- Se muestra
 * la pantalla principal
 *
 * @author Ernesto Cantu Valle Conciencia 2018 ernesto.cantu1989@live.com
 * Versión 0.9 Octubre 10
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
        BigDecimal cantidadInicial = null;
        while (cantidadInicial == null) {
            Optional<String> result
                    = GeneralUtilities.
                            mostrarInputDialog("Cantidad Inicial Caja Chica",
                                    "Cantidad Inicial Caja Chica",
                                    "Ingrece la cantidad inicial en Caja Chica");
            try {
                cantidadInicial = new BigDecimal(result.get());
            } catch (Exception e) {}
        }
        AdminController.CANTIDAD_INICIAL_CAJA_CHICA = cantidadInicial;
        AdminController.CANTIDAD_CAJA_CHICA = cantidadInicial;
        MenuRepository.initMenu();
        VertxConfig.config();
        GeneralUtilities.abrirVentana(primaryStage, "/fxml/NuevaOrdenUI.fxml",
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
