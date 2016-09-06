/*
 * Copyright (c) 2016 Daniel Campodonico; Emiliano Gioria; Lucas Moretti.
 * This file is part of Lab02.
 *
 * Lab02 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lab02 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lab02.  If not, see <http://www.gnu.org/licenses/>.
 */

package dam.isi.frsf.utn.edu.ar.lab02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private Spinner spnHorario;
    private TextView tvPedidos;
    private RadioGroup rgOpcionesPlato;
    private Button buttonAgregar, buttonConfirmar, buttonReiniciar;
    private ListView listViewOpciones;
    private ArrayList<ElementoMenu> listElementos;
    private ArrayAdapter<ElementoMenu> listAdapterOpciones;
    private ElementoMenu pedidoActual;
    private ArrayAdapter<CharSequence> adapterSpinner;
    private double cuenta;
    private boolean pedidoConfirmado;

    private DecimalFormat f = new DecimalFormat("##.00");
    private ElementoMenu[] listaBebidas;
    private ElementoMenu[] listaPlatos;
    private ElementoMenu[] listaPostre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setParametros();

        listViewOpciones.setAdapter(listAdapterOpciones);
        listViewOpciones.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        spnHorario.setAdapter(adapterSpinner);

        rgOpcionesPlato.setOnCheckedChangeListener(this);
        listViewOpciones.setOnItemClickListener(this);
        buttonAgregar.setOnClickListener(this);
        buttonConfirmar.setOnClickListener(this);
        buttonReiniciar.setOnClickListener(this);

        tvPedidos.setMovementMethod(new ScrollingMovementMethod());
    }

    private void setParametros(){
        iniciarListas();
        spnHorario = (Spinner) findViewById(R.id.spnHorario);
        tvPedidos = (TextView) findViewById(R.id.tvPedidos);
        rgOpcionesPlato = (RadioGroup) findViewById(R.id.rgOpcionesPlato);
        buttonAgregar = (Button) findViewById(R.id.buttonAgregar);
        buttonConfirmar = (Button) findViewById(R.id.buttonConfirmar);
        buttonReiniciar = (Button) findViewById(R.id.buttonReiniciar);
        listViewOpciones = (ListView) findViewById(R.id.listViewOpciones);
        listElementos = new ArrayList<>();
        listAdapterOpciones = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, listElementos);
        adapterSpinner = ArrayAdapter.createFromResource(this, R.array.valores_spinner, android.R.layout.simple_spinner_item);
        cuenta = 0;
        pedidoConfirmado = false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case -1: break;
            case R.id.radioButtonPlato:
                cambiarLista(listaPlatos);
                break;
            case R.id.radioButtonPostre:
                cambiarLista(listaPostre);
                break;
            case R.id.radioButtonBebida:
                cambiarLista(listaBebidas);
                break;
        }
    }

    private void cambiarLista(ElementoMenu[] lista){
        listElementos.clear();
        listElementos.addAll(Arrays.asList(lista));
        actualizarVistaLista();
    }

    private void actualizarVistaLista(){
        listViewOpciones.clearChoices();
        pedidoActual = null;
        listViewOpciones.setItemChecked(-1, true);
        listAdapterOpciones.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case -1:
                break;
            case R.id.listViewOpciones:
                pedidoActual = (ElementoMenu) listViewOpciones.getItemAtPosition(i);
                break;
        }
    }

    @Override
    public void onClick(View button) {
        switch (button.getId()){
            case -1:
                break;
            case R.id.buttonAgregar:
                agregarPedido();
                actualizarVistaLista();
                break;
            case R.id.buttonConfirmar:
                confirmarPedido();
                actualizarVistaLista();
                break;
            case R.id.buttonReiniciar:
                reiniciarPedido();
                actualizarVistaLista();
                break;
        }
    }

    private void agregarPedido(){
        if(pedidoConfirmado){
            Toast.makeText(this, getResources().getString(R.string.toast_pedido_ya_confirmado), Toast.LENGTH_SHORT).show();
        }
        else {
            if (pedidoActual != null) {
                String textoPedido = tvPedidos.getText() + (tvPedidos.getText().toString().equals("") ? "" : "\n") + pedidoActual;
                cuenta += pedidoActual.getPrecio();
                tvPedidos.setText(textoPedido);
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.toast_seleccion_vacia), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void  confirmarPedido(){
        if(pedidoConfirmado){
            Toast.makeText(this, getResources().getString(R.string.toast_pedido_ya_confirmado), Toast.LENGTH_SHORT).show();
        }
        else{
            if(cuenta == 0){
                Toast.makeText(this, getResources().getString(R.string.toast_pedido_vacio),Toast.LENGTH_SHORT).show();
            }
            else{
                String textoPedido = getResources().getString(R.string.total);
                textoPedido = String.format(Locale.getDefault(),textoPedido, cuenta);
                textoPedido = tvPedidos.getText() + "\n" + textoPedido;
                tvPedidos.setText(textoPedido);
                pedidoConfirmado = true;
            }
        }
    }

    private void reiniciarPedido(){
        tvPedidos.setText("");
        cuenta = 0;
        pedidoConfirmado = false;
    }

    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i,n,0.0);
            Random r = new Random();
            this.precio= (r.nextInt(3)+1)*((r.nextDouble()*100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre+ " ("+f.format(this.precio)+")";
        }
    }

    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[13]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");
    }
 }