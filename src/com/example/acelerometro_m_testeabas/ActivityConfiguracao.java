package com.example.acelerometro_m_testeabas;


import java.text.DecimalFormat;

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
import android.widget.Button;
import android.widget.TextView;
import net.trucomanx.pdsplibj.pdsdf.*;
import net.trucomanx.pdsplibj.pdsra.PdsVector;



public class ActivityConfiguracao extends ActionBarActivity implements SensorEventListener {
	
	DecimalFormat decimal = new DecimalFormat( "0.###" );
	
	SensorManager sm;
	Sensor acelerometro;
	
	TextView titulo = null;
	TextView Valoresx = null;
	TextView Valoresy = null;
	TextView Valoresz = null;
	
	double Hx=1.0,Rx=2.0,Ax=0.5,Qx=1.0;
    double Hy=1.0,Ry=2.0,Ay=0.5,Qy=1.0;
    double Hz=1.0,Rz=2.0,Az=0.5,Qz=1.0;
    
    int N = 250;
    int contRepouso=0;
    int contAtividade=0;
    
    boolean repouso = false;
    boolean atividade = false;
	boolean repousoArmazenado = false;
	boolean atividadeArmazenado = false;
    
	PdsKalman1DTool X = new PdsKalman1DTool(Hx,N);
	PdsVector Dx = X.GetParameters();

	
	PdsKalman1DTool Y = new PdsKalman1DTool(Hy,N);
	PdsVector Dy = Y.GetParameters();
	
