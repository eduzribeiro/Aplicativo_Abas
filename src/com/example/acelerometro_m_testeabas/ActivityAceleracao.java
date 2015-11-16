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
import net.trucomanx.pdsplibj.pdsdf.*;

public class ActivityAceleracao extends ActionBarActivity implements SensorEventListener {

	
	SensorManager sm;
	Sensor acelerometro;
	
	TextView accX=null;
	TextView accY=null;
	TextView accZ=null;
	
	TextView accHatX=null;
	TextView accHatY=null;
	TextView accHatZ=null;
	
	TextView X=null;
	TextView Y=null;
	TextView Z=null;
	
	TextView Estado=null;
	
	boolean gravaAcc = false;
	
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
	
	ArrayList<Double> AccSalvas;
	
	DecimalFormat decimal = new DecimalFormat( "0.###" );
	
	
	
	PdsKalman1D fx = new PdsKalman1D (Ax,Hx,Qx,Rx);
	PdsKalman1D fy = new PdsKalman1D (Ay,Hy,Qy,Ry);
	PdsKalman1D fz = new PdsKalman1D (Az,Hz,Qz,Rz);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aceleracao);
		
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // Acessando os sensores
		acelerometro = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);// Acessando o acelerometro
		
		Estado = (TextView) findViewById(R.id.textView9);
		
		accX = (TextView) findViewById(R.id.textViewAccX);
		accY = (TextView) findViewById(R.id.textViewAccY);
		accZ = (TextView) findViewById(R.id.textViewAccZ);
		
		accHatX = (TextView) findViewById(R.id.textView6);
		accHatY = (TextView) findViewById(R.id.textView7);
		accHatZ = (TextView) findViewById(R.id.textView8);
		
		
		X = (TextView) findViewById(R.id.textView2);
		Y = (TextView) findViewById(R.id.textView3);
		Z = (TextView) findViewById(R.id.textView4);
	
		Intent intent = getIntent();
		
		Hx = intent.getDoubleExtra("HX2", 1.0);
		Hy = intent.getDoubleExtra("HY2", 1.0);
		Hz = intent.getDoubleExtra("HZ2", 1.0);
		Rx = intent.getDoubleExtra("RX2", 2.0);
		Ry = intent.getDoubleExtra("RY2", 2.0);
		Rz = intent.getDoubleExtra("RZ2", 2.0);
		Ax = intent.getDoubleExtra("AX2", 0.5);
		Ay = intent.getDoubleExtra("AY2", 0.5);
		Az = intent.getDoubleExtra("AZ2", 0.5);
		Qx = intent.getDoubleExtra("QX2", 1.0);
		Qy = intent.getDoubleExtra("QY2", 1.0);
		Qz = intent.getDoubleExtra("QZ2", 1.0);
		
		 String hx = decimal.format(Hx);
		 String hy = decimal.format(Hy);
		 String hz = decimal.format(Hz);
		 
		 String rx = decimal.format(Rx);
		 String ry = decimal.format(Ry);
		 String rz = decimal.format(Rz);
		 
		 String ax = decimal.format(Ax);
		 String ay = decimal.format(Ay);
		 String az = decimal.format(Az);
		 
		 String qx = decimal.format(Qx);
		 String qy = decimal.format(Qy);
		 String qz = decimal.format(Qz);	
		
		 
		try {
		    X.setText("Hx: "+ hx + "Rx: " + rx + "Ax: " + ax + "Qx: " + qx );
		    Y.setText("Hy: "+ hy + "Ry: " + ry + "Ay: " + ay + "Qy: " + qy );
		    Z.setText("Hz: "+ hz + "Rz: " + rz + "Az: " + az + "Qz: " + qz );
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	
	    AccSalvas = new ArrayList<Double>();
	}
	
	
	public void startActivityVoltar(View view) {
		 
	    Intent Menu = new Intent(this, TesteAbasActivity.class); // Mudança de telas
	    
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

	
	private void salvarAceleracoes() {
    	String filename = "aceleracoes_com_filtro.txt";

    	FileOutputStream outputStream;
    	String entrada = new String();
    	int ii=0;
    	for (Double d : AccSalvas) {
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
	       
	   	Estado.setText("Gravando Acc");
    	gravaAcc = true;
		
	    }
	
	public void onClickSalvar(View v){
	       
    	salvarAceleracoes();
    	gravaAcc = false;
    	Estado.setText("Salvo");
		
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teste_abas, menu);
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
		
		 String AX = decimal.format(ax);
		 String AY = decimal.format(ay);
		 String AZ = decimal.format(az);
		
		accX.setText("x:"+(AX));
		accY.setText("y:"+(AY));
		accZ.setText("z:"+(AZ));
		
		//--------------------------------------------------------
		
		double hatax = fx.EvaluateValue(ax);
		double hatay = fy.EvaluateValue(ay);
		double hataz = fz.EvaluateValue(az);
		
		 String Hatax = decimal.format(hatax);
		 String Hatay = decimal.format(hatay);
		 String Hataz = decimal.format(hataz);
		 
		 if (gravaAcc) {
				AccSalvas.add(hatax);
				AccSalvas.add(hatay);
				AccSalvas.add(hataz);
			}
			
		 
		 accHatX.setText("x:"+(Hatax));
		 accHatY.setText("y:"+(Hatay));
		 accHatZ.setText("z:"+(Hataz));
		
		
		
		}
	
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
