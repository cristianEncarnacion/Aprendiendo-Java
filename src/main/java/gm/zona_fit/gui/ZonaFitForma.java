package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.ClienteServicio;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Component
public class ZonaFitForma extends JFrame
{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField memebresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private Integer idCliente;


    IClienteServicio clienteServicio;

    private DefaultTableModel tablaModeloClientes;

    @Autowired
    public ZonaFitForma(ClienteServicio clienteServicio){
        this.clienteServicio=clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> {
            eliminarCliente();

        });
        limpiarButton.addActionListener(e -> {
            limpiarFormulario();
        });
    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Para que se cierre la ventana
        setSize(900,700);
        setLocationRelativeTo(null); // Para que se centre la ventana
    }




    private void createUIComponents() {
        // TODO: place custom component creation code here
//        this.tablaModeloClientes=new DefaultTableModel(0,4);
        this.tablaModeloClientes=new DefaultTableModel(0,4){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };
        String[] cabeceros={"Id","Nombre","Apellido","Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);
        this.clientesTabla=new JTable(tablaModeloClientes);
//Reestringimos la seleccion de la tabla a 1 solo registro;
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);








        // Cargar el listado de clientes
        listarClientes();

    }

    private void listarClientes(){
        this.tablaModeloClientes.setRowCount(0);
        var clientes= this.clienteServicio.listarClientes();
        clientes.forEach(cliente -> {
            Object[] renglonCiente={
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(renglonCiente);
        });

    }

    private void guardarCliente(){
    if(nombreTexto.getText().equals("")){
        mostrarMensaje("Proporciona un nombre");
        nombreTexto.requestFocusInWindow();
        return;
    }
    if(memebresiaTexto.getText().equals("")){
        mostrarMensaje("Proporciona una membresia");
        memebresiaTexto.requestFocusInWindow();
        return;
    }


        // Recuperamos los valores del formulario
        var nombre=nombreTexto.getText();
        var apellido=apellidoTexto.getText();
        var membresia=Integer.parseInt(memebresiaTexto.getText());
        var cliente= new Cliente(this.idCliente,nombre,apellido,membresia);
        this.clienteServicio.guardarCliente(cliente);// Insertamos el objeto en la BD

        if(this.idCliente ==null){
            mostrarMensaje("Se agrego el nuevo cliente");
        } else{
            mostrarMensaje("Se actualizo el cliente");
        }
        limpiarFormulario();
        listarClientes();
    }
    private void eliminarCliente(){

        var renglon= clientesTabla.getSelectedRow();
        if(renglon != -1){
            var idClienteStr=clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente=Integer.parseInt(idClienteStr);
            var cliente=new Cliente();
            cliente.setId(this.idCliente);
            clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("Cliente con id " + this.idCliente + " eliminado");
            limpiarFormulario();
            listarClientes();
        } else{
            mostrarMensaje("Debe seleccionar un cliente a eliminar");

        }
//        // Recuperamos los valores del formulario
//        var nombre=nombreTexto.getText();
//        var apellido=apellidoTexto.getText();
//        var membresia=Integer.parseInt(memebresiaTexto.getText());
//        var cliente= new Cliente(this.idCliente,nombre,apellido,membresia);
//        this.clienteServicio.eliminarCliente(cliente);// Insertamos el objeto en la BD
//        mostrarMensaje("Cliente eliminado");
//        limpiarFormulario();
//        listarClientes();
    }


    private void cargarClienteSeleccionado(){
        var renglon=clientesTabla.getSelectedRow();
        if(renglon != -1){ // Significa que no se selecciono ningun registro
            var id=clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente=Integer.parseInt(id);
            var nombre=clientesTabla.getModel().getValueAt(renglon,1).toString();
            this.nombreTexto.setText(nombre);
            var apellido=clientesTabla.getModel().getValueAt(renglon,2).toString();
            this.apellidoTexto.setText(apellido);
            var membresia= clientesTabla.getModel().getValueAt(renglon,3).toString();
            this.memebresiaTexto.setText(membresia);
        }
    }


    private void mostrarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this,mensaje);
    }

    private void limpiarFormulario(){
        //Limpiamos el id del cliente seleccionado
        this.idCliente=null;
        nombreTexto.setText("");
        apellidoTexto.setText("");
        memebresiaTexto.setText("");

        // Deseleccionamos el registro seleccionado de la tabla
        this.clientesTabla.getSelectionModel().clearSelection();
    }
}
