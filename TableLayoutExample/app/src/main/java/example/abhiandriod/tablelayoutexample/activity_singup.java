package example.abhiandriod.tablelayoutexample;

import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import example.abhiandriod.tablelayoutexample.Model.Formulario;
import example.abhiandriod.tablelayoutexample.Model.Model;

public class activity_singup extends AppCompatActivity implements View.OnClickListener {

    public EditText fecha;
    public ImageButton imgFecha;
    private Model model;
    public Button guardar;


    private static final String CERO = "0";
    private static final String BARRA = "/";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);



    private EditText nombre;
    private EditText apellido;
    private EditText direccion;
    private EditText segundaDireccion;
    private EditText ciudad;
    private EditText provincia;
    private EditText zip;
    private Spinner pais;
    private EditText correo;
    private EditText codigoArea;
    private EditText telefono;
    private Spinner puesto;
    private Boolean isEditable;
    private Formulario viewForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            this.model = (Model) bundle.getSerializable("model");
            this.isEditable = (Boolean) bundle.getBoolean("editable");
            if (!this.isEditable){
                this.viewForm = (Formulario) bundle.getSerializable("formulario");
            }
        }

        if (this.model == null)
            this.model = new Model();

        nombre = (EditText)findViewById(R.id.IdNombre);
        apellido  = (EditText) findViewById(R.id.idApellido);
        direccion = (EditText) findViewById(R.id.idDireccion);
        segundaDireccion = (EditText) findViewById(R.id.idSegundaDireccion);
        ciudad = (EditText) findViewById(R.id.idCiudad);
        provincia = (EditText) findViewById(R.id.idProvincia);
        zip = (EditText) findViewById(R.id.idZip);
        pais = (Spinner) findViewById(R.id.idPais);
        correo = (EditText) findViewById(R.id.idCorreo);
        codigoArea =(EditText) findViewById(R.id.idCodigoArea);
        telefono = (EditText) findViewById(R.id.idTelefono);
        puesto = (Spinner) findViewById(R.id.idPosicion);

        fecha = (EditText) findViewById(R.id.idFecha);
        imgFecha = (ImageButton) findViewById(R.id.idObtenerFecha);
        imgFecha.setOnClickListener(this);
        guardar = (Button) findViewById(R.id.guardar);

        if(this.isEditable){
            guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int Vnumero =0;
                    Random random = new Random();
                    String Vnombre = nombre.getText().toString();
                    String Vapellido = apellido.getText().toString();
                    String Vdireccion = direccion.getText().toString();
                    String VsegundaDireccin = segundaDireccion.getText().toString();
                    String Vprovincia = provincia.getText().toString();
                    String Vciudad = ciudad.getText().toString();
                    String Vzip = zip.getText().toString();
                    String Vpais = pais.getSelectedItem().toString();
                    String VcodigoArea = codigoArea.getText().toString();
                    Vnumero = Integer.parseInt(telefono.getText().toString()) ;
                    String Vfecha = fecha.getText().toString();
                    String VformID = String.valueOf(model.getListaFormularios().size() + 1);
                    String Vpuesto = puesto.getSelectedItem().toString();
                    String Vcorreo = correo.getText().toString();



                    switch (validateForm(Vnombre,Vapellido,Vdireccion,VsegundaDireccin,Vprovincia,Vciudad,Vzip,
                            Vpais,VcodigoArea,Vnumero,Vfecha,VformID,Vpuesto,Vcorreo)){
                        case 0:
                            Formulario form = new Formulario(Vnombre,Vapellido,Vdireccion,VsegundaDireccin,Vprovincia,Vciudad,Vzip,
                                    Vpais,VcodigoArea,Vnumero,Vfecha,VformID,Vpuesto,Vcorreo);
                            model.addForm(form);
                            model.setLoggedUser(null);
                            Intent intent = new Intent(activity_singup.this, MainActivity.class);
                            intent.putExtra("model", model);
                            activity_singup.this.startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(activity_singup.this, "Complete todos los campos del formulario.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });
        }else{
            guardar.setVisibility(Button.INVISIBLE);
            this.nombre.setText(viewForm.getNombre());
            this.apellido.setText(viewForm.getApellido());
            this.direccion.setText(viewForm.getDireccion());
            this.segundaDireccion.setText(viewForm.getSegundaDireccin());
            this.provincia.setText(viewForm.getProvincia());
            this.ciudad.setText(viewForm.getCiudad());
            this.zip.setText(viewForm.getZip());

            ArrayList<String> paises = new ArrayList<>();
            paises.add(viewForm.getPais());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, paises);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.pais.setAdapter(adapter);

            this.correo.setText(viewForm.getCorreo());
            this.codigoArea.setText(viewForm.getCodigoArea());
            this.telefono.setText(String.valueOf(viewForm.getNumero()));
            this.fecha.setText(viewForm.getFecha());


            ArrayList<String> puestos = new ArrayList<>();
            puestos.add(viewForm.getPuesto());
            adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,puestos);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            this.puesto.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.idObtenerFecha:
                obtenerFecha();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                fecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }


    public int validateForm(String nombre, String apellido, String direccion, String segundaDireccin,
                            String provincia, String ciudad, String zip, String pais, String codigoArea,
                            int numero, String fecha, String id, String puesto, String correo){
        if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty()||segundaDireccin.isEmpty()||
        provincia.isEmpty()||ciudad.isEmpty()||zip.isEmpty()||pais.isEmpty()||codigoArea.isEmpty()||
        numero==0||fecha.isEmpty()||id.isEmpty()||puesto.isEmpty()||correo.isEmpty()){
            return 1;
        }
        return 0;
    }
}
