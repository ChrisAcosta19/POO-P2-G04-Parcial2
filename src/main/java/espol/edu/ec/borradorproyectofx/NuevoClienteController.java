/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package espol.edu.ec.borradorproyectofx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.*;
/**
 * FXML Controller class
 *
 * @author chris
 */
public class NuevoClienteController implements Initializable {


    @FXML
    private Label lblCliente;
    @FXML
    private TextField txtCedulaCliente;
    @FXML
    private TextField txtNombreCliente;
    @FXML
    private TextField txtTelefonoCliente;
    @FXML
    private TextField txtEmailCliente;
    @FXML
    private Label lblRepresentante;
    @FXML
    private TextField txtCedulaRepresentante;
    @FXML
    private TextField txtNombreRepresentante;
    @FXML
    private TextField txtTelefonoRepresentante;
    @FXML
    private TextField txtEmailRepresentante;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    
    public static Cliente cliente;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCancelar.setOnAction(eh -> {
            try {
                App.setRoot("Clientes");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }    
    
    @FXML
    private void guardarCliente(ActionEvent event) {
        ArrayList<Cliente> clientes = Cliente.cargarClientes(App.pathClientes);//cargar la lista del archivo
        System.out.println("Guardando cliente");
        
        String cedulaCl = txtCedulaCliente.getText();
        Validacion.validarEntero("Cedula Cliente", cedulaCl);
        String nombreCl = txtNombreCliente.getText();
        Validacion.validarNombre("Cliente", nombreCl);
        String telefonoCl = txtTelefonoCliente.getText();
        Validacion.validarEntero("Telefono Cliente", telefonoCl);
        String emailCl = txtEmailCliente.getText();
        Validacion.validarEmail("Cliente", emailCl);
        
        Validacion.validarEntero("Cedula Representante", txtCedulaRepresentante.getText());
        Validacion.validarNombre("Representante", txtNombreRepresentante.getText());
        Validacion.validarEntero("Telefono Representante", txtTelefonoRepresentante.getText());
        Validacion.validarEmail("Representante", txtEmailRepresentante.getText());
        
        if(Validacion.mensaje.equals("")){
            Persona representante = new Persona(txtCedulaRepresentante.getText(),
                    txtNombreRepresentante.getText(),
                    txtTelefonoRepresentante.getText(),
                    txtEmailRepresentante.getText());
            Cliente cl = new Cliente(cedulaCl,nombreCl,telefonoCl,emailCl,representante);
            if (cliente == null) {
                clientes.add(cl);//agregar cliente a la lista
                System.out.println("Nuevo Cliente:" + cl);
                
                //serializar la lista
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathClientes))) {
                    out.writeObject(clientes);
                    out.flush();

                    //mostrar informacion
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Resultado de la operación");
                    alert.setContentText("Nuevo cliente agregado exitosamente");
                    alert.showAndWait();
                    App.setRoot("Clientes");
                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
            } else {
                int indice = clientes.indexOf(cliente);
                clientes.set(indice, cl);

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathClientes))) {
                    out.writeObject(clientes);
                    out.flush();

                    //mostrar informacion
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Resultado de la operación");
                    alert.setContentText("Cliente editado exitosamente");

                    alert.showAndWait();
                    App.setRoot("Clientes");

                } catch (IOException ex) {
                    System.out.println("IOException:" + ex.getMessage());
                }
                cliente = null;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error");
            alert.setContentText(Validacion.mensaje);
            alert.showAndWait();
            Validacion.mensaje = "";
        }
    }
    
    public void llenarCampos(Cliente cl){
        lblCliente.setText("Editar Cliente");
        txtCedulaCliente.setText(cl.getCedula());
        txtCedulaCliente.setEditable(false);
        txtNombreCliente.setText(cl.getNombre());
        txtTelefonoCliente.setText(cl.getTelefono());
        txtEmailCliente.setText(cl.getEmail());
        
        lblRepresentante.setText("Editar Representante");
        txtCedulaRepresentante.setText(cl.getDatosRepresentante().getCedula());
        txtNombreRepresentante.setText(cl.getDatosRepresentante().getNombre());
        txtTelefonoRepresentante.setText(cl.getDatosRepresentante().getTelefono());
        txtEmailRepresentante.setText(cl.getDatosRepresentante().getEmail());
    }
}