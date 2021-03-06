package com.example.acelerometro_m_testeabas;



import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import net.trucomanx.pdsplibj.pdsdf.PdsKalman1D;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ActivityVelocidade extends ActionBarActivity implements SensorEventListener {

	//private long last_timeAcel=0;		
	//private long current_timeAcel=0;
	
	private double current_timeAcel=0;
	private double tns;
	
	SensorManager sm;
	Sensor acelerometro;
	
	
	TextView Velx = null;
	TextView Vely = null;
	TextView Velz = null;
	
	TextView Estado=null;
	
	boolean gravaVel = false;

	Double Ax=0.5;
	Double Ay=0.5;
	Double Az=0.5;
	
	Double Hx=1.0;
	Double Hy=1.0;
	Double Hz=1.0;
	
	Double Qx=1.0;
	Double Qy=1.0;
	Double Qz=1.0;
	
	Double Rx=2.0;
	Double Ry=2.0;
	Double Rz=2.0;
	
	double Vx = 0;
	double Vy = 0;
	double Vz = 0;
	
	ArrayList<Double> VelSalvas;
	
	DecimalFormat decimal = new DecimalFormat( "0.###" );
	

	PdsKalman1D fx = new PdsKalman1D (Ax,Hx,Qx,Rx);
	PdsKalman1D fy = new PdsKalman1D (Ay,Hy,Qy,Ry);
	PdsKalman1D fz = new PdsKalman1D (Az,Hz,Qz,Rz);
	
	Integral Int1=new Integral();
	Integral Int2=new Integral();
	Integral Int3=new Integral();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_velocidade);
		
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // Acessando os sensores
		acelerometro = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);// Acessando o acelerometro
		
		Estado = (TextView) findViewById(R.id.estado);
			
		Velx = (TextView) findViewById(R.id.textViewPosx);
		Vely = (TextView) findViewById(R.id.textViewPosy);
		Velz = (TextView) findViewById(R.id.textViewPosz);
		
		
	    Intent intent = getIntent();
		
		Hx = intent.getDoubleExtra("HX3", 1.0);
		Hy = intent.getDoubleExtra("HY3", 1.0);
		Hz = intent.getDoubleExtra("HZ3", 1.0);
		Rx = intent.getDoubleExtra("RX3", 2.0);
		Ry = intent.getDoubleExtra("RY3", 2.0);
		Rz = intent.getDoubleExtra("RZ3", 2.0);
		Ax = intent.getDoubleExtra("AX3", 0.5);
		Ay = intent.getDoubleExtra("AY3", 0.5);
		Az = intent.getDoubleExtra("AZ3", 0.5);
		Qx = intent.getDoubleExtra("QX3", 1.0);
		Qy = intent.getDoubleExtra("QY3", 1.0);
		Qz = intent.getDoubleExtra("QZ3", 1.0);
		
		
		VelSalvas = new ArrayList<Double>();
		
	}
	
	
	public void startActivityVoltar(View view) {
		 
	    Intent Menu = new Intent(this, TesteAbasActivity.class);
	    
	    Menu.putExtra("HX", Hx);
	    Menu.putExtra("HY", Hy);
	    Menu.putExtra("HZ", Hz);
	    Menu.putExtra("RX", Rx);
	    Menu.putExtra("RY", Ry);
	    Menu.putExtra("RZ", Rz);
	    Menu.putExtra("AX", Ax);
	    Menu.putExtra("AY", Ay);
	    Menu.putExtra("AZ", Az);
	    Menu.putExtra("QX", Qx);
	    Menu.putExtra("QY", Qy);
	    Menu.putExtra("QZ", Qz);
	    
	    startActivity(Menu);
	}
	

	
	@Override
	protected void onResume() {
		super.onResume();
		sm.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL); // Inicia o processo de captura do acelerometro

	}

	@Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this); // Ir� parar o processo de captura do sensor
	}                                // Estes m�todos(onResume/onPause)fazem poupar bateria, pois sem eles o aplicativo vai continuar  
	// captando informa��es mesmo que o usu�rio n�o esteja interagindo

	
	private void salvarVelocidades() {
    	String filename = "velocidades.txt";

    	FileOutputStream outputStream;
    	String entrada = new String();
    	int ii=0;
    	for (Double d : VelSalvas) {
    		if(ii==3) ii=0;
    		entrada = entrada.concat(d.toString());
    		if(ii!=2) entrada = entrada.concat("\t");
    		else     entrada = entrada.concat("\n");
    		ii++;
    	}
    	
    	File file = new File("/storage/emulated/0", filename);

    	try {
    	  outputStream = new FileOutputStream(file);
    	  outputStream.write(entrada.getBytes());
    	  outputStream.close();  
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    }

	public void onClickGravar(View v){
	       
	   	Estado.setText("Gravando Vel");
    	gravaVel = true;
		
	    }
	
	public void onClickSalvar(View v){
	       
    	salvarVelocidades();
    	gravaVel = false;
    	Estado.setText("Salvo");
		
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_velocidade, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onSensorChanged(SensorEvent event) {

		double ax = (event.values[0]);
		double ay = (event.values[1]);
		double az = (event.values[2]);
		
		double hatax = fx.EvaluateValue(ax);
		double hatay = fy.EvaluateValue(ay);
		double hataz = fz.EvaluateValue(az);
		
		if(current_timeAcel==0) this.current_timeAcel=event.timestamp; //The time in nanosecond at which the event happened	    
		
		tns = (event.timestamp-current_timeAcel)/1000000000.0;
		
		
		Vx = Int1   .EvaluateValue(hatax,tns);
		Vy = Int2   .EvaluateValue(hatay,tns);
		Vz = Int3   .EvaluateValue(hataz,tns);
		
		 String VX = decimal.format(Vx);
		 String VY = decimal.format(Vy);
		 String VZ = decimal.format(Vz);
		 
		 Velx.setText("x:"+(VX));
		 Vely.setText("y:"+(VY));
		 Velz.setText("z:"+(VZ));
		
	/*	double VxBk=0;
		double VyBk=0;
		double VzBk=0;
					
		VxBk = Vx;
		VyBk = Vy;
		VzBk = Vz;
		
		//----------------------------------------- VELOCIDADE
		
		
		this.current_timeAcel=event.timestamp;	
		if(this.last_timeAcel==0)	this.last_timeAcel=this.current_timeAcel; 
		
		Vx = VxBk + hatax*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		Vy = VyBk + hatay*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		Vz = VzBk + hataz*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
		
		
		 String VX = decimal.format(Vx);
		 String VY = decimal.format(Vy);
		 String VZ = decimal.format(Vz);
		
		
		Velx.setText("x:"+(VX));
		Vely.setText("y:"+(VY));
		Velz.setText("z:"+(VZ));
		
		this.last_timeAcel=this.current_timeAcel;
		*/
		
		 if (gravaVel) {
				VelSalvas.add(Vx);
				VelSalvas.add(Vy);
				VelSalvas.add(Vz);
			}
		
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
