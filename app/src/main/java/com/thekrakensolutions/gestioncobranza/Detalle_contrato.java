package com.thekrakensolutions.gestioncobranza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thekrakensolutions.gestioncobranza.adapters.PagosAdapter;
import com.thekrakensolutions.gestioncobranza.adapters.ProductosAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hectoraguilar on 07/03/17.
 */

public class Detalle_contrato extends AppCompatActivity {
    ListView lv;
    ListView lv_pagos;
    ListView lv_productos;

    private String _urlGet;
    private String _urlGetProductos;
    private String _urlGetPagos;
    private String _url;
    public static final String idu = "idu";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private int valueID = 0;

    String idPersona;

    public static ArrayList<String> listaNombreVeterinarios = new ArrayList<String>();
    public static ArrayList<String> listaImagenVeterinarios = new ArrayList<String>();
    public static ArrayList<String> listaIdVeterinario = new ArrayList<String>();

    public static ArrayList<String> listaCantidad = new ArrayList<String>();
    public static ArrayList<String> listaProducto = new ArrayList<String>();
    public static ArrayList<String> listaCostoUnitario = new ArrayList<String>();
    public static ArrayList<String> listaCostoTotal = new ArrayList<String>();
    public static ArrayList<String> listaIdProducto = new ArrayList<String>();

    public static ArrayList<String> listaCantidadPago = new ArrayList<String>();
    public static ArrayList<String> listaFechaPago = new ArrayList<String>();
    public static ArrayList<String> listaIdPago = new ArrayList<String>();


    public Detalle_contrato mActivity = this;
    public PagosAdapter _pagosAdapter;
    public ProductosAdapter _productosAdapter;



    public Detalle_contrato _activity = this;
    RecyclerView lvMascotas;

    private String _urlNotificaciones;

    String idString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contrato);

        //lv = (ListView) findViewById(R.id.list_pagos);

        //showMsg("test");

        //String idString;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idString= null;

        } else {
            //idString= extras.getString("idcliente");
            idString= extras.getString("idcontrato");
            //idPersona=extras.getString("idpersona");
            Log.d("id_vet", idString);

        }


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        valueID = sharedpreferences.getInt("idu", 0);

        lv_pagos = (ListView) findViewById(R.id.list_pagos_contrato);
        lv_productos = (ListView) findViewById(R.id.list_venta_contrato);
        /*
        lvMascotas = (RecyclerView) findViewById(R.id.lvVeterinarios);

        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + Integer.toString(valueID);
        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString;

        _urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        new Detalle_veterinario.RetrieveFeedTaskGet().execute();

        _urlNotificaciones = "http://hyperion.init-code.com/zungu/app/vt_get_numero_notificaciones.php?idv=" + valueID;
        new Detalle_veterinario.RetrieveFeedTaskNotificaciones().execute();
        */
        //_urlGet = "http://hyperion.init-code.com/zungu/app/vt_principal.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        //_urlGet = "http://thekrakensolutions.com/cobradores/android_get_cliente.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        //_urlGet = "http://thekrakensolutions.com/cobradores/android_get_contrato.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        //_urlGet = "http://thekrakensolutions.com/cobradores/android_get_contrato.php?id_persona=" + idPersona + "&idv=" + valueID + "&accion=true";
        _urlGet = "http://thekrakensolutions.com/cobradores/android_get_contrato.php?id_editar=" + idString + "&idv=" + valueID + "&accion=true";
        new Detalle_contrato.RetrieveFeedTaskGet().execute();


        _urlGetPagos = "http://thekrakensolutions.com/cobradores/android_get_pagos.php?id_editar=" + idString;
        Log.d("url_pagos", _urlGetPagos);
        new Detalle_contrato.RetrieveFeedTaskPago().execute();

        _urlGetProductos = "http://thekrakensolutions.com/cobradores/android_get_productos.php?id_editar=" + idString;
        Log.d("url_productos", _urlGetProductos);
        new Detalle_contrato.RetrieveFeedTaskProducto().execute();

        /**
        _url = "http://thekrakensolutions.com/cobradores/android_get_contratos.php?id=" + Integer.toString(valueID);
        Log.d("url_veterinarios", _url);
        new Detalle_contrato.RetrieveFeedTask().execute();
        */

    }
    private void showMsg(CharSequence text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    class RetrieveFeedTaskProducto extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlGetProductos);
                URL url = new URL(_urlGetProductos);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    listaCantidad.clear();
                    listaProducto.clear();
                    listaCostoUnitario.clear();
                    listaCostoTotal.clear();
                    listaIdProducto.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);

                        listaCantidad.add(jsonobject.getString("cantidad"));
                        listaProducto.add(jsonobject.getString("concepto"));
                        listaCostoUnitario.add(jsonobject.getString("costo_unitario"));
                        listaCostoTotal.add(jsonobject.getString("costo_concepto"));
                        listaIdProducto.add(jsonobject.getString("id_contrato_producto"));

                    }

