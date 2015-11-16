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

public class ActivityDeslocamento extends ActionBarActivity implements SensorEventListener {


	//private long last_timeAcel=0;		
	//private long current_timeAcel=0;
	
	private double current_timeAcel=0;
	private double tns;
	
	SensorManager sm;
	Sensor acelerometro;
	
	TextView Posx = null;
	TextView Posy = null;
	TextView Posz = null;
	
	TextView Estado=null;
	
	boolean gravaDesloc = false;
	
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
	
	
	double Sx = 0;
	double Sy = 0;
	double Sz = 0;
	double Vx = 0;
	double Vy = 0;
	double Vz = 0;
	
	ArrayList<Double> DeslocSalvo;
	
	DecimalFormat decimal = new DecimalFormat( "0.###" );


	PdsKalman1D fx = new PdsKalman1D (Ax,Hx,Qx,Rx);
	PdsKalman1D fy = new PdsKalman1D (Ay,Hy,Qy,Ry);
	PdsKalman1D fz = new PdsKalman1D (Az,Hz,Qz,Rz);
	
	Integral Int1=new Integral();
	Integral Int2=new Integral();
	Integral Int3=new Integral();
	Integral Int4=new Integral();
	Integral Int5=new Integral();
	Integral Int6=new Integral();
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deslocamento);
	
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // Acessando os sensores
		acelerometro = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);// Acessando o acelerometro 

		Estado = (TextView) findViewById(R.id.estado);
		
		Posx = (TextView) findViewById(R.id.textViewPosx);
		Posy = (TextView) findViewById(R.id.textViewPosy);
		Posz = (TextView) findViewById(R.id.textViewPosz);
	
		
	Intent intent = getIntent();
		
		Hx = intent.getDoubleExtra("HX4", 1.0);
		Hy = intent.getDoubleExtra("HY4", 1.0);
		Hz = intent.getDoubleExtra("HZ4", 1.0);
		Rx = intent.getDoubleExtra("RX4", 2.0);
		Ry = intent.getDoubleExtra("RY4", 2.0);
		Rz = intent.getDoubleExtra("RZ4", 2.0);
		Ax = intent.getDoubleExtra("AX4", 0.5);
		Ay = intent.getDoubleExtra("AY4", 0.5);
		Az = intent.getDoubleExtra("AZ4", 0.5);
		Qx = intent.getDoubleExtra("QX4", 1.0);
		Qy = intent.getDoubleExtra("QY4", 1.0);
		Qz = intent.getDoubleExtra("QZ4", 1.0);
	
		DeslocSalvo = new ArrayList<Double>();	
		
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
		sm.unregisterListener(this); // Irá parar o processo de captura do sensor
	}                                // Estes métodos(onResume/onPause)fazem poupar bateria, pois sem eles o aplicativo vai continuar  
	// captando informações mesmo que o usuário não esteja interagindo

	
	private void salvarDeslocamentos() {
    	String filename = "deslocamento.txt";

    	FileOutputStream outputStream;
    	String entrada = new String();
    	int ii=0;
    	for (Double d : DeslocSalvo) {
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
	       
	   	Estado.setText("Gravando Desloc");
    	gravaDesloc = true;
		
	    }
	
	public void onClickSalvar(View v){
	       
    	salvarDeslocamentos();
    	gravaDesloc = false;
    	Estado.setText("Salvo");
		
	    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_deslocamento, menu);
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
		
		Sx = Int4   .EvaluateValue(Vx,tns);
		Sy = Int5   .EvaluateValue(Vy,tns);
		Sz = Int6   .EvaluateValue(Vz,tns);
		
		String SX = decimal.format(Sx);
		String SY = decimal.format(Sy);
		String SZ = decimal.format(Sz);
		
		
		Posx.setText("x: "+(SX));
		Posy.setText("y: "+(SY));
		Posz.setText("z: "+(SZ));
		
		
		//--------------------------------------------------------------------- INTEGRAIS SIMPLES
		
			/*	double VxBk=0;
				double VyBk=0;
				double VzBk=0;
				double SxBk=0;
				double SyBk=0;
				double SzBk=0;
								
				VxBk = Vx;
				VyBk = Vy;
				VzBk = Vz;
				SxBk = Sx;
				SyBk = Sy;
				SzBk = Sz;
				
				//----------------------------------------- VELOCIDADE
				
				
				this.current_timeAcel=event.timestamp;	
				if(this.last_timeAcel==0)	this.last_timeAcel=this.current_timeAcel; 
				
				Vx = VxBk + hatax*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				Vy = VyBk + hatay*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				Vz = VzBk + hataz*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				
				//----------------------------------------- DESLOCAMENTO

				Sx = SxBk + Vx*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				Sy = SyBk + Vy*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				Sz = SzBk + Vz*(this.current_timeAcel-this.last_timeAcel)/1000000000.0;
				
				String SX = decimal.format(Sx);
				String SY = decimal.format(Sy);
				String SZ = decimal.format(Sz);
				
				
				Posx.setText("x: "+(SX));
				Posy.setText("y: "+(SY));
				Posz.setText("z: "+(SZ));
				
							
				this.last_timeAcel=this.current_timeAcel;
				*/
				
				 if (gravaDesloc) {
						DeslocSalvo.add(Sx);
						DeslocSalvo.add(Sy);
						DeslocSalvo.add(Sz);
					}
				
			}

		



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
