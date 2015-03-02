/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.vipsoft.ce.ui;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import ec.com.vipsoft.ce.comprobantesNeutros.FacturaDetalleBinding;
import ec.com.vipsoft.erp.gui.componentesbasicos.BotonAnadir;
import ec.com.vipsoft.erp.gui.componentesbasicos.BotonBuscar;
import ec.com.vipsoft.erp.gui.componentesbasicos.BotonCancelar;
import ec.com.vipsoft.erp.gui.componentesbasicos.BotonRegistrar;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoCantidad;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoCodigo;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoDinero;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoDireccion;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoFecha;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoNumeroComprobante;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoNumeroIdentificacion;
import ec.com.vipsoft.erp.gui.componentesbasicos.CampoRazonSocial;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;

/**
 *
 * @author chrisvv
 */
@CDIView("FACTURA")
public class FacturaView extends VerticalLayout implements View{
    private BotonBuscar botonBuscarDetalle;
    private Label labelSeleccion;
    private CampoCantidad cantidad;
    private CampoDinero valorUnitario;
    private CampoDinero descuento;
    private TextField lote;
    private BotonAnadir botonAnadirDetalle;
    private BotonRegistrar botonRegistrar;
    private BotonCancelar botonCancelar;
    private Grid tablaDetalles;
    private TextField nombreInfoAdicional;
    private TextField valorInfoAdicional;
    private BotonAnadir botonAnadirInfoAdiconal;
    private Table tablaTotales;
    private Table tablaInfoAdicional;
    private BeanContainer beanContainertotales;
    private BeanContainer beanContainerInfoAdicional;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
       if(SecurityUtils.getSubject().isAuthenticated()){
    	   if(!SecurityUtils.getSubject().hasRole("operador")){
    		   UI.getCurrent().getNavigator().navigateTo("login");
    	   }
       }else{
    	   UI.getCurrent().getNavigator().navigateTo("login");
       }
          
       
    }
    @PostConstruct
    public void postconstructor(){
       System.out.println("Hola");
        construirGui();
        iniciarEventos();
    }
    public void construirGui(){
        HorizontalLayout l1=new HorizontalLayout();
        l1.setSpacing(true);
        Label lruc=new Label("RUC");
        l1.addComponent(lruc);
        rucBeneficiario=new CampoNumeroIdentificacion();
        l1.addComponent(rucBeneficiario);
        Label lrazonSocial=new Label("R. Social");
        l1.addComponent(lrazonSocial);
        razonSocialBeneficiario=new CampoRazonSocial();
        l1.addComponent(razonSocialBeneficiario);
        Label ldireccion=new Label("Dir.");
        l1.addComponent(ldireccion);
        direccion=new CampoDireccion();
        l1.addComponent(direccion);
        fechaEmision=new CampoFecha();
        l1.addComponent(fechaEmision);
        //////////////////////////////////////////
        HorizontalLayout l2=new HorizontalLayout();
        l2.setSpacing(true);
        Label lordenCompra=new Label("OC");        
        setMargin(true);
        l2.addComponent(lordenCompra);
        ordenCompra=new TextField();
        ordenCompra.setWidth("70px");
        l2.addComponent(ordenCompra);
        Label lvencimiento=new Label("Cond. Pago");
        l2.addComponent(lvencimiento);
        formaPago=new CampoCodigo();
        formaPago.setWidth("100px");
        l2.addComponent(formaPago);
        Label lguia=new Label("Guía Remisión");
        l2.addComponent(lguia);
        numeroGuiaRemision=new CampoNumeroComprobante();
        l2.addComponent(numeroGuiaRemision);
        Label lsucursalCliente=new Label("Sucursal cliente");
        l2.addComponent(lsucursalCliente);
        codigoEstablecimientoDestino=new CampoCodigo();
        codigoEstablecimientoDestino.setInputPrompt("001");
        codigoEstablecimientoDestino.setWidth("70px");
        l2.addComponent(codigoEstablecimientoDestino);
        
        
        ////////////////////////////////
        HorizontalLayout l3=new HorizontalLayout();
        l3.setSpacing(true);
        botonBuscarDetalle=new BotonBuscar();
        l3.addComponent(botonBuscarDetalle);
        labelSeleccion=new Label();
        labelSeleccion.setWidth("200px");
        l3.addComponent(labelSeleccion);
        Label lcantidad=new Label("Cant.");
        l3.addComponent(lcantidad);
        cantidad=new CampoCantidad();
        l3.addComponent(cantidad);
        Label lvalorU=new Label("V. Unitario");
        l3.addComponent(lvalorU);
        valorUnitario=new CampoDinero();
        l3.addComponent(valorUnitario);
        Label ldescuento=new Label("Dcto.");
        l3.addComponent(ldescuento);
        descuento=new CampoDinero();
        l3.addComponent(descuento);
        Label labelLote=new Label("lote");
        l3.addComponent(labelLote);
        lote=new TextField();
        lote.setInputPrompt("lote 323, expira 2016-05-22");
        lote.setWidth("165px");
        botonAnadirDetalle=new BotonAnadir();
        l3.addComponent(lote);
        l3.addComponent(botonAnadirDetalle);
        botonRegistrar=new BotonRegistrar();
        botonCancelar = new BotonCancelar();
        //l3.addComponent(botonRegistrar);
        //l3.addComponent(botonCancelar);
        ////////////////////////////////////////
        HorizontalLayout l4=new HorizontalLayout();
        l4.setSizeFull();
        
        tablaDetalles=new Grid();
        BeanContainer beanContainer=new BeanContainer(FacturaDetalleBinding.class);
        tablaDetalles.setContainerDataSource(beanContainer);
        
//        tablaDetalles.setPageLength(5);
//        tablaDetalles.setSelectable(true);
//        tablaDetalles.setMultiSelect(false);
        tablaDetalles.setWidth("100%");
        tablaDetalles.setReadOnly(true);
        l4.addComponent(tablaDetalles);
        l4.setComponentAlignment(tablaDetalles, Alignment.MIDDLE_CENTER);
        ///////////////////////////////////////////////////////7
        HorizontalLayout l5=new HorizontalLayout();
        l5.setSpacing(true);
        l5.setSizeFull();
        VerticalLayout v1=new VerticalLayout();
        Label linfo=new Label("Info");
        HorizontalLayout vl1=new HorizontalLayout();
        vl1.setSpacing(true);
        vl1.addComponent(linfo);
        nombreInfoAdicional=new TextField();
        valorInfoAdicional=new TextField();
        botonAnadirInfoAdiconal=new BotonAnadir();
        vl1.addComponent(nombreInfoAdicional);
        vl1.addComponent(valorInfoAdicional);
        vl1.addComponent(botonAnadirInfoAdiconal);
        v1.addComponent(vl1);
        tablaInfoAdicional=new Table();
        tablaInfoAdicional.setWidth("100%");
        beanContainerInfoAdicional=new BeanContainer(InfoAdicionalB.class);
        tablaInfoAdicional.setContainerDataSource(beanContainerInfoAdicional);
        tablaInfoAdicional.setPageLength(3);
        tablaInfoAdicional.setReadOnly(true);
        v1.addComponent(tablaInfoAdicional);
        
        VerticalLayout v2=new VerticalLayout();
        v2.setSizeFull();
        tablaTotales=new Table();        
        beanContainertotales=new BeanContainer(TotalesFactura.class);
        tablaTotales.setContainerDataSource(beanContainertotales);
        tablaTotales.setPageLength(4);
        tablaTotales.setSizeFull();
        tablaTotales.setReadOnly(true);
        HorizontalLayout v2l1=new HorizontalLayout();
        v2l1.addComponent(botonRegistrar);
        v2l1.addComponent(botonCancelar);
        v2.addComponent(tablaTotales);
        v2.addComponent(v2l1);
        v2.setComponentAlignment(v2l1, Alignment.TOP_RIGHT);
        l5.addComponent(v1);
        l5.addComponent(v2);
                
        //////////////////////////////////////////////
        setSpacing(true);
        addComponent(l1);
        addComponent(l2);
        addComponent(l3);
        addComponent(l4);
        addComponent(l5);
        setComponentAlignment(l1, Alignment.MIDDLE_CENTER);
        setComponentAlignment(l2, Alignment.MIDDLE_CENTER);
        setComponentAlignment(l3, Alignment.MIDDLE_CENTER);
        setComponentAlignment(l4, Alignment.MIDDLE_CENTER);
        setComponentAlignment(l5, Alignment.MIDDLE_CENTER);
    }
    private CampoNumeroIdentificacion rucBeneficiario;
    private CampoRazonSocial razonSocialBeneficiario;
    private CampoDireccion direccion;
    private CampoFecha fechaEmision;
    private CampoNumeroComprobante numeroGuiaRemision;
    private TextField ordenCompra;
    private TextField formaPago;
    private TextField codigoEstablecimientoDestino;
    private TextField informacion_adicional;
    //private 
    //private 

    private void iniciarEventos() {
        botonCancelar.addClickListener(event -> UI.getCurrent().getNavigator().navigateTo("menu"));
    }
}