	PdsKalman1DTool Z = new PdsKalman1DTool(Hz,N);
	PdsVector Dz = Z.GetParameters();

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracao);
		
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE); // Acessando os sensores
		acelerometro = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);// Acessando o acelerometro 
		
		titulo = (TextView) findViewById(R.id.estado);
		Valoresx = (TextView) findViewById(R.id.textView4);
		Valoresy = (TextView) findViewById(R.id.textView5);
		Valoresz = (TextView) findViewById(R.id.textView6);
		
	
		Valoresx.setText("Hx: "+ Hx + "Rx: " + Rx + "Ax: " + Ax + "Qx: " + Qx );
        Valoresy.setText("Hy: "+ Hy + "Ry: " + Ry + "Ay: " + Ay + "Qy: " + Qy );
        Valoresz.setText("Hz: "+ Hz + "Rz: " + Rz + "Az: " + Az + "Qz: " + Qz );
		
	
		 /*Intent params = new Intent();
			{
				params.putExtra("HX", Hx);
				params.putExtra("HY", Hy);
			    params.putExtra("HZ", Hz);
			    params.putExtra("RX", Rx);
			    params.putExtra("RY", Ry);
			    params.putExtra("RZ", Rz);
			    params.putExtra("AX", Ax);
			    params.putExtra("AY", Ay);
			    params.putExtra("AZ", Az);
			    params.putExtra("QX", Qx);
			    params.putExtra("QY", Qy);
			    params.putExtra("QZ", Qz);
			}*/

        
        /*final Button OKrepouso = (Button) this.findViewById(R.id.button1);
        OKrepouso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            
            	titulo.setText("Realizando Passo 1");
            	repouso = true;
            	repousoArmazenado = false;
            	
            }
        });
        
        
        final Button OKatividade = (Button) this.findViewById(R.id.button2);
        OKatividade.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            
            	titulo.setText("Realizando Passo 2");
            	atividade = true;
            	atividadeArmazenado = false;
            	
            }
        });*/
        

		
		        
        
	}

	
	public void startActivityAceleracao(View view) {
		 
	    Intent Aceleracao = new Intent(this, TesteAbasActivity.class);
	    
	    Aceleracao.putExtra("HX", Hx);
	    Aceleracao.putExtra("HY", Hy);
	    Aceleracao.putExtra("HZ", Hz);
	    Aceleracao.putExtra("RX", Rx);
	    Aceleracao.putExtra("RY", Ry);
	    Aceleracao.putExtra("RZ", Rz);
	    Aceleracao.putExtra("AX", Ax);
	    Aceleracao.putExtra("AY", Ay);
	    Aceleracao.putExtra("AZ", Az);
	    Aceleracao.putExtra("QX", Qx);
	    Aceleracao.putExtra("QY", Qy);
	    Aceleracao.putExtra("QZ", Qz);
	    
	    startActivity(Aceleracao);
	}
	
	public void onClickRepouso(View v){
	       
	 	titulo.setText("Realizando Passo 1");
    	repouso = true;
    	//titulo.setText(String.valueOf(repouso));
    	repousoArmazenado = false;
	
	    }
	
	public void onClickAtividade(View v){
	       
	 	titulo.setText("Realizando Passo 2");
    	atividade = true;
    	atividadeArmazenado = false;
	
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
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_configuracao, menu);
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
		
				
		 if(repouso)
         {
		
             X.AddValueR(ax);
             Y.AddValueR(ay);
             Z.AddValueR(az);
             ++contRepouso;
         
             

             
         }
		 
         if(contRepouso > N)
         {   
             repouso = false;
             contRepouso = 0;
             repousoArmazenado = true;
             titulo.setText("Aguardando Passo 2");
         }
                /* if ((repousoArmazenado == true) && (atividadeArmazenado == true))
                 {
                	 titulo.setText("Configuração Relizada!");
                 
                 Hx=Dx.GetValue(0);      Rx=Dx.GetValue(1);        Ax=Dx.GetValue(2);        Qx=Dx.GetValue(3);
                 Hy=Dy.GetValue(0);      Ry=Dy.GetValue(1);        Ay=Dy.GetValue(2);        Qy=Dy.GetValue(3);
                 Hz=Dz.GetValue(0);      Rz=Dz.GetValue(1);        Az=Dz.GetValue(2);        Qz=Dz.GetValue(3);

                 Valoresx.setText("Hx: "+ Hx + "Rx: " + Rx + "Ax: " + Ax + "Qx: " + Qx );
                 Valoresy.setText("Hy: "+ Hy + "Ry: " + Ry + "Ay: " + Ay + "Qy: " + Qy );
                 Valoresz.setText("Hz: "+ Hz + "Rz: " + Rz + "Az: " + Az + "Qz: " + Qz );
                 
                 
             
         }*/
		 
		 
		 
		 if(atividade)
         {			 
             X.AddValueA(ax);
             Y.AddValueA(ay);
             Z.AddValueA(az);
             ++contAtividade;
         
  
         } 
		 
         if(contAtividade >N)
         {
             atividade = false;
             contAtividade = 0;
             atividadeArmazenado = true;
         }    
                 
                 if ((repousoArmazenado) && (atividadeArmazenado))
                 {
                	 titulo.setText("Configuração Relizada!");
                	 
                 
                 Dx = X.GetParameters();
                 Dy = Y.GetParameters();
                 Dz = Z.GetParameters();
                	 
                 Hx=Dx.GetValue(0);      Rx=Dx.GetValue(1);        Ax=Dx.GetValue(2);        Qx=Dx.GetValue(3);
                 Hy=Dy.GetValue(0);      Ry=Dy.GetValue(1);        Ay=Dy.GetValue(2);        Qy=Dy.GetValue(3);
                 Hz=Dz.GetValue(0);      Rz=Dz.GetValue(1);        Az=Dz.GetValue(2);        Qz=Dz.GetValue(3);
                 
                 String HX = decimal.format(Hx);
        		 String HY = decimal.format(Hy);
        		 String HZ = decimal.format(Hz);
        		 
        		 String RX = decimal.format(Rx);
        		 String RY = decimal.format(Ry);
        		 String RZ = decimal.format(Rz);
        		 
        		 String AX = decimal.format(Ax);
        		 String AY = decimal.format(Ay);
        		 String AZ = decimal.format(Az);
        		 
        		 String QX = decimal.format(Qx);
        		 String QY = decimal.format(Qy);
        		 String QZ = decimal.format(Qz);

        		 
        		/* Intent params = new Intent();
        			{
        				params.putExtra("HX", Hx);
        				params.putExtra("HY", Hy);
        			    params.putExtra("HZ", Hz);
        			    params.putExtra("RX", Rx);
        			    params.putExtra("RY", Ry);
        			    params.putExtra("RZ", Rz);
        			    params.putExtra("AX", Ax);
        			    params.putExtra("AY", Ay);
        			    params.putExtra("AZ", Az);
        			    params.putExtra("QX", Qx);
        			    params.putExtra("QY", Qy);
        			    params.putExtra("QZ", Qz);
        			}*/
                 
                 Valoresx.setText("Hx: "+ HX + "Rx: " + RX + "Ax: " + AX + "Qx: " + QX );
                 Valoresy.setText("Hy: "+ HY + "Ry: " + RY + "Ay: " + AY + "Qy: " + QY );
                 Valoresz.setText("Hz: "+ HZ + "Rz: " + RZ + "Az: " + AZ + "Qz: " + QZ );

             }
         
		
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
