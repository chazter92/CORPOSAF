/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contar.productos;

import BDO.BDO_Categoria_Producto;
import DAO.DAO_Categoria_Producto;
import com.alee.laf.combobox.WebComboBox;
import contar.Catalogos;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author Chaz
 */
public class CmbBuscarCategoriaProducto extends WebComboBox{
    private DAO_Categoria_Producto connCategoria = new DAO_Categoria_Producto();
    private BDO_Categoria_Producto categoriaSeleccionada;
    private FrmCategoriaProducto ventanaPadre;
    
    public CmbBuscarCategoriaProducto(FrmCategoriaProducto ventanaPadre) {
        super();
        this.ventanaPadre = ventanaPadre;
        createUI();
    }
    
    private void createUI(){
        setEditable(true);
        setEditorColumns(20);
        setToolTipText("Ingrese código o descripción");
        connCategoria = new DAO_Categoria_Producto();
        cargarCategoriasProducto(Catalogos.getCategorias_Producto());
        setCategoriaSeleccionada(Catalogos.getCategorias_Producto().get(0));
        getEditor().getEditorComponent().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String buscado = ((JTextField) getEditor().getEditorComponent()).getText();
                if (!buscado.isEmpty()) {
                    cargarCategoriasProducto(new ArrayList(connCategoria.busquedaCodigoTipo(buscado.trim()).values()));
                } else {
                    cargarCategoriasProducto(Catalogos.getCategorias_Producto());
                }
                setSelectedItem(buscado);
                setPopupVisible(true);
            }
        });
        
        addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    WebComboBox localCombo = (WebComboBox)e.getSource();
                    if(verificarCategoriaSeleccionada(localCombo.getSelectedItem())){
                        ventanaPadre.actualizarDatos(categoriaSeleccionada);
                    }
                }
            }
            
        });
        
        getEditor().getEditorComponent().addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {

                ((JTextField)getEditor().getEditorComponent()).selectAll();                
            }

            @Override
            public void focusLost(FocusEvent e) {
                
            }
            
        });
    }
    
    private boolean verificarCategoriaSeleccionada(Object item){
        if(item != null){
            String itemStr = item.toString();
            if(!itemStr.isEmpty()){
                String[] datosCliente = itemStr.split(" | ");
                if(datosCliente.length>2){
                    BDO_Categoria_Producto categoriaEncontrada = Catalogos.buscarCategoriaProductoInt(Integer.parseInt(datosCliente[0]));
                    if(categoriaEncontrada !=null){
                        setCategoriaSeleccionada(categoriaEncontrada);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void cargarCategoriasProducto(ArrayList<BDO_Categoria_Producto> categoriasEncontradas) {
        if (categoriasEncontradas != null) {
            removeAllItems();
            for (int i = 0; i < categoriasEncontradas.size(); i++) {
                addItem(categoriasEncontradas.get(i).basicsToString());
            }
        }        
    }

    public BDO_Categoria_Producto getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(BDO_Categoria_Producto categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

}