/*
                    _mascotasAdapter = new DireccionesAdapter(valueID, mActivity, listaDirecciones, listaIdDirecciones);
                    lv.setAdapter(_mascotasAdapter);

                   */
                    /*
                    _pagosAdapter = new PagosAdapter(valueID, mActivity, listaCantidad, listaProducto, listaCostoUnitario, listaCostoTotal, listaIdPago);
                    lv_pagos.setAdapter(_pagosAdapter);
                    */
                    _productosAdapter = new ProductosAdapter(valueID, mActivity, listaCantidad, listaProducto, listaCostoUnitario, listaCostoTotal, listaIdProducto);
                    lv_productos.setAdapter(_productosAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }
    class RetrieveFeedTaskPago extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlGetPagos);
                URL url = new URL(_urlGetPagos);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    listaCantidadPago.clear();
                    listaFechaPago.clear();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);

                        listaCantidadPago.add(jsonobject.getString("cantidad_pago"));

                        //Log.d("valor", jsonobject.getString("calle"));
                        listaFechaPago.add(jsonobject.getString("fecha_pago"));
                        //listaIdVeterinario.add(jsonobject.getString("total"));

                    }

/*
                    _mascotasAdapter = new DireccionesAdapter(valueID, mActivity, listaDirecciones, listaIdDirecciones);
                    lv.setAdapter(_mascotasAdapter);

                   */
                    _pagosAdapter = new PagosAdapter(valueID, mActivity, listaCantidadPago, listaFechaPago, listaIdPago);
                    lv_pagos.setAdapter(_pagosAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }
    class RetrieveFeedTaskGet extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }



        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url Contrato: ", _urlGet);
                URL url = new URL(_urlGet);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }


        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                /*
                TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
                TextView lblDireccion = (TextView) findViewById(R.id.lblDireccion);
                ImageView foto = (ImageView) findViewById(R.id.imgFoto);
                */

                TextView lblNombreVo = (TextView) findViewById(R.id.txtNombreA);
                TextView lblEmailVo = (TextView) findViewById(R.id.txtEmailA);
                TextView lblCelVo = (TextView) findViewById(R.id.txtCelA);
                //TextView lblCedVo = (TextView) findViewById(R.id.txtCedA);
                TextView txtDireccion = (TextView) findViewById(R.id.txtDireccion);







                TextView txtTotalContrato = (TextView) findViewById(R.id.txtTotalContrato);
                TextView txtTotalRestante = (TextView) findViewById(R.id.txtTotalRestante);
                TextView txtTotalAbonado = (TextView) findViewById(R.id.txtTotalAbonado);
                TextView txtCantidadPagos = (TextView) findViewById(R.id.txtCantidadPagos);
                TextView txtPagoRequerido = (TextView) findViewById(R.id.txtPagoRequerido);

                TextView txtPeriodoPago = (TextView) findViewById(R.id.txtPeriodoPago);
                TextView txtDiaPago = (TextView) findViewById(R.id.txtDiaPago);
                TextView txtFechaEstimadaFin = (TextView) findViewById(R.id.txtFechaEstimadaFin);
                TextView txtUltimoPago = (TextView) findViewById(R.id.txtUltimoPago);


                final ImageView fotoVeterinario = (ImageView) findViewById(R.id.imgVeterinario);

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();

                    idPersona = object.getString("id_persona");

                    String _nombre_vo = object.getString("numero_cliente") + " - " + object.getString("nombre") + " " + object.getString("apaterno") + " " + object.getString("amaterno");

                    //String _telefono_vo = object.getString("telefono_casa");
                    String _cedula_vo = object.getString("numero_cliente");
                    String _email_vo = object.getString("fecha_nacimiento");
                    //String _imagen_vo = object.getString("sexo");
                    String _imagen_vo = object.getString("imagen");

                    String totalContrato = object.getString("total");
                    String totalRestante = object.getString("cantidad_restante");
                    String totalAbonado = object.getString("cantidad_pagada");
                    String cantidadPagos = object.getString("cantidad_de_pagos") + " de " + object.getString("cantidad_pagos");
                    String pagoRequerido = object.getString("cantidad_abono");

                    String diaPago = "";

                    String periodoPago = object.getString("frecuencia_pago");
                    if(periodoPago.equals("SEMANAL")){
                        if(object.getString("valor_semanal").equals("1")){
                            diaPago = "Lunes";
                        }
                        if(object.getString("valor_semanal").equals("2")){
                            diaPago = "Martes";
                        }
                        if(object.getString("valor_semanal").equals("3")){
                            diaPago = "Miércoles";
                        }
                        if(object.getString("valor_semanal").equals("4")){
                            diaPago = "Jueves";
                        }
                        if(object.getString("valor_semanal").equals("5")){
                            diaPago = "Viernes";
                        }
                        if(object.getString("valor_semanal").equals("6")){
                            diaPago = "Sábado";
                        }
                        if(object.getString("valor_semanal").equals("7")){
                            diaPago = "Domingo";
                        }
                    }
                    if(periodoPago.equals("QUINCENAL")){
                        diaPago = object.getString("valor_quincenal_1") + " y " + object.getString("valor_quincenal_2");
                    }
                    if(periodoPago.equals("MENSUAL")){
                        diaPago = object.getString("valor_mensual_1");
                    }

                    String fechaEstimadaFin = object.getString("fecha_estimada_fin");
                    String ultimoPago = object.getString("cantidad_de_pagos") + " de " + object.getString("cantidad_pagos");


                    txtTotalContrato.setText(totalContrato);
                    txtTotalRestante.setText(totalRestante);
                    txtTotalAbonado.setText(totalAbonado);
                    txtCantidadPagos.setText(cantidadPagos);
                    txtPagoRequerido.setText(pagoRequerido);

                    txtPeriodoPago.setText(periodoPago);
                    txtDiaPago.setText(diaPago);
                    txtFechaEstimadaFin.setText(fechaEstimadaFin);
                    txtUltimoPago.setText(ultimoPago);







                    //showMsg("tesst2");

                    //showMsg(_email_vo);
                    //showMsg(_telefono_vo);


                    /*
                    {"id_cliente":"1","cliente":"","numero_cliente":"0","fecha_nacimiento":"0000-00-00","sexo":"mkl","imagen":"",":"klmkl","":"mkl","":"mklm","":"klm","":"klmkl","telefono_casa":"","telefono_celular":"","telefono_oficina":"","":"","":"","ocupacion":"","direccion_trabajo":"","nombre_pareja":"","ocupacion_pareja":"","telefono_pareja":"","complexion":"","estatura":"","tez":"","edad_rango":"","cabello":"","color_cabello":"","tipo_identificacion":"","numero_identificacion":"","nombre_referencia_1":"","direccion_referencia_1":"","telefono_referencia_1":"","parentesco_referencia_1":"","anios_conocerce_referencia_1":"","nombre_referencia_2":"","direccion_referencia_2":"","telefono_referencia_2":"","parentesco_referencia_2":"","anios_conocerce_referencia_2":"","maps_localizacion":"","imagen_plano_localizacion":"","fachada_casa":"","a_lado_casa":"","enfrente_casa":"","autorizacion_contratos":"","id_creador":"0","id_empresa":"0"}

                    {"id_cliente":"1","cliente":"","numero_cliente":"0","nombre":"mklmklmklm","apaterno":"klmkl","amaterno":"mklm","fecha_nacimiento":"0000-00-00","sexo":"mkl","imagen":"","calle":"mklm","numero_exterior":"klm","numero_interior":"klmkl","colonia":"mkl","delegacion_municipio":"mkl","estado":"mklm","codigo_postal":"klm","pais":"klmkl","telefono_casa":"","telefono_celular":"","telefono_oficina":"","entre_calle":"","y_calle":"","ocupacion":"","direccion_trabajo":"","nombre_pareja":"","ocupacion_pareja":"","telefono_pareja":"","complexion":"","estatura":"","tez":"","edad_rango":"","cabello":"","color_cabello":"","tipo_identificacion":"","numero_identificacion":"","nombre_referencia_1":"","direccion_referencia_1":"","telefono_referencia_1":"","parentesco_referencia_1":"","anios_conocerce_referencia_1":"","nombre_referencia_2":"","direccion_referencia_2":"","telefono_referencia_2":"","parentesco_referencia_2":"","anios_conocerce_referencia_2":"","maps_localizacion":"","imagen_plano_localizacion":"","fachada_casa":"","a_lado_casa":"","enfrente_casa":"","autorizacion_contratos":"","id_creador":"0","id_empresa":"0"}

                    String _telefono_vo = object.getString("telefono_veterinario");
                    String _cedula_vo = object.getString("cedula_veterinario");
                    String _email_vo = object.getString("email_veterinario");
                    String _imagen_vo = object.getString("imagen_veterinario");
                    */




                    if(_nombre_vo.length() > 3)
                        lblNombreVo.setText(_nombre_vo);

                    if(_email_vo.length() > 3)
                        lblEmailVo.setText(_email_vo);

                    /*
                    if(_telefono_vo.length() > 3)
                        lblCelVo.setText(_telefono_vo);

                    */
                    /*
                    if(_cedula_vo.length() > 3)
                        lblCedVo.setText(_cedula_vo);
                        */


                    //DIRECCION
                    //String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("delegacion_municipio")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais")  + " , entre calle " + object.getString("entre_calle")  + " y calle " + object.getString("y_calle")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno")  + " " + object.getString("amaterno");
                    //String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("delegacion_municipio")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais")  + " , entre calle " + object.getString("entre_calle")  + " y calle " + object.getString("y_calle");
                    String txtDireccion_ = object.getString("calle") + " " + object.getString("numero_exterior") + " " + object.getString("numero_interior")  + " , Colonia " + object.getString("colonia")  + " , Delegación/Municipio " + object.getString("poblacion")  + " , Estado " + object.getString("estado")  + " , C.P. " + object.getString("codigo_postal")  + " , País " + object.getString("pais");

                    if(txtDireccion_.length() > 3)
                        txtDireccion.setText(txtDireccion_);


                    Log.d("INFO", _nombre_vo);


                    if(_imagen_vo.length() > 3){
                        String _urlFoto = "http://thekrakensolutions.com/administrativos/images/clientes/" + _imagen_vo;
                        //Picasso.with(fotoVeterinario.getContext()).load(_urlFoto).fit().centerCrop().into(fotoVeterinario);

                        Picasso.with(fotoVeterinario.getContext()).load(_urlFoto)
                                .into(fotoVeterinario, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imageBitmap = ((BitmapDrawable) fotoVeterinario.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(fotoVeterinario.getContext().getResources(), imageBitmap);
                                        circularBitmapDrawable.setCircular(true);
                                        fotoVeterinario.setImageDrawable(circularBitmapDrawable);
                                    }
                                    @Override
                                    public void onError() {

                                    }
                                });
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.i("INFO", response);
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _url);
                URL url = new URL(_url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    listaNombreVeterinarios.clear();
                    listaImagenVeterinarios.clear();
                    listaIdVeterinario.clear();


                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);

                        /*
                        listaNombreVeterinarios.add(jsonobject.getString("nombre"));
                        listaImagenVeterinarios.add(jsonobject.getString("foto"));
                        listaIdVeterinario.add(jsonobject.getString("id_veterinario"));
                        */
                        //listaImagenVeterinarios.add(jsonobject.getString("foto"));

                        /*
                        listaNombreVeterinarios.add(jsonobject.getString("numero_cliente") + " " + jsonobject.getString("nombre") + " " + jsonobject.getString("apaterno"));

                        listaImagenVeterinarios.add(jsonobject.getString("imagen"));
                        listaIdVeterinario.add(jsonobject.getString("id_cliente"));
                        */
                        listaNombreVeterinarios.add(jsonobject.getString("numero_contrato"));

                        listaImagenVeterinarios.add(jsonobject.getString("cantidad"));
                        //listaIdVeterinario.add(jsonobject.getString("total"));
                        listaIdVeterinario.add(jsonobject.getString("id_contrato"));

                    }

                    /*
                    _mascotasAdapter = new PagosAdapter(valueID, mActivity, listaNombreVeterinarios, listaImagenVeterinarios, listaIdVeterinario);
                    lv.setAdapter(_mascotasAdapter);
                    */

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }

    /*
    public void editarEstablecimiento(View view) {
        Intent i = new Intent(Detalle_veterinario.this, Editar_establecimiento.class);
        startActivity(i);
    }
    */

    public void goBack(View v){
        //Intent i = new Intent(Detalle_contrato.this, Lista_clientes.class);
        Intent i = new Intent(Detalle_contrato.this, Lista_contratos.class);
        startActivity(i);
    }
    public void goAgregarPago(View v){
        Intent i = new Intent(Detalle_contrato.this, Agregar_pago.class);
        //i.putExtra("idcliente", _listaIdVeterinarios.get(i));
        //i.putExtra("idcliente", idString);
        i.putExtra("idcontrato", idString);
        startActivity(i);
    }
    public void goDetalleCliente(View v){
        Intent i = new Intent(Detalle_contrato.this, Detalle_cliente.class);
        //i.putExtra("idcliente", _listaIdVeterinarios.get(i));
        i.putExtra("idcliente", idString);
        i.putExtra("idpersona", idPersona);
        startActivity(i);
    }
    public void goMenu(View v){
        Intent i = new Intent(Detalle_contrato.this, Menu.class);
        startActivity(i);
    }

    /*

    public void goMenu(View v){
        Intent i = new Intent(Detalle_veterinario.this, Menu.class);
        startActivity(i);
    }
    public void goBack(View v){
        Intent i = new Intent(Detalle_veterinario.this, Principal.class);
        startActivity(i);
    }
    public void goNotificaciones(View v){
        Intent i = new Intent(Detalle_veterinario.this, Notificaciones.class);
        startActivity(i);
    }

    public void goAgregar(View v){
        Intent i = new Intent(Detalle_veterinario.this, Agregar_veterinarios.class);
        startActivity(i);
    }

    public void goClientes(View v){
        Intent i = new Intent(Detalle_veterinario.this, Clientes.class);
        startActivity(i);
    }

    public void goMascotas(View v){
        Intent i = new Intent(Detalle_veterinario.this, Mascotas.class);
        startActivity(i);
    }

    public void goServicio(View v){
        Intent i = new Intent(Detalle_veterinario.this, Servicio.class);
        startActivity(i);
    }

    public void goTienda(View v){
        Intent i = new Intent(Detalle_veterinario.this, Tienda.class);
        startActivity(i);
    }

    public void goEditarPerfil(View v){
        Intent i = new Intent(Detalle_veterinario.this, Editar_perfil.class);
        startActivity(i);
    }



    class RetrieveFeedTaskGet extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlGet);
                URL url = new URL(_urlGet);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
                TextView lblDireccion = (TextView) findViewById(R.id.lblDireccion);
                ImageView foto = (ImageView) findViewById(R.id.imgFoto);

                TextView lblNombreVo = (TextView) findViewById(R.id.txtNombreA);
                TextView lblEmailVo = (TextView) findViewById(R.id.txtEmailA);
                TextView lblCelVo = (TextView) findViewById(R.id.txtCelA);
                TextView lblCedVo = (TextView) findViewById(R.id.txtCedA);
                final ImageView fotoVeterinario = (ImageView) findViewById(R.id.imgVeterinario);

                try {

                    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();


                    String _nombre_vo = object.getString("nombre_veterinario");
                    String _telefono_vo = object.getString("telefono_veterinario");
                    String _cedula_vo = object.getString("cedula_veterinario");
                    String _email_vo = object.getString("email_veterinario");
                    String _imagen_vo = object.getString("imagen_veterinario");

                    if(_nombre_vo.length() > 3)
                        lblNombreVo.setText(_nombre_vo);

                    if(_email_vo.length() > 3)
                        lblEmailVo.setText(_email_vo);

                    if(_telefono_vo.length() > 3)
                        lblCelVo.setText(_telefono_vo);

                    if(_cedula_vo.length() > 3)
                        lblCedVo.setText(_cedula_vo);

                    if(_imagen_vo.length() > 3){
                        String _urlFoto = "http://hyperion.init-code.com/zungu/imagen_establecimiento/" + _imagen_vo;
                        //Picasso.with(fotoVeterinario.getContext()).load(_urlFoto).fit().centerCrop().into(fotoVeterinario);

                        Picasso.with(fotoVeterinario.getContext()).load(_urlFoto)
                                .into(fotoVeterinario, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imageBitmap = ((BitmapDrawable) fotoVeterinario.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(fotoVeterinario.getContext().getResources(), imageBitmap);
                                        circularBitmapDrawable.setCircular(true);
                                        fotoVeterinario.setImageDrawable(circularBitmapDrawable);
                                    }
                                    @Override
                                    public void onError() {

                                    }
                                });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Log.i("INFO", response);
        }
    }

    class RetrieveFeedTaskNotificaciones extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                Log.i("INFO url: ", _urlNotificaciones);
                URL url = new URL(_urlNotificaciones);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                try {
                    JSONTokener tokener = new JSONTokener(response);
                    JSONArray arr = new JSONArray(tokener);

                    TextView numeroNotificaciones = (TextView) findViewById(R.id.numeroNotificaciones);


                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        //Log.d("nombre",jsonobject.getString("nombre"));

                        //"":"45","":"9","":"0","fecha":"2016-11-19 15:59:00","":"","acepto":"0","":"","":"1","id_pago":"0","id_veterinaria":"0"},

                        if(jsonobject.getString("numero_notificaciones").equals("")){
                            numeroNotificaciones.setVisibility(View.GONE);
                        }else{
                            numeroNotificaciones.setVisibility(View.VISIBLE);
                            numeroNotificaciones.setText(jsonobject.getString("numero_notificaciones"));
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("INFO", response);
        }
    }
    */
}
