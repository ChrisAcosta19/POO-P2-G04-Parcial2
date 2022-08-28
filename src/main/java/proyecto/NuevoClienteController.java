package proyecto;

import modelo.Validacion;
import modelo.Persona;
import modelo.Cliente;
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
/**
 * FXML Controller class
 *
 * @author chris
 */
public class NuevoClienteController implements Initializable {
    @FXML private Label lblCliente;
    @FXML private TextField txtCedulaCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private TextField txtTelefonoCliente;
    @FXML private TextField txtEmailCliente;
    @FXML private Label lblRepresentante;
    @FXML private TextField txtCedulaRepresentante;
    @FXML private TextField txtNombreRepresentante;
    @FXML private TextField txtTelefonoRepresentante;
    @FXML private TextField txtEmailRepresentante;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    
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
        Persona c = new Persona(cedulaCl,nombreCl,telefonoCl,emailCl);
        
        String cedulaR = txtCedulaRepresentante.getText();
        Validacion.validarEntero("Cedula Representante", cedulaR);
        String nombreR = txtNombreRepresentante.getText();
        Validacion.validarNombre("Representante", nombreR);
        String telefonoR = txtTelefonoRepresentante.getText();
        Validacion.validarEntero("Telefono Representante", telefonoR);
        String emailR = txtEmailRepresentante.getText();
        Validacion.validarEmail("Representante", emailR);
        Persona representante = new Persona(cedulaR, nombreR, telefonoR, emailR);
        Cliente cl = new Cliente(cedulaCl,nombreCl,telefonoCl,emailCl,representante);
        
        if(cliente == null){
            Validacion.validarPersona("Cliente",c, null);
            Validacion.validarPersona("Representante",representante, null);
        }else{
            Validacion.validarPersona("Cliente",c, cliente);
            Validacion.validarPersona("Representante",representante, cliente.getDatosRepresentante());
        }
        
        if(c.equals(representante)){
            Validacion.mensaje += "Alguno de los datos del cliente coincide con los de su representante\n";
        }
        
        if(Validacion.mensaje.equals("") && cliente == null){
            clientes.add(cl);//agregar cliente a la lista
            System.out.println("Nuevo Cliente:" + cl);

            //serializar la lista
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathClientes))) {
                out.writeObject(clientes);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Agregar cliente");
                alert.setHeaderText(null);
                alert.setContentText("Nuevo cliente agregado exitosamente");
                alert.showAndWait();
                App.setRoot("Clientes");
            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
        } else if (Validacion.mensaje.equals("") && cliente != null){
            int indice = clientes.indexOf(cliente);
            clientes.set(indice, cl);

            try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(App.pathClientes))) {
                out.writeObject(clientes);
                out.flush();

                //mostrar informacion
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Editar Cliente");
                alert.setHeaderText(null);
                alert.setContentText("Cliente editado exitosamente");

                alert.showAndWait();
                App.setRoot("Clientes");

            } catch (IOException ex) {
                System.out.println("IOException:" + ex.getMessage());
            }
            cliente = null;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al intentar guardar el cliente");
            alert.setHeaderText(null);
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