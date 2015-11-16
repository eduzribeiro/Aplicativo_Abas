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

public class TesteAbasActivity extends ActionBarActivity  {
	
	Double Ax2;
	Double Ay2;
	Double Az2;
	
	Double Hx2;
	Double Hy2;
	Double Hz2;
	
	Double Qx2;
	Double Qy2;
	Double Qz2;
	
	Double Rx2;
	Double Ry2;
	Double Rz2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teste_abas);
		
	Intent intent = getIntent();
		
		Hx2 = intent.getDoubleExtra("HX", 1.0);
		Hy2 = intent.getDoubleExtra("HY", 1.0);
		Hz2 = intent.getDoubleExtra("HZ", 1.0);
		Rx2 = intent.getDoubleExtra("RX", 2.0);
		Ry2 = intent.getDoubleExtra("RY", 2.0);
		Rz2 = intent.getDoubleExtra("RZ", 2.0);
		Ax2 = intent.getDoubleExtra("AX", 0.5);
		Ay2 = intent.getDoubleExtra("AY", 0.5);
		Az2 = intent.getDoubleExtra("AZ", 0.5);
		Qx2 = intent.getDoubleExtra("QX", 1.0);
		Qy2 = intent.getDoubleExtra("QY", 1.0);
		Qz2 = intent.getDoubleExtra("QZ", 1.0);
		
		
		
	}
	
	
	
	public void startActivityAceleracao(View view) {
		Intent Aceleracao = new Intent(this, ActivityAceleracao.class); // Mudança de telas
		
		 	Aceleracao.putExtra("HX2", Hx2);
		 	Aceleracao.putExtra("HY2", Hy2);
		 	Aceleracao.putExtra("HZ2", Hz2);
		 	Aceleracao.putExtra("RX2", Rx2);
		 	Aceleracao.putExtra("RY2", Ry2);
		 	Aceleracao.putExtra("RZ2", Rz2);
		 	Aceleracao.putExtra("AX2", Ax2);
		 	Aceleracao.putExtra("AY2", Ay2);
		 	Aceleracao.putExtra("AZ2", Az2);
		 	Aceleracao.putExtra("QX2", Qx2);
		 	Aceleracao.putExtra("QY2", Qy2);
		 	Aceleracao.putExtra("QZ2", Qz2);
		
		startActivity(Aceleracao);
				 
		
	}
	
	public void startActivityVelocidade(View view) {
		 
	    Intent Velocidade = new Intent(this, ActivityVelocidade.class); // Mudança de telas
	    
		Velocidade.putExtra("HX3", Hx2);
		Velocidade.putExtra("HY3", Hy2);
		Velocidade.putExtra("HZ3", Hz2);
		Velocidade.putExtra("RX3", Rx2);
		Velocidade.putExtra("RY3", Ry2);
		Velocidade.putExtra("RZ3", Rz2);
		Velocidade.putExtra("AX3", Ax2);
		Velocidade.putExtra("AY3", Ay2);
		Velocidade.putExtra("AZ3", Az2);
		Velocidade.putExtra("QX3", Qx2);
		Velocidade.putExtra("QY3", Qy2);
		Velocidade.putExtra("QZ3", Qz2);
	    
	    startActivity(Velocidade);
	}
	
	public void startActivityDeslocamento(View view) {
		 
	    Intent Deslocamento = new Intent(this, ActivityDeslocamento.class); // Mudança de telas
	    
		Deslocamento.putExtra("HX4", Hx2);
		Deslocamento.putExtra("HY4", Hy2);
		Deslocamento.putExtra("HZ4", Hz2);
		Deslocamento.putExtra("RX4", Rx2);
		Deslocamento.putExtra("RY4", Ry2);
		Deslocamento.putExtra("RZ4", Rz2);
		Deslocamento.putExtra("AX4", Ax2);
		Deslocamento.putExtra("AY4", Ay2);
		Deslocamento.putExtra("AZ4", Az2);
		Deslocamento.putExtra("QX4", Qx2);
		Deslocamento.putExtra("QY4", Qy2);
		Deslocamento.putExtra("QZ4", Qz2);
	    
	    startActivity(Deslocamento);
	}
	
	public void startActivitySalvarTudo(View view) {
		 
	    Intent Salvar = new Intent(this, ActivitySalvarDados.class); // Mudança de telas
	    
	    Salvar.putExtra("HX5", Hx2);
	    Salvar.putExtra("HY5", Hy2);
	    Salvar.putExtra("HZ5", Hz2);
	    Salvar.putExtra("RX5", Rx2);
	    Salvar.putExtra("RY5", Ry2);
	    Salvar.putExtra("RZ5", Rz2);
	    Salvar.putExtra("AX5", Ax2);
	    Salvar.putExtra("AY5", Ay2);
	    Salvar.putExtra("AZ5", Az2);
	    Salvar.putExtra("QX5", Qx2);
	    Salvar.putExtra("QY5", Qy2);
	    Salvar.putExtra("QZ5", Qz2);
	    
	    startActivity(Salvar);
	}
	
	public void startActivityConfiguracao(View view) {
		 
	    Intent Configurar = new Intent(this, ActivityConfiguracao.class); // Mudança de telas
	    startActivity(Configurar);
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

	
}
